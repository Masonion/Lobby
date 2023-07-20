package org.mason.lobby.commands;

import net.citizensnpcs.Citizens;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import net.citizensnpcs.trait.LookClose;
import net.citizensnpcs.trait.SkinTrait;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.mason.lobby.Data.NPCDataStore;

public class NPCCommand implements CommandExecutor {

    private final Citizens citizens;
    private final NPCDataStore npcDataStore;

    public NPCCommand(Citizens citizens, NPCDataStore npcDataStore) {
        this.citizens = citizens;
        this.npcDataStore = npcDataStore;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        NPCRegistry npcRegistry = CitizensAPI.getNPCRegistry();

        if (args.length == 4 && args[0].equalsIgnoreCase("create")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("Only players can use this command!");
                return true;
            }

            NPC npc = npcRegistry.createNPC(EntityType.PLAYER, args[1]);
            npc.spawn(((Player) sender).getLocation());

            // Store the server name in the NPC's data
            npc.data().set("serverName", args[3]);
            npcDataStore.setServerName(npc.getId(), args[3]);

            SkinTrait skinTrait = npc.getTrait(SkinTrait.class);
            skinTrait.setSkinName(args[2]);

            sender.sendMessage("NPC named " + args[1] + " with skin " + args[2] + " created for server " + args[3]);
            return true;
        }

        sender.sendMessage("Incorrect command syntax. Use: /mnpc create <name> <skin-name> <server-name>");
        return true;
    }
}
