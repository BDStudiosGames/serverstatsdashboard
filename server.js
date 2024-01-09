const express = require('express');
const bodyParser = require('body-parser');

const app = express();
const port = 3000;

app.use(bodyParser.json());

let serverStats = {
    onlinePlayers: 0,
    tps: 0.0,
    cpuUsage: 0.0,
};

app.get('/stats', (req, res) => {
    res.json(serverStats);
});

app.post('/updateStats', (req, res) => {
    // Hier verwerk je de ontvangen statistieken van de Bukkit-plugin
    const { onlinePlayers, tps, cpuUsage } = req.body;
    serverStats = { onlinePlayers, tps, cpuUsage };
    
    // Je kunt hier extra logica toevoegen, bijvoorbeeld het opslaan van de statistieken in een database.

    res.send('Stats received successfully');
});

app.listen(port, () => {
    console.log(`Server listening at http://localhost:${port}`);
});
