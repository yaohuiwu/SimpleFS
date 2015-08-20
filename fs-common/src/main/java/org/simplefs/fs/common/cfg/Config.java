package org.simplefs.fs.common.cfg;

import java.util.Properties;

public class Config {

    private String name;

    private Properties prop;

    public Config() {
        prop = new Properties();
    }

    public void set(String property, String value){
        prop.setProperty(property, value);
    }

    public String get(String property){
        return prop.getProperty(property);
    }

    public String get(String property, String defaultValue){
        return prop.getProperty(property, defaultValue);
    }

    public Integer getInteger(String key){
        return Integer.valueOf(prop.getProperty(key));
    }

    public Integer getInteger(String key, String defaultValue){
        return Integer.valueOf(prop.getProperty(key, defaultValue));
    }

    //TODO add utilities methods loading config from classpath properties file.
}
