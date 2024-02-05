package org.mason.lobby.util;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SettingsSelector implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        ItemStack item = event.getItem();
        if (item != null && item.getType() == Material.BOOK && item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();
            if (meta.hasDisplayName() && meta.getDisplayName().equals(ChatColor.YELLOW + "Settings")) {
                event.getPlayer().performCommand("settings");
            }
        }
    }

    public static ItemStack getSettingsItem() {
        ItemStack settings = new ItemStack(Material.BOOK);
        ItemMeta settingsMeta = settings.getItemMeta();
        settingsMeta.setDisplayName(ChatColor.YELLOW + "Settings");
        settings.setItemMeta(settingsMeta);

        return settings;
    }
}