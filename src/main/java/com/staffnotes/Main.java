package com.staffnotes;
import com.staffnotes.Listeners.ChatListener;
import com.staffnotes.classes.NotesDatabase;
import com.staffnotes.classes.TabComplete;
import com.staffnotes.commands.CmdExecuter;
import com.staffnotes.commands.Cmds;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.SQLException;

public final class Main extends JavaPlugin {
    private FileConfiguration config;
    private final NotesDatabase notesDatabase = new NotesDatabase(this);
    private ChatListener chatListener = new ChatListener(this);
    public NotesDatabase getNotesDatabase(){
        return this.notesDatabase;
    }
    public Cmds getCommands(){
        return this.cmds;
    }
    public FileConfiguration getConfig(){
        return config;
    }

    private Cmds cmds;
    public ChatListener getChatListener() {
        return chatListener;
    }

    @Override
    public void onEnable() {
        getLogger().info("[StaffNotes] plugin is being enabled...");
        reloadConfig();
        cmds = new Cmds(this);
        // Register command
        File dataFolder = getDataFolder();
        if (!dataFolder.exists()) {
            boolean success = dataFolder.mkdirs(); // Create the directory and any necessary parent directories
            if (success) {
                getLogger().info("[StaffNotes]Directory created successfully: " + dataFolder.getAbsolutePath());
            } else {
                getLogger().warning("[StaffNotes]Failed to create directory: " + dataFolder.getAbsolutePath());
            }
        } else {
            getLogger().info("[StaffNotes]Directory already exists: " + dataFolder.getAbsolutePath());
        }
        this.getCommand("notes").setExecutor(new CmdExecuter(this));
        this.getCommand("notes").setTabCompleter(new TabComplete(this));
        Bukkit.getPluginManager().registerEvents(chatListener, this);
        try {
            notesDatabase.connect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        getLogger().info("[StaffNotes] plugin has been enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("[StaffNotes] plugin is being disabled...");
        try {
            notesDatabase.disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        getLogger().info("[StaffNotes] plugin has been disabled.");
    }


    public void reloadConfig() {
        File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            saveDefaultConfig();
        }
        config = YamlConfiguration.loadConfiguration(configFile);
    }
}