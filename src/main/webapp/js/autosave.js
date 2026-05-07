document.addEventListener('DOMContentLoaded', () => {
    if (typeof isConnected !== 'undefined' && isConnected) {
        
        setInterval(() => {
            fetch('save', { method: 'POST' })
                .then(res => {
                    if(res.ok) console.log("sauvegardes auto effectuer");
                })
                .catch(err => console.error("erreur sauvegarde:", err));
        }, 30000); //30s
        
    }
});