document.addEventListener('DOMContentLoaded', () => {
    const canvas = document.getElementById('gridCanvas');
    const pCtx = canvas.getContext('2d');
    
    const grille = 50; 
    const cellSize = canvas.width / grille; 

    const drawPixel = (x, y, color) => {
        pCtx.fillStyle = color;
        pCtx.fillRect(x * cellSize, y * cellSize, cellSize, cellSize);
        //bordure pixl
        pCtx.strokeStyle = '#000000';
        pCtx.strokeRect(x * cellSize, y * cellSize, cellSize, cellSize);
    };

    //creation de la grille noir
    const renderGrid = () => {
        pCtx.beginPath();
        pCtx.strokeStyle = "#000000";
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
        }
    };
});