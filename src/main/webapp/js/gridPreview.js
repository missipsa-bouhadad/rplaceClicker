document.addEventListener('DOMContentLoaded', () => {
    const canvas = document.getElementById('gridCanvas');
    const pCtx = canvas.getContext('2d');
    
    const grille = 50; 
    const cellSize = canvas.width / grille; 
    // meme quadrillage que la grande grille
    const GRID_LINE = "#E5E7EB";

    const drawPixel = (x, y, color) => {
        pCtx.fillStyle = color;
        pCtx.fillRect(x * cellSize, y * cellSize, cellSize, cellSize);
        //bordure pixl
        pCtx.strokeStyle = GRID_LINE;
        pCtx.lineWidth = 0.5;
        pCtx.strokeRect(x * cellSize, y * cellSize, cellSize, cellSize);
    };

    //creation du quadrillage
    const renderGrid = () => {
        pCtx.beginPath();
        pCtx.strokeStyle = GRID_LINE;
        // cases 2x plus petites que dans la grande grille 
        pCtx.lineWidth = 0.5;

        for (let i = 0; i <= grille; i++) {
            const pos = i * cellSize;
            // Ligne verticale
            pCtx.moveTo(pos, 0);
            pCtx.lineTo(pos, canvas.height);
            // Ligne horizontale
            pCtx.moveTo(0, pos);
            pCtx.lineTo(canvas.width, pos);
        }
        pCtx.stroke();
    };

    // carreaux visibles avant meme la reception des pixels
    renderGrid();

    const protocole = location.protocol === "https:" ? "wss://" : "ws://";
    const socket = new WebSocket(`${protocole}${location.host}${contextPath}/grid`);

    socket.onmessage = ({ data }) => {
        const payload = JSON.parse(data);
        
        if (payload.type === "init") {
            pCtx.clearRect(0, 0, canvas.width, canvas.height);
            payload.pixels.forEach(({ x, y, color }) => drawPixel(x, y, color));
            renderGrid();
        } else if (payload.type === "pixel") {
            drawPixel(payload.x, payload.y, payload.color);
        } else if (payload.type === "presence") {
            showPresence(payload);
        }
    };

    // "x en ligne" dans la barre de navigation
    const showPresence = ({ online }) => {
        const badge = document.getElementById('online-badge');
        if (!badge) return;
        badge.classList.remove('hidden');
        document.getElementById('online-count').textContent = `${online} en ligne`;
    };
});