package client.utils;

import client.Main;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Objects;

/**
 * Collection of File utility methods.
 *
 * @author Jake Nijssen
 */
public final class FileUtils {

    /**
     * Copy's a file from the resource directory to a target {@link File} if the corresponding
     * file does not exist yet.
     *
     * @param target The target file
     * @param resource The name of the resource inside the resources' directory.
     */
    public static void copyFileIfNotExists(@NotNull File target, @NotNull String resource) {
        if (!target.exists()) {
            try (InputStream is = Objects.requireNonNull(Main.class.getClassLoader().getResourceAsStream("config.yml"))) {
                Files.createFile(target.toPath());
                Files.write(target.toPath(), is.readAllBytes(), StandardOpenOption.TRUNCATE_EXISTING);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Gets the current running directory of the application.
     *
     * @return The current running directory of the app.
     */
    public static @NotNull Path getRunningDirectory() {
        return Paths.get("").toAbsolutePath();
    }

}
