package client;

import client.utils.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConfigTest {

    @Test
    public void testConfig() {
        Config config = Config.get();
        assertEquals(config.getHost(), "http://localhost:8080/");
        config.setHost("test");
        config.setCurrentLocale("English");
        Set<String> ids = new HashSet<>();
        ids.add("1");
        config.addPastID("1");
        config.addPastID("2");
        config.removePastID("2");

        config.save();
        config = Config.reload();

        assertEquals(config.getHost(), "test");
        assertEquals(config.getCurrentLocaleName(), "English");
        assertEquals(config.getPastIDs(), ids);
        config.setHost("http://localhost:8080/");
        config.save();
    }

    @Test
    public void supportedLocaleTest() {
        Config.SupportedLocale sl = new Config.SupportedLocale("English", "en");
        assertEquals("English", sl.getName());
        assertEquals("en", sl.getCode());

        sl.setName("Nederlands");
        sl.setCode("nl");

        assertEquals("Nederlands", sl.getName());
        assertEquals("nl", sl.getCode());
    }

    @AfterAll
    public static void deleteConfig() {
        File configFile = new File(FileUtils.getRunningDirectory().toFile(), "config.yml");
        if (!configFile.delete()) {
            System.out.println("Failed to clean up config file from test.");
        }
    }

}
