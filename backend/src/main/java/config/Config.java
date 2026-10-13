package config;

import java.nio.file.Paths;

import io.github.cdimascio.dotenv.Dotenv;

public class Config {
    // Werte kommen aus Umgebungsvariablen (von Tauri gesetzt) oder einer optionalen .env-Datei
    private static final Dotenv ENV = Dotenv.configure().ignoreIfMissing().load();

    private static String get(String key, String fallback) {
        String value = ENV.get(key);
        return (value == null || value.isBlank()) ? fallback : value;
    }

    // In der Tauri-App: <AppData>/com.perin.mtgdatenbank/data/, sonst ./data/
    private static final String APP_DATA_DIR = get("APP_DATA_DIR", ".");
    public static final String DATA_PATH = get("DATA_PATH",
            Paths.get(APP_DATA_DIR, "data").toString() + java.io.File.separator);

    //Databank
    public static final String PORT = get("DB_PORT", "5432");
    public static final String DATABASE = get("DB_NAME", "postgres");
    public static final String USERNAME = get("DB_USER", "postgres");
    public static final String PASSWORD = get("DB_PASSWORD", "1234");
    public static final String HOST = get("DB_HOST", "localhost");
    public static final String URL ="jdbc:postgresql://" + HOST + ":" + PORT + "/" + DATABASE;
}
