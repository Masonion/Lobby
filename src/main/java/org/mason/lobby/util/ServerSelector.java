package org.mason.lobby.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.mason.lobby.util.Bungee;

import java.util.ArrayList;
import java.util.List;

public class ServerSelector implements Listener {

    private final Bungee bungee;
    private final String arenaPermission = "lobby.arena.join"; // Define the permission for joining the arena
    private final String newUHCPermission = "lobby.newuhc.join"; // Define the permission for joining the arena

    public ServerSelector(Bungee bungee) {
        this.bungee = bungee;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        ItemStack item = event.getItem();
        if (item != null && item.getType() == Material.WATCH) {
            openServerSelector(event.getPlayer());
        }
    }

    private void openServerSelector(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 9, ChatColor.AQUA + "Server Selector");

        ItemStack uhc = createServerItem(Material.GOLDEN_APPLE, ChatColor.AQUA + "1.8 UHC", "UHC");
        ItemStack arena;
        arena = createServerItem(Material.DIAMOND_SWORD, ChatColor.AQUA + "Arena", "Arena");

        ItemStack UHC_NEW;
        UHC_NEW = createServerItem(Material.GOLDEN_APPLE, ChatColor.AQUA + "1.20 UHC", "UHC_NEW");

        inventory.setItem(3, uhc);
        inventory.setItem(4, UHC_NEW);
        inventory.setItem(5, arena);

        player.openInventory(inventory);
    }


    private ItemStack createServerItem(Material material, String name, String server) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        List<String> lore = new ArrayList<>();
        // Assuming you have bungee instance accessible here
        Integer playerCount = bungee.getServerPlayerCount(server);
        lore.add(ChatColor.GRAY + "Online: " + ChatColor.WHITE + playerCount);
        meta.setLore(lore);

        item.setItemMeta(meta);
        return item;
    }

    private ItemStack createItem(Material material, String name) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.RED + "Server is under construction");
        meta.setLore(lore);

        item.setItemMeta(meta);
        return item;
    }


    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals(ChatColor.AQUA + "Server Selector")) {
            event.setCancelled(true);

            ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem == null || clickedItem.getType() == Material.AIR) {
                return;
            }

            Player player = (Player) event.getWhoClicked();
            String serverName = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName());

            if (serverName.equals("Arena") && !player.hasPermission(arenaPermission)) {
                player.sendMessage(ChatColor.RED + "You do not have permission to join the Arena.");
                return;
            }

            if (serverName.equals("1.8 UHC")) {
                serverName = "UHC";
            }

            if (serverName.equals("1.20 UHC")) {
                if (!player.hasPermission(newUHCPermission)) {
                    player.sendMessage(ChatColor.RED + "You do not have permission to join the 1.20 UHC.");
                    return;
                }
                serverName = "UHC_NEW";
            }

            bungee.sendPlayerToServer(player, serverName);
        }
    }
}
