    const canvas = document.getElementById("grid-canvas");
    const ctx = canvas.getContext("2d");
    const cellSize = 20; //plus simple pour modifeir et tester
    const gridSize = 50; //''
    let selectedColor = "#FF0000";
    let socket = null;
    //ajout objet pour les infos sur les pixel 
    let gridData = {};
    // quadrillage discret
    const GRID_LINE = "#E5E7EB";
    // prix d'une case jamais peinte, envoye par le serveur dans le message init
    let emptyPixelPrice = null;

    // dessiner la grille (vide)
    function drawGrid() {
        for (let x = 0; x < gridSize; x++) {
            for (let y = 0; y < gridSize; y++) {
                ctx.fillStyle = "#FFFFFF";
                ctx.fillRect(x * cellSize, y * cellSize, cellSize, cellSize);
                ctx.strokeStyle = GRID_LINE;
                ctx.strokeRect(x * cellSize, y * cellSize, cellSize, cellSize);
            }
        }
    }

    // colorier un pixel
    function drawPixel(x, y, color) {
        ctx.fillStyle = color;
        ctx.fillRect(x * cellSize, y * cellSize, cellSize, cellSize);
        //ajout ligne noir pour visibiliter des pixels c'est plus simple de viser les couleur deja peintes
        ctx.strokeStyle = GRID_LINE;
        ctx.strokeRect(x*cellSize, y*cellSize, cellSize, cellSize);
    }

    // ouvrir le websocket
    function openGrid() {
        document.getElementById("grid-overlay").classList.remove("hidden");

        if (socket !== null && socket.readyState == WebSocket.OPEN) return;

        const protocole = window.location.protocol === "https:" ? "wss://" : "ws://";
        const url = protocole + window.location.host + contextPath + "/grid";
        socket = new WebSocket(url);

        // utilisation de l'ia pour gérer la réception des messages
        socket.onmessage = function(event) {
            const data = JSON.parse(event.data);
            if (data.type == "init") {
                emptyPixelPrice = data.emptyPrice;
                drawGrid();
                data.pixels.forEach(p => { 
                    drawPixel(p.x, p.y, p.color);
                    gridData[`${p.x},${p.y}`] = { pseudo: p.player, date: p.date, price: p.price, color: p.color };
                });

            } else if (data.type == "pixel") {
                drawPixel(data.x, data.y, data.color);
                gridData[`${data.x},${data.y}`] = { pseudo: data.player, date: data.date, price: data.price, color: data.color };
            } else if (data.type == "error") {
                showToast(data.message);
            }
            if (data.newBalance !== undefined) {
                setCredits(data.newBalance);
            }
        };
    }

    // zoom (molette) et deplacement (glisser) par transform CSS : getBoundingClientRect
    // tient compte de la transformation, donc le calcul de la case cliquee ne change pas
    const MIN_ZOOM = 1;
    const MAX_ZOOM = 8;
    let zoom = 1;
    let panX = 0;
    let panY = 0;
    let dragStart = null;
    let dragged = false;

    canvas.style.transformOrigin = "0 0";

    function applyTransform() {
        if (zoom === MIN_ZOOM) {
            panX = 0;
            panY = 0;
        }
        canvas.style.transform = `translate(${panX}px, ${panY}px) scale(${zoom})`;
        canvas.style.cursor = zoom > MIN_ZOOM ? "grab" : "";
    }

    function resetZoom() {
        zoom = MIN_ZOOM;
        applyTransform();
    }

    canvas.addEventListener("wheel", (e) => {
        e.preventDefault();
        const rect = canvas.getBoundingClientRect();
        const newZoom = Math.min(MAX_ZOOM, Math.max(MIN_ZOOM, zoom * (e.deltaY < 0 ? 1.2 : 1 / 1.2)));
        // garde le point sous le curseur au meme endroit
        const px = e.clientX - rect.left;
        const py = e.clientY - rect.top;
        panX += px * (1 - newZoom / zoom);
        panY += py * (1 - newZoom / zoom);
        zoom = newZoom;
        applyTransform();
    }, { passive: false });

    canvas.addEventListener("pointerdown", (e) => {
        if (zoom === MIN_ZOOM) return;
        dragStart = { x: e.clientX, y: e.clientY, panX, panY };
        dragged = false;
        canvas.setPointerCapture(e.pointerId);
    });

    canvas.addEventListener("pointermove", (e) => {
        if (!dragStart) return;
        const dx = e.clientX - dragStart.x;
        const dy = e.clientY - dragStart.y;
        // au-dela de 5px c'est un deplacement, pas un clic
        if (Math.abs(dx) > 5 || Math.abs(dy) > 5) dragged = true;
        if (dragged) {
            panX = dragStart.panX + dx;
            panY = dragStart.panY + dy;
            canvas.style.cursor = "grabbing";
            applyTransform();
        }
    });

    canvas.addEventListener("pointerup", () => {
        dragStart = null;
        if (zoom > MIN_ZOOM) canvas.style.cursor = "grab";
    });

    // écouter un clic
    canvas.addEventListener("click",async function (e) {
        // fin d'un glisser : ne pas poser de pixel
        if (dragged) {
            dragged = false;
            return;
        }
        if (!isConnected) return;

        await fetch('save', { method: 'POST' }); 

        if (socket == null || socket.readyState !== WebSocket.OPEN) return;

        const rect = canvas.getBoundingClientRect();
        const scaleX = canvas.width / rect.width;
        const scaleY = canvas.height / rect.height;
        const x = Math.floor((e.clientX - rect.left) * scaleX / cellSize);
        const y = Math.floor((e.clientY - rect.top)  * scaleY / cellSize);
        // envoyer un nouveau message au websocket
        const json = JSON.stringify({ x: x, y: y, color: selectedColor })
        socket.send(json);
    });

    drawGrid();

    // ouvrir / fermer la palette
    function handlePalette() {
      const drawer = document.getElementById("palette-drawer");
      const icon = document.getElementById("palette-icon");
      const button = document.getElementById("palette-btn");

      const isHidden = drawer.classList.contains("hidden");

      drawer.classList.toggle("hidden");
      drawer.style.display = isHidden ? "flex" : "none";

      if (isHidden) {
        button.classList.replace("-right-5", "right-52");
        icon.classList.replace("fa-chevron-left", "fa-chevron-right");
      } else {
        button.classList.replace("right-52", "-right-5");
        icon.classList.replace("fa-chevron-right", "fa-chevron-left");
      }
    }

    // sélectionner une couleur
    document.querySelectorAll(".selected-color").forEach(pixel => {
        pixel.addEventListener("click", () => {
            changeSelectedColor(pixel.getAttribute("data-color"));
        });
    });

    // Fonction pour changer la couleur sélectionnée avec les couleurs bonus
    function changeSelectedColor(color) {
        if (!isConnected) return;
        selectedColor = color;
        document.getElementById("selected-color-rectangle").style.background = selectedColor;
        // anneau autour de la couleur choisie
        document.querySelectorAll("#palette-grid [data-color]").forEach(swatch => {
            swatch.classList.toggle("ring-2", swatch.dataset.color === color);
            swatch.classList.toggle("ring-offset-2", swatch.dataset.color === color);
            swatch.classList.toggle("ring-cookie-600", swatch.dataset.color === color);
        });
    }

    changeSelectedColor(selectedColor);

    //action overlay au survol des pixels
    const popover = document.getElementById("pixel-popover");
    const popoverContent = document.getElementById("popover-content");
    
    canvas.addEventListener("mousemove", (e) => {
        const rect = canvas.getBoundingClientRect();
        const x = Math.floor((e.clientX - rect.left) * (canvas.width / rect.width) / cellSize);
        const y = Math.floor((e.clientY - rect.top) * (canvas.height / rect.height) / cellSize);
        // memes libelles que le serveur pour une case jamais peinte
        const info = gridData[`${x},${y}`] || {pseudo: "Personne", date: "Jamais peint", price: emptyPixelPrice};

        if (info) {
            // pastille de la couleur de la case puis le pseudo
            const swatch = document.createElement("span");
            swatch.className = "inline-block w-3 h-3 mr-1.5 align-middle rounded-sm border border-white/40";
            swatch.style.backgroundColor = info.color || "#FFFFFF";
            const pseudo = document.createElement("strong");
            pseudo.textContent = info.pseudo;
            const date = document.createElement("span");
            date.className = "text-xs text-cookie-200";
            // Peint le 2/10/2025 à 22:00"
            const parts = /^(\d{2}\/\d{2}\/\d{4}) (\d{2}:\d{2})$/.exec(info.date);
            date.textContent = parts ? `Peint le ${parts[1]} à ${parts[2]}` : info.date;

            popoverContent.replaceChildren(swatch, pseudo, document.createElement("br"));
            if (isConnected && info.price != null) {
                popoverContent.append("Prix : " + formatCredits(info.price) + " crédits", document.createElement("br"));
            }
            popoverContent.append(date);
            popover.style.left = (e.clientX - 50) + "px";
             if (e.clientY < 150) {
             popover.style.top = (e.clientY + 20) + "px";
         } else {
             popover.style.top = (e.clientY - 90) + "px";
         }
            popover.classList.replace("opacity-0","opacity-100");
        } else {
            popover.classList.replace("opacity-100","opacity-0");
        }
    });
   canvas.addEventListener("mouseleave", () => popover.classList.replace("opacity-100", "opacity-0"));