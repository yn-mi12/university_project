package client;

import client.utils.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConfigTest {

    @Test
    public void testConfig() {
        Config config = Config.get();
        assertEquals(config.getHost(), "http://localhost:8080/");
        config.setHost("test");
        assertEquals(config.getCurrentLocale(), "English");
        config.save();
        config = Config.reload();
        assertEquals(config.getHost(), "test");
        config.setHost("http://localhost:8080/");
        config.save();
    }

    @AfterAll
    public static void deleteConfig() {
        File configFile = new File(FileUtils.getRunningDirectory().toFile(), "config.yml");
        if (!configFile.delete()) {
            System.out.println("Failed to clean up config file from test.");
        }
    }

}
