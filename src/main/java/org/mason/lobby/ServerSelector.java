package org.mason.lobby;

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

import java.util.ArrayList;
import java.util.List;

public class ServerSelector implements Listener {

    private final Bungee bungee;

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
        Inventory inventory = Bukkit.createInventory(null, 9, ChatColor.GOLD + "Server Selector");

        Integer uhcPlayerCount = bungee.getServers().get("UHC");
        Integer arenaPVPPlayerCount = bungee.getServers().get("Arena");

        ItemStack uhc = createItem(Material.GOLDEN_APPLE, ChatColor.GOLD + "UHC", uhcPlayerCount != null ? uhcPlayerCount : 0);
        ItemStack arena = createItem(Material.DIAMOND_SWORD, ChatColor.GOLD + "Arena", arenaPVPPlayerCount != null ? arenaPVPPlayerCount : 0);

        inventory.setItem(0, uhc);
        inventory.setItem(1, arena);

        player.openInventory(inventory);
    }


    private ItemStack createItem(Material material, String name, int playerCount) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + name);

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Players: " + ChatColor.AQUA + playerCount);
        meta.setLore(lore);

        item.setItemMeta(meta);
        return item;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals(ChatColor.GOLD + "Server Selector")) {
            event.setCancelled(true);

            ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem == null || clickedItem.getType() == Material.AIR) {
                return;
            }

            Player player = (Player) event.getWhoClicked();
            String serverName = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName());

            bungee.sendPlayerToServer(player, serverName);
        }
    }
}
