package org.mason.lobby.commands;

import net.citizensnpcs.Citizens;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.mason.lobby.Data.NPCDataStore;

public class DeleteNPCCommand implements CommandExecutor {

    private final Citizens citizens;
    private final NPCDataStore npcDataStore;

    public DeleteNPCCommand(Citizens citizens, NPCDataStore npcDataStore) {
        this.citizens = citizens;
        this.npcDataStore = npcDataStore;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "Incorrect command syntax. Use: /deleteNPC <npc-id>");
            return true;
        }

        NPCRegistry npcRegistry = citizens.getNPCRegistry();
        NPC npc = npcRegistry.getById(Integer.parseInt(args[0]));

        if (npc != null) {
            npc.destroy();

            // Remove the NPC data from the store
            npcDataStore.removeNPC(Integer.parseInt(args[0]));

            sender.sendMessage(ChatColor.GREEN + "NPC with ID " + args[0] + " has been successfully deleted.");
        } else {
            sender.sendMessage(ChatColor.RED + "NPC with ID " + args[0] + " not found.");
        }

        return true;
    }
}