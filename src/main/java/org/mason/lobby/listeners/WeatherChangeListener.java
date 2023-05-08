package org.mason.lobby.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

public class WeatherChangeListener implements Listener {

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
        if (event.toWeatherState()) { // If the weather is changing to raining or snowing
            event.setCancelled(true); // Cancel the event to keep the weather clear (sunny)
        }
    }
}