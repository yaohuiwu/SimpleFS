package org.simplefs.fs.meta;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.Properties;

public class MetaManagers {

    private static final Logger LOG = LoggerFactory.getLogger(MetaManagers.class);

    public static FileMetaManager getDefault(){
        URL cfgFile = MetaManagers.class.getResource("/redis.properties");
        Properties prop = new Properties();
        if(cfgFile != null){
            LOG.debug("find redis config file:{}", cfgFile.getFile());
            try(FileInputStream in = new FileInputStream(cfgFile.getFile())){
                prop.load(in);
            }catch (IOException e){
                LOG.error("error reading {}", cfgFile.getFile());
            }
        }
        return createByClass("org.simplefs.fs.meta.redis.RedisFileMetaManager", prop);
    }

    public static FileMetaManager createByClass(String className, Properties prop){
        Class<?> metaMgrClass;
        try {
            metaMgrClass = Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }

        FileMetaManager metaManager;
        try{
            Constructor<?> constructor = metaMgrClass.getConstructor(Properties.class);
            if(constructor != null){
                metaManager = (FileMetaManager)constructor.newInstance(prop);
            }else{
                metaManager = (FileMetaManager)metaMgrClass.newInstance();
            }
        }catch (Exception e){
            LOG.error("error creating insance of class {}", metaMgrClass);
            return null;
        }
        return metaManager;
    }
}
