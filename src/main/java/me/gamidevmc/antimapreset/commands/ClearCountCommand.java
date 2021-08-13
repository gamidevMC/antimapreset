package me.gamidevmc.antimapreset.commands;

import me.gamidevmc.antimapreset.Antimapreset;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ClearCountCommand implements CommandExecutor {
    private Antimapreset plugin;

    public ClearCountCommand(Antimapreset pl) {
        this.plugin = pl;
    }

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        this.plugin.playerStats.clear();
        return true;
    }
}
