package com.naman.framework.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Centralized configuration manager.
 * Reads from config.properties with environment variable override support.
 */
public class ConfigManager {
    private static final Properties props = new Properties();
    private static ConfigManager instance;

    private ConfigManager() {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (is != null) props.load(is);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    public static synchronized ConfigManager getInstance() {
        if (instance == null) instance = new ConfigManager();
        return instance;
    }

    public String get(String key) {
        String envVal = System.getenv(key.toUpperCase().replace(".", "_"));
        return envVal != null ? envVal : props.getProperty(key, "");
    }

    public String getBaseUrl()      { return get("api.base.url"); }
    public int    getTimeout()      { return Integer.parseInt(get("api.timeout")); }
    public String getAuthToken()    { return get("api.auth.token"); }
    public String getEnvironment()  { return get("environment"); }
}
