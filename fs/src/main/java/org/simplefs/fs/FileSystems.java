package org.simplefs.fs;

import org.simplefs.fs.cfg.Config;
import org.simplefs.fs.cfg.FastDFSConfig;
import org.simplefs.fs.cfg.LocalFSConfig;

import java.util.UUID;

public class FileSystems {

    private FileSystems() {
    }

    /**
     * Get simple file system of all kinds.
     * @param fsConfig config of system you want , not null.
     * @return
     */
    public static SimpleFileSystem get(Config fsConfig){

        if(fsConfig == null){
            throw new IllegalArgumentException("null fs config.");
        }

        SimpleFileSystem fs;
        if(fsConfig instanceof LocalFSConfig){
            fs = createByClass("org.simplefs.fs.local.LocalFileSystem", fsConfig);
        }else if(fsConfig instanceof FastDFSConfig){
            fs = createByClass("org.simplefs.fs.fdfs.FastDfsFileSystem", fsConfig);
        }else{
            throw new IllegalStateException("unsupported fs type");
        }

        return fs;
    }

    public static SimpleFileSystem createByClass(String className, Config config){
        Class<?> fileSystemClass;
        try {
            fileSystemClass = Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }

        SimpleFileSystem simleFs = null;
        try{
            simleFs =  (SimpleFileSystem)fileSystemClass.newInstance();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

        if(simleFs instanceof AbstractSimpleFileSystem){
            ((AbstractSimpleFileSystem)simleFs).setConfig(config);
        }else{
            System.out.println("no config set.");
        }

        return simleFs;
    }

    public static String createFileId(){
        return UUID.randomUUID().toString();
    }
}
