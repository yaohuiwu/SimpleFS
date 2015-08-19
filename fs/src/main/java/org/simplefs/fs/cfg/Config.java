package org.simplefs.fs.cfg;

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
}
