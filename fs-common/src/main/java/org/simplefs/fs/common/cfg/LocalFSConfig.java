package org.simplefs.fs.common.cfg;

public class LocalFSConfig extends Config{

    public LocalFSConfig() {
        setBaseDir("/tmp/simlefs");
    }

    public void setBaseDir(String baseDir){
        set("base_dir", baseDir);
    }
}
