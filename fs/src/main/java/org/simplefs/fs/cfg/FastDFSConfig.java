package org.simplefs.fs.cfg;

public class FastDFSConfig extends Config{

    public void setClientConfig(String clientConfigFile){
        set("client_config_file", clientConfigFile);
    }
}
