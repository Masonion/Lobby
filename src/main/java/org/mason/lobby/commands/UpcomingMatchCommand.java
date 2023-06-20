package org.mason.lobby.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.mason.lobby.util.UpcomingMatchUtil;

public class UpcomingMatchCommand implements CommandExecutor {
    private UpcomingMatchUtil upcomingMatchUtil;

    public UpcomingMatchCommand(UpcomingMatchUtil upcomingMatchUtil) {
        this.upcomingMatchUtil = upcomingMatchUtil;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            String message = upcomingMatchUtil.checkUpcomingGamesAndPrint();
            if (message != null) {
                player.sendMessage(message);
            } else {
                player.sendMessage("No upcoming match found!");
            }
        } else {
            sender.sendMessage("This command can only be used by a player!");
        }
        return true;
    }
}