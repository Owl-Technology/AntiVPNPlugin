package dev.aswitch.antivpnplugin.api.utils;

import com.google.common.io.Files;
import dev.aswitch.antivpnplugin.bungee.AntiVPNBungee;
import lombok.Getter;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.*;
import java.nio.charset.StandardCharsets;

@Getter
public class ConfigUtil {

    private File configFile;
    private final File configPath;
    private Configuration configuration;

    private final String configName;

    public ConfigUtil(String configName, File configPath) {
        this.configName = configName;
        this.configPath = configPath;
    }

    public void loadConfig() {
        configFile = new File(getConfigPath(),  getConfigName());
        try {
            if (!configFile.exists()) {
                configFile.createNewFile();
            }
            configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile, configuration);
        } catch ( Exception exception) {
            exception.printStackTrace();
        }
    }

    public void save() {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(configuration, configFile);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

}
