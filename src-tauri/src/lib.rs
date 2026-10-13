use std::path::PathBuf;
use std::process::{Child, Command};
use std::sync::Mutex;
use std::{thread, time::Duration};
use tauri::{Manager, RunEvent};

const DB_PORT: &str = "5433";

struct Processes {
    children: Mutex<Vec<Child>>,
    pg_bin: PathBuf,
    pg_data: PathBuf,
}

#[cfg(windows)]
fn hide_window(cmd: &mut Command) {
    use std::os::windows::process::CommandExt;
    cmd.creation_flags(0x08000000); // kein Konsolenfenster
}
#[cfg(not(windows))]
fn hide_window(_cmd: &mut Command) {}

fn exe(dir: &PathBuf, name: &str) -> PathBuf {
    dir.join(if cfg!(windows) { format!("{name}.exe") } else { name.to_string() })
}

#[cfg_attr(mobile, tauri::mobile_entry_point)]
pub fn run() {
    tauri::Builder::default()
        .setup(|app| {
            // Beim Entwickeln nichts starten: Backend aus der IDE, eigenes PostgreSQL
            if cfg!(debug_assertions) {
                return Ok(());
            }

            // dunce: "\\?\C:\..." -> "C:\...", sonst findet initdb postgres.exe nicht
            let data_dir = dunce::simplified(&app.path().app_data_dir()?).to_path_buf();
            std::fs::create_dir_all(&data_dir)?;
            let pg_data = data_dir.join("pgdata");

            let res = dunce::simplified(&app.path().resource_dir()?).to_path_buf();
            let pg_bin = res.join("postgres").join("bin");
            let backend_dir = res.join("backend");

            // 1. Beim allerersten Start Datenbank anlegen
            if !pg_data.join("PG_VERSION").exists() {
                let mut init = Command::new(exe(&pg_bin, "initdb"));
                init.arg("-D").arg(&pg_data)
                    .args(["-U", "postgres", "--auth=trust", "-E", "UTF8", "--locale=C"]);
                hide_window(&mut init);
                let status = init.status()?;
                if !status.success() {
                    return Err("initdb fehlgeschlagen".into());
                }
            }

            // 2. PostgreSQL starten (nur lokal erreichbar)
            let mut pg = Command::new(exe(&pg_bin, "postgres"));
            pg.arg("-D").arg(&pg_data)
              .args(["-p", DB_PORT, "-h", "127.0.0.1"]);
            hide_window(&mut pg);
            let postgres = pg.spawn()?;

            // 3. Warten, bis PostgreSQL Verbindungen annimmt (max. ~15 s)
            for _ in 0..30 {
                let mut ready = Command::new(exe(&pg_bin, "pg_isready"));
                ready.args(["-h", "127.0.0.1", "-p", DB_PORT]);
                hide_window(&mut ready);
                if ready.status().map(|s| s.success()).unwrap_or(false) {
                    break;
                }
                thread::sleep(Duration::from_millis(500));
            }

            // 4. Spring-Backend starten
            let mut java = Command::new(exe(&backend_dir.join("runtime").join("bin"), "java"));
            java.arg("-jar").arg(backend_dir.join("app.jar"))
                .arg("--server.port=8080")
                .env("DB_PORT", DB_PORT)
                .env("APP_DATA_DIR", &data_dir);
            hide_window(&mut java);
            let backend = java.spawn()?;

            app.manage(Processes {
                children: Mutex::new(vec![backend, postgres]),
                pg_bin,
                pg_data,
            });
            Ok(())
        })
        .build(tauri::generate_context!())
        .expect("Fehler beim Starten der App")
        .run(|app, event| {
            if let RunEvent::Exit = event {
                if let Some(state) = app.try_state::<Processes>() {
                    let mut children = state.children.lock().unwrap();

                    // Backend beenden
                    if let Some(backend) = children.get_mut(0) {
                        let _ = backend.kill();
                    }

                    // PostgreSQL sauber herunterfahren
                    let mut stop = Command::new(exe(&state.pg_bin, "pg_ctl"));
                    stop.arg("stop").arg("-D").arg(&state.pg_data).args(["-m", "fast"]);
                    hide_window(&mut stop);
                    let _ = stop.status();

                    // Falls noch etwas läuft: hart beenden
                    for child in children.iter_mut() {
                        let _ = child.kill();
                    }
                }
            }
        });
}