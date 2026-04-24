    const canvas = document.getElementById("grid-canvas");
    const ctx = canvas.getContext("2d");

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
        drawPixel(x, y, "#FF0000");
    });

    // colorier un pixel
    function drawPixel(x, y, color) {
        ctx.fillStyle = color;
        ctx.fillRect(x * 20, y * 20, 20, 20);
    }

    drawGrid();