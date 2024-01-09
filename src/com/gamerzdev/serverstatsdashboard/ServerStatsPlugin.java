package com.gamerzdev.serverstatsdashboard;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServerStatsPlugin extends JavaPlugin {

    private final String dashboardEndpoint = "http://localhost:3000/updateStats";

    @Override
    public void onEnable() {
        getLogger().info("ServerStatsPlugin is enabled!");
        getServer().getScheduler().runTaskTimer(this, this::updateStats, 0, 20 * 60); // Update elke minuut
    }

    private void updateStats() {
        int onlinePlayers = getServer().getOnlinePlayers().size();
        double[] tps = Bukkit.getServer().getTPS();
        double cpuUsage = // Implementeer de logica om CPU-gebruik te verkrijgen
        // Voeg andere statistieken toe

        // Stuur statistieken naar het dashboard
        sendStatsToDashboard(onlinePlayers, tps[0], cpuUsage);
    }

    private void sendStatsToDashboard(int onlinePlayers, double tps, double cpuUsage) {
        try {
            // Maak een HTTP POST-request naar het dashboard
            URL url = new URL(dashboardEndpoint);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");

            // Bouw de JSON-payload voor het verzoek
            String jsonInputString = String.format("{\"onlinePlayers\": %d, \"tps\": %.2f, \"cpuUsage\": %.2f}", onlinePlayers, tps, cpuUsage);

            // Stuur de statistieken naar het dashboard
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Ontvang het antwoord van het dashboard (je kunt dit gebruiken voor foutafhandeling)
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                getLogger().info("Stats sent successfully to the dashboard.");
            } else {
                getLogger().warning("Failed to send stats to the dashboard. Response code: " + responseCode);
            }

        } catch (Exception e) {
            getLogger().warning("Error while sending stats to the dashboard: " + e.getMessage());
        }
    }
}
