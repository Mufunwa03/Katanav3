package com.yourname.katanas;

import com.yourname.katanas.commands.GiveKatanaCommand;
import com.yourname.katanas.commands.ReloadKatanasCommand;
import com.yourname.katanas.item.KatanaFactory;
import org.bukkit.plugin.java.JavaPlugin;

public final class KatanasPlugin extends JavaPlugin {

    private KatanaFactory katanaFactory;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        this.katanaFactory = new KatanaFactory(this);
        katanaFactory.loadKatanas();

        getCommand("givekatana").setExecutor(new GiveKatanaCommand(katanaFactory));
        getCommand("givekatana").setTabCompleter(new GiveKatanaCommand(katanaFactory));
        getCommand("reloadkatanas").setExecutor(new ReloadKatanasCommand(this));

        getLogger().info("Katanas plugin has been enabled successfully.");
    }

    @Override
    public void onDisable() {
        getLogger().info("Katanas plugin has been disabled.");
    }

    public KatanaFactory getKatanaFactory() {
        return katanaFactory;
    }
}