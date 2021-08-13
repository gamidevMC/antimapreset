package me.gamidevmc.antimapreset.events;

import me.gamidevmc.antimapreset.Antimapreset;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.UUID;


public class MapEvent implements Listener {
    private Antimapreset plugin;

    public MapEvent(Antimapreset pl) {
        this.plugin = pl;
    }


    @EventHandler
    public void onMapClick(PlayerInteractEvent event) {
        if (event.getItem() != null && event.getItem().getType() == Material.EMPTY_MAP && (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR)) {
            Player player = event.getPlayer();
            UUID playerID = player.getUniqueId();
            this.checkTime();
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK && this.plugin.matBlacklist.contains(event.getClickedBlock().getType())) {
                if(this.plugin.getConfig().getBoolean("Messages")){
                    player.sendMessage(ChatColor.DARK_PURPLE + "Sorry! You can't do this with an empty map in your hand!");
                }
                event.setCancelled(true);
            } else if (!this.plugin.playerStats.containsKey(playerID)) {
                this.plugin.playerStats.put(playerID, 1);
            } else {
                int mapLimit = this.plugin.getConfig().getInt("MapLimit");
                int warningCount = this.plugin.getConfig().getInt("WarningCount");
                int playerCounter = (Integer)this.plugin.playerStats.get(playerID);
                if (playerCounter >= mapLimit) {
                    event.setCancelled(true);
                    if(this.plugin.getConfig().getBoolean("Messages")){
                        player.sendMessage(ChatColor.DARK_PURPLE + "AntiMapReset: Exceeded Map Limit!");
                    }
                } else {
                    ++playerCounter;
                    this.plugin.playerStats.put(playerID, playerCounter);
                    this.Warn(player, playerCounter);
                }
            }
        }
    }

    private void Warn(Player player, int playerCounter) {
        if (playerCounter > this.plugin.getConfig().getInt("WarningCount")) {
            if(this.plugin.getConfig().getBoolean("Messages")){
                player.sendMessage(ChatColor.AQUA + "AntiMapReset: Watch out! You can't make more than " + this.plugin.getConfig().getInt("MapLimit") + " maps every " + this.plugin.getConfig().getDouble("ResetTime") + " hours!");
            }
        }

    }

    public boolean checkTime() {
        long checkTimeStamp = System.currentTimeMillis() + this.plugin.resetIntervalMilli;
        if (this.plugin.resetTimeStampMilli < System.currentTimeMillis()) {
            this.plugin.playerStats.clear();
            this.plugin.resetTimeStampMilli = checkTimeStamp;
            return true;
        } else {
            return false;
        }
    }
}
