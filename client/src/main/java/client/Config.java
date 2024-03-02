package client;

import client.utils.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.NodeKey;
import org.spongepowered.configurate.objectmapping.meta.Required;
import org.spongepowered.configurate.objectmapping.meta.Setting;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.File;
import java.util.Collections;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

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

    @Required
    @Setting("current-locale")
    private String currentLocale;

    @Required
    @Setting("supported-locales")
    private Set<SupportedLocale> supportedLocales;

    /**
     * Get the host url of the backend server.
     * **NOTE** This value may be changed during runtime, so do not store it separately.
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
     * Get the current locale for the UI.
     * **NOTE** This value may be changed during runtime, so do not store it separately.
     *
     * @return The locale of the frontend.
     */
    public @NotNull String getCurrentLocaleName() {
        return currentLocale;
    }

    /**
     * Gets the current {@link Locale} of the current configured language.
     *
     * @return The current {@link Locale} of the current language.
     * @throws IllegalStateException If the current locale is not supported by the config.
     */
    public @NotNull Locale getCurrentLocale() throws IllegalStateException {
        SupportedLocale supportedLocale = null;
        for (SupportedLocale locale : supportedLocales) {
            if (getCurrentLocaleName().equalsIgnoreCase(locale.getName())) {
                supportedLocale = locale;
                break;
            }
        }

        if (supportedLocale == null)
            throw new IllegalStateException("Illegal config state, current locale must be supported");

        return Locale.of(supportedLocale.getCode());
    }

    /**
     * Set the locale of the frontend. Must match a representative name inside the supportedLanguages list.
     * **NOTE** Execute the {@link Config#save()} method after all the mutations are completed.
     *
     * @param currentLocale The new locale of the frontend.
     * @throws IllegalArgumentException If the specified locale is not supported.
     */
    public void setCurrentLocale(@NotNull String currentLocale) throws IllegalArgumentException {
        boolean supported = false;
        for (SupportedLocale supportedLocale : supportedLocales) {
            if (supportedLocale.getName().equalsIgnoreCase(currentLocale)) {
                supported = true;
                break;
            }
        }
        if (!supported) throw new IllegalArgumentException("Locale " + currentLocale + " is not supported!");

        this.currentLocale = currentLocale;
    }

    /**
     * Returns a read only view of the all the {@link SupportedLocale}'s.
     *
     * @return Read only view of the supported locales.
     */
    public @UnmodifiableView @NotNull Set<SupportedLocale> getSupportedLocales() {
        return Collections.unmodifiableSet(supportedLocales);
    }

    /**
     * Adds a supported locale to the system.
     *
     * @param supportedLocale The newly supported locale.
     */
    public void addSupportedLocale(@NotNull SupportedLocale supportedLocale) {
        supportedLocales.add(supportedLocale);
    }

    /**
     * Removes a supported locale from the system.
     *
     * @param supportedLocale The no longer supported locale.
     */
    public void removeSupportedLocale(@NotNull SupportedLocale supportedLocale) {
        supportedLocales.remove(supportedLocale);
    }

    /**
     * Represents a support locale in the frontend.
     */
    @ConfigSerializable
    public static class SupportedLocale {

        @NodeKey
        @Required
        @Setting("name")
        private String name;

        @Required
        private String code;

        /**
         * For deserialization purposes only, do not use!
         */
        private SupportedLocale(){}

        /**
         * Construct a new supported locale for the frontend.
         *
         * @param name The name of the locale, i.e. "English" for English.
         * @param code The code of the locale, i.e. "en" for English. Must be supported by the {@link java.util.Locale}
         *             class.
         */
        public SupportedLocale(@NotNull String name, @NotNull String code) {
            this.name = name;
            this.code = code;
        }

        /**
         * Get the representation name of the locale. For example "English" for English.
         * **NOTE** This value may be changed during runtime, so do not store it separately.
         *
         * @return The representative name of the locale.
         */
        public @NotNull String getName() {
            return name;
        }

        /**
         * Sets the representative name of the locale. For example "English" for English. Must be unique.
         * **NOTE** Execute the {@link Config#save()} method after all the mutations are completed.
         *
         * @param name The new representative name of the locale.
         */
        public void setName(@NotNull String name) {
            this.name = name;
        }

        /**
         * Gets the current locale code for this locale, this will allow a reference to the {@link java.util.Locale}
         * class. For example "en" for English.
         * **NOTE** This value may be changed during runtime, so do not store it separately.
         *
         * @return The locale code.
         */
        public @NotNull String getCode() {
            return code;
        }

        /**
         * Sets the new locale code for this locale, will be used to reference language definition files.
         * **NOTE** Execute the {@link Config#save()} method after all the mutations are completed.
         *
         * @param code The new code for the locale, must be supported by {@link java.util.Locale}.
         */
        public void setCode(@NotNull String code) {
            this.code = code;
        }
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
