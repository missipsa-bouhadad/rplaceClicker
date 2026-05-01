    const canvas = document.getElementById("grid-canvas");
    const ctx = canvas.getContext("2d");
    const cellSize = 20; //plus simple pour modifeir et tester
    const gridSize = 50; //''
    let selectedColor = "#FF0000";
    let socket = null;

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

        const url = "ws://" + window.location.host + contextPath + "/grid";
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
                drawGrid();
                data.pixels.forEach(p => drawPixel(p.x, p.y, p.color));
            } else if (data.type == "pixel") {
                drawPixel(data.x, data.y, data.color);
            } else if (data.type == "error") {
                alert(data.message);
            }
        };
    }

    // écouter un clic
    canvas.addEventListener("click", function (e) {
        if (!isConnected) return;

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
        if (!isConnected) return;
        selectedColor = pixel.getAttribute("data-color");
        document.getElementById("selected-color-rectangle").style.background = selectedColor;
      });
    });