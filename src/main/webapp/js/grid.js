    const canvas = document.getElementById("grid-canvas");
    const ctx = canvas.getContext("2d");
    let selectedColor = "#FF0000";

    // dessiner la grille
    function drawGrid() {
        for (let x = 0; x < 50; x++) {
            for (let y = 0; y < 50; y++) {
                ctx.fillStyle = "#FFFFFF";
                ctx.fillRect(x * 20, y * 20, 20, 20);
                ctx.strokeStyle = "rgba(0,0,0,0.1)";
                ctx.strokeRect(x * 20, y * 20, 20, 20);
            }
        }
    }

    // écouter un clic
    canvas.addEventListener("click", function (e) {
        const rect = canvas.getBoundingClientRect();
        const scaleX = canvas.width / rect.width;
        const scaleY = canvas.height / rect.height;
        const x = Math.floor((e.clientX - rect.left) * scaleX / 20);
        const y = Math.floor((e.clientY - rect.top)  * scaleY / 20);
        drawPixel(x, y, selectedColor);
    });

    // colorier un pixel
    function drawPixel(x, y, color) {
        ctx.fillStyle = color;
        ctx.fillRect(x * 20, y * 20, 20, 20);
    }

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
        selectedColor = pixel.style.background;
        document.getElementById("selected-color-rectangle").style.background = selectedColor;
      });
    });