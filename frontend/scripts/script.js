let filters = {
  color: null,
  type: null,
  cmc: 10,
  rarity: null,
  oracle_text: null
};
const host = "http://localhost:8080"; // noch nicht fertig implementiert 
const shoppingCart = [];
const HALTEDAUER = 600;
console.log("JS FILE LOADED");

document.addEventListener("DOMContentLoaded", () => {
    console.log("DOM READY");

    searchbar();
    initDropzone();
    loadCards();
});

// --------------------
// DROPZONE
// --------------------
console.log("JS FILE LOADED");

document.addEventListener("DOMContentLoaded", () => {
    console.log("DOM READY");
    initDropzone();
});

function initDropzone() {
    const dropzone = document.getElementById("dropzone");
    if (!dropzone) return;

    const isCSV = (file) => file && file.name.toLowerCase().endsWith(".csv");

    dropzone.addEventListener("dragover", (e) => {
        e.preventDefault();
        dropzone.classList.add("active");
    });

    dropzone.addEventListener("dragleave", () => {
        dropzone.classList.remove("active", "csv", "loading", "error");
    });

    dropzone.addEventListener("drop", (e) => {
        e.preventDefault();
        dropzone.classList.remove("active");

        const file = e.dataTransfer.files[0];
        if (!file) return;

        if (!isCSV(file)) {
            dropzone.classList.add("error");
            setTimeout(() => dropzone.classList.remove("error"), 800);
            return;
        }

        dropzone.classList.add("csv", "loading");

        setTimeout(() => {
            dropzone.classList.remove("loading");
            sendToBackend(file);
        }, 300);
    });
}

// --------------------
// UPLOAD
// --------------------
function sendToBackend(file) {
    const formData = new FormData();
    formData.append("file", file);

    fetch("http://localhost:8080/api/upload", {
        method: "POST",
        body: formData
    })
    .then(res => res.text())
    .then(data => console.log("Server:", data))
    .catch(err => console.error(err));
}

// --------------------
// CARDS DISPLAY
// --------------------
async function loadCards() {
  const container = document.getElementById("collection");

  const params = new URLSearchParams();

  if (filters.color) params.append("color", filters.color);
  if (filters.type) params.append("type", filters.type);
  if (filters.rarity) params.append("rarity", filters.rarity);
  if (filters.cmc !== null) params.append("cmc", filters.cmc);
  if (filters.oracle_text) params.append("oracle_text", filters.oracle_text);

  const response = await fetch(host + `/api/cards?${params.toString()}`);
  const cards = await response.json();

  container.innerHTML = "";
  document.getElementById("CardCounttext").textContent = cards.length + " Different Cards!";

  cards.forEach(card => {
    // Wrapper für Bild + Zahl
    const wrapper = document.createElement("div");
    wrapper.className = "card-wrapper";

    const img = document.createElement("img");
    img.className = "card";
    img.draggable = false;

    img.style.background = "linear-gradient(90deg, #444, #666, #444)";
    img.style.backgroundSize = "200%";
    img.style.animation = "loading 1s infinite";

    img.src = `${host}/api/card/image/${card.id}`;
    img.loading = "lazy";

    img.onload = () => {
      img.style.animation = "none";
      img.style.background = "none";
    };

    // Zahl
    const badge = document.createElement("span");
    badge.className = "badge";
    badge.textContent = card.count;
    badge.hidden = card.count <= 1;

    wrapper.append(img, badge);

    // Long-Press
    let timer;

    wrapper.addEventListener("contextmenu", (e) => e.preventDefault());

    wrapper.addEventListener("pointerdown", () => {
      img.style.transition = `opacity ${HALTEDAUER}ms linear`;
      img.style.opacity = "0.01";

      timer = setTimeout(async () => {
        const res = await fetch(host + `/api/cards/deleat/${card.id}`);

        if (!res.ok) {
          console.error("Fehler:", res.status);
          abbrechen();
          return;
        }

        const neuerCount = await res.json();

        if (neuerCount <= 0) {
          wrapper.remove();
        } else {
          badge.textContent = neuerCount;
          badge.hidden = neuerCount <= 1;
          abbrechen();
        }
      }, HALTEDAUER);
    });

    function abbrechen() {
      clearTimeout(timer);
      img.style.transition = "opacity 0.2s";
      img.style.opacity = "1";
    }

    wrapper.addEventListener("pointerup", abbrechen);
    wrapper.addEventListener("pointerleave", abbrechen);
    wrapper.addEventListener("pointercancel", abbrechen);

    container.appendChild(wrapper);
  });
}

function searchbar() {
  filters.color = document.getElementById("color").value || null;
  filters.type = document.getElementById("type").value || null;
  filters.rarity = document.getElementById("rarity").value || null;

  const cmcRaw = document.getElementById("ManaCost").value;
  filters.cmc = cmcRaw !== "" ? Number(cmcRaw) : 10;

  filters.oracle_text ="%" + document.getElementById("query").value + "%" || null;

  loadCards();
}
document.getElementById("searchForm").addEventListener("submit", (e) => {
  e.preventDefault();
  searchbar();
});

/*async function getCardCount() {
  const response = await fetch(host + "/api/cards/count");
  const cardCount = await response.json();

  document.getElementById("CardCounttext").textContent = cardCount + " Diffrent Cards!";
}*/
