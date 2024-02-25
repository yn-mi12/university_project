package client;

import client.utils.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Required;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.File;
import java.util.Objects;

/**
 * Config class that contains runtime changeable settings that affects how
 * the application operates.
 *
 * @author Jake Nijssen
 */
@ConfigSerializable
public final class Config {

    private static CommentedConfigurationNode node;
    private static Config instance;

    static {
        reload();
    }

    @Required
    private String host;

    /**
     * Get the host url of the backend server.
     * **NOTE** This value may be changed during runtime, so do not store is separately.
     *
     * @return The host of the backend server.
     */
    public @NotNull String getHost() {
        return host;
    }

    /**
     * Set the host url of the backend server.
     * **NOTE** Execute the {@link Config#save()} method after all the mutations are completed.
     *
     * @param host The new host url of the backend.
     */
    public void setHost(@NotNull String host) {
        this.host = host;
    }

    /**
     * Gets the current instance of the config object.
     *
     * @return The current config instance.
     */
    public static @NotNull Config get() {
        return instance;
    }

    /**
     * Saves the current state of the configuration object, and stores it to disk.
     */
    public void save() {
        File configFile = new File(FileUtils.getRunningDirectory().toFile(), "config.yml");
        YamlConfigurationLoader configLoader = YamlConfigurationLoader.builder()
                .file(configFile).build();

        try {
            node.set(Config.class, this);
            configLoader.save(node);
        } catch (ConfigurateException e) {
            System.out.println("Failed to save the configuration file");
            throw new RuntimeException(e);
        }
    }

    /**
     * Reloads the config from the disk, and returns the new instance.
     *
     * @return The new config instance
     */
    public static @NotNull Config reload() {
        File configFile = new File(FileUtils.getRunningDirectory().toFile(), "config.yml");
        FileUtils.copyFileIfNotExists(configFile, "config.yml");
        YamlConfigurationLoader configLoader = YamlConfigurationLoader.builder()
                        .file(configFile).build();
        try {
            node = configLoader.load();
            instance = node.get(Config.class);
        } catch (ConfigurateException e) {
            System.out.println("Failed to load the configuration file");
            throw new RuntimeException(e);
        }

        return Objects.requireNonNull(instance);
    }

}
