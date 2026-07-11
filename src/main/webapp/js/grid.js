    const canvas = document.getElementById("grid-canvas");
    const ctx = canvas.getContext("2d");
    const cellSize = 20; //plus simple pour modifeir et tester
    const gridSize = 50; //''
    let selectedColor = "#FF0000";
    let socket = null;
    //ajout objet pour les infos sur les pixel 
    let gridData = {};
    // prix d'une case jamais peinte, envoye par le serveur dans le message init
    let emptyPixelPrice = null;

    // dessiner la grille (vide)
    function drawGrid() {
        for (let x = 0; x < gridSize; x++) {
            for (let y = 0; y < gridSize; y++) {
                ctx.fillStyle = "#FFFFFF";
                ctx.fillRect(x * cellSize, y * cellSize, cellSize, cellSize);
                ctx.strokeStyle = "#000000";
                ctx.strokeRect(x * cellSize, y * cellSize, cellSize, cellSize);
            }
        }
    }

    // colorier un pixel
    function drawPixel(x, y, color) {
        ctx.fillStyle = color;
        ctx.fillRect(x * cellSize, y * cellSize, cellSize, cellSize);
        //ajout ligne noir pour visibiliter des pixels c'est plus simple de viser les couleur deja peintes
        ctx.strokeStyle = "#000000";
        ctx.strokeRect(x*cellSize, y*cellSize, cellSize, cellSize);
    }

    // ouvrir le websocket
    function openGrid() {
        document.getElementById("grid-overlay").classList.remove("hidden");

        if (socket !== null && socket.readyState == WebSocket.OPEN) return;

        const protocole = window.location.protocol === "https:" ? "wss://" : "ws://";
        const url = protocole + window.location.host + contextPath + "/grid";
        socket = new WebSocket(url);

        /*socket.onopen = function() {
            console.log("connecté");
        };

        socket.onerror = function(error) {
            console.log("erreur", error);
        };

        socket.onclose = function() {
            console.log("fermé");
        };*/

        // utilisation de l'ia pour gérer la réception des messages
        socket.onmessage = function(event) {
            //console.log("message: ", event.data);
            const data = JSON.parse(event.data);
            if (data.type == "init") {
                emptyPixelPrice = data.emptyPrice;
                drawGrid();
                data.pixels.forEach(p => { 
                    drawPixel(p.x, p.y, p.color);
                    gridData[`${p.x},${p.y}`] = { pseudo: p.player, date: p.date, price: p.price };
                });

            } else if (data.type == "pixel") {
                drawPixel(data.x, data.y, data.color);
                gridData[`${data.x},${data.y}`] = { pseudo: data.player, date: data.date, price: data.price };
            } else if (data.type == "error") {
                showToast(data.message);
            }
            if (data.newBalance !== undefined) {
                setCredits(data.newBalance);
            }
        };
    }

    // écouter un clic
    canvas.addEventListener("click",async function (e) {
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
        button.classList.replace("-right-5", "right-48");
        icon.classList.replace("fa-chevron-left", "fa-chevron-right");
      } else {
        button.classList.replace("right-48", "-right-5");
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
    }

    //action overlay au survol des pixels
    const popover = document.getElementById("pixel-popover");
    const popoverContent = document.getElementById("popover-content");
    
    canvas.addEventListener("mousemove", (e) => {
        const rect = canvas.getBoundingClientRect();
        const x = Math.floor((e.clientX - rect.left) * (canvas.width / rect.width) / cellSize);
        const y = Math.floor((e.clientY - rect.top) * (canvas.height / rect.height) / cellSize);
        // memes libelles que le serveur pour une case jamais peinte
        const info = gridData[`${x},${y}`] || {pseudo: "Personne", date: "Jamais peinte", price: emptyPixelPrice};

        if (info) {
            const pseudo = document.createElement("strong");
            pseudo.textContent = info.pseudo;
            const date = document.createElement("span");
            date.className = "text-xs";
            date.textContent = info.date;

            popoverContent.replaceChildren(pseudo, document.createElement("br"));
            if (isConnected && info.price != null) {
                popoverContent.append("Prix : " + formatCredits(info.price) + " crédits", document.createElement("br"));
            }
            popoverContent.append(date);
            popover.style.left = (e.clientX - 50) + "px";
             if (e.clientY < 150) {
             popover.style.top = (e.clientY + 20) + "px";
         } else {
             popover.style.top = (e.clientY - 125) + "px";
         }
            popover.classList.replace("opacity-0","opacity-100");
        } else {
            popover.classList.replace("opacity-100","opacity-0");
        }
    });
   canvas.addEventListener("mouseleave", () => popover.classList.replace("opacity-100", "opacity-0"));