package me.gamidevmc.antimapreset;

import me.gamidevmc.antimapreset.commands.ClearCountCommand;
import me.gamidevmc.antimapreset.events.MapEvent;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public final class Antimapreset extends JavaPlugin {

    private FileConfiguration config;
    public Set matBlacklist = new HashSet();
    public HashMap playerStats = new HashMap();
    public long resetTimeStampMilli;
    public long resetIntervalMilli;

    public void onEnable() {
        this.initializeConfiguration();
        this.createTimeStamp();
        this.createBlackList();
        this.registerEvents();
        this.getCommand("amrclear").setExecutor(new ClearCountCommand(this));
    }

    private void initializeConfiguration() {
        this.config = this.getConfig();
        this.config.options().copyDefaults(true);
        this.saveDefaultConfig();
    }

    private void registerEvents() {
        PluginManager pm = this.getServer().getPluginManager();
        pm.registerEvents(new MapEvent(this), this);
    }

    private void createTimeStamp() {
        this.resetIntervalMilli = (long)(this.config.getDouble("ResetTime") * 3600000.0D);
        this.resetTimeStampMilli = System.currentTimeMillis() + this.resetIntervalMilli;
    }

    private void createBlackList() {
        Iterator var1 = this.config.getStringList("BlackList").iterator();

        while(var1.hasNext()) {
            String materialString = (String)var1.next();

            try {
                this.matBlacklist.add(Material.valueOf(materialString));
            } catch (Exception var4) {
                System.out.println("ERROR! " + materialString + " is not a valid Material. Check Bukkit API");
            }
        }

    }

}
