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

import java.util.ArrayList;
import java.util.List;

public class ServerSelector implements Listener {

    private final Bungee bungee;
    private final String arenaPermission = "lobby.arena.join";
    private final UpcomingMatchUtil upcomingMatchUtil;

    public ServerSelector(Bungee bungee, UpcomingMatchUtil upcomingMatchUtil) {
        this.bungee = bungee;
        this.upcomingMatchUtil = upcomingMatchUtil;
    }

    public static ItemStack getServerSelectorItem() {
        ItemStack clock = new ItemStack(Material.WATCH);
        ItemMeta clockMeta = clock.getItemMeta();
        clockMeta.setDisplayName(ChatColor.GREEN + "Server Selector");
        clock.setItemMeta(clockMeta);

        return clock;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        ItemStack item = event.getItem();
        if (item != null && item.getType() == Material.WATCH && item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();
            if (meta.hasDisplayName() && meta.getDisplayName().equals(ChatColor.GREEN + "Server Selector")) {
                openServerSelector(event.getPlayer());
            }
        }
    }

    private void openServerSelector(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 9, ChatColor.AQUA + "Server Selector");

        ItemStack uhc = createServerItem(Material.GOLDEN_APPLE, ChatColor.AQUA + "1.8 UHC", "UHC", player);
        ItemStack arena;
        arena = createServerItem(Material.DIAMOND_SWORD, ChatColor.AQUA + "Arena", "Arena", player);

        inventory.setItem(3, uhc);
        inventory.setItem(5, arena);

        player.openInventory(inventory);
    }


    private ItemStack createServerItem(Material material, String name, String server, Player player) {
        ItemStack item;
        ItemMeta meta;
        String currentMatches = upcomingMatchUtil.checkUpcomingGamesAndPrint();
        List<String> lore = new ArrayList<>();
        Integer playerCount = bungee.getServerPlayerCount(server);

        if (server.equals("Arena")) {
            item = new ItemStack(material);
            meta = item.getItemMeta();
            lore.add(ChatColor.GRAY + "Online: " + ChatColor.WHITE + playerCount);
        } else if (server.equals("UHC")) {
            item = new ItemStack(material);
            meta = item.getItemMeta();
            lore.add(ChatColor.GRAY + "Online: " + ChatColor.WHITE + playerCount);
            lore.add("");
            lore.add(ChatColor.BLUE.toString() + ChatColor.BOLD + "Upcoming Game:");
            if (currentMatches == null || currentMatches.isEmpty()) {
                lore.add(ChatColor.RED + "No upcoming matches at the moment.");
            } else {
                String[] matchArray = currentMatches.split("\n");
                for (String match : matchArray) {
                    lore.add(ChatColor.WHITE + match);
                }
            }
        }

        else {
            item = new ItemStack(material);
            meta = item.getItemMeta();
        }

        meta.setDisplayName(name);
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack createItem(Material material, String name) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        List<String> lore = new ArrayList<>();
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


            if (serverName.equals("1.8 UHC")) {
                serverName = "UHC";
            }

            bungee.sendPlayerToServer(player, serverName);
        }
    }
}
