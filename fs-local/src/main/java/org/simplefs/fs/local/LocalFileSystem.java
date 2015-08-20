package org.simplefs.fs.local;

import org.simplefs.fs.common.AbstractSimpleFileSystem;
import org.simplefs.fs.common.FileSystems;
import org.simplefs.fs.common.cfg.Config;
import org.simplefs.fs.meta.FileMeta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * File System that use local disk as a back store.
 */
public class LocalFileSystem extends AbstractSimpleFileSystem {
    private static final Logger LOG = LoggerFactory.getLogger(LocalFileSystem.class);

    private String baseDir;

    public LocalFileSystem() {
    }

    public String getBaseDir() {
        return baseDir;
    }

    @Override
    protected void setConfig(Config config) {
        baseDir = config.get("base_dir");
    }

    public String getFullPath(FileMeta fileMeta){
        StringBuilder s = new StringBuilder();
        s.append(baseDir);
        if(!baseDir.endsWith("/")){
            s.append(File.separator);
        }
        s.append(fileMeta.getInnerFileId());
        return s.toString();
    }

    public String save(byte[] fileContent, String fileName) {

        final String fileId = FileSystems.createFileId();

        FileMeta meta = new FileMeta(fileId, fileName, fileContent.length, System.currentTimeMillis(), fileId);
        fileMetaManager.save(meta);

        String fullPath = getFullPath(meta);
        try(FileOutputStream out = new FileOutputStream(fullPath)){
            out.write(fileContent);
            out.flush();
        }catch (IOException e){
            LOG.error("error save file " + fullPath, e);
            fileMetaManager.delete(fileId);
            return null;
        }
        return meta.getFileId();
    }

    public boolean delete(String fileId) {
        FileMeta meta = fileMetaManager.get(fileId);
        if(meta == null){
            return false;
        }
        String fullPath = getFullPath(meta);
        LOG.debug("deleting file {}", fullPath);

        File f = new File(fullPath);
        boolean fileDeleted = f.delete();

        boolean metaDeleted = false;
        if(fileDeleted){
            metaDeleted = fileMetaManager.delete(fileId);
        }
        return  fileDeleted && metaDeleted;
    }

    public byte[] get(String fileId) {
        FileMeta meta = fileMetaManager.get(fileId);
        if(meta == null){
            return null;
        }
        String fullPath = getFullPath(meta);
        ByteArrayOutputStream out = new ByteArrayOutputStream((int)meta.getFileSize());

        byte[] buffer = new byte[1024];
        try(FileInputStream in = new FileInputStream(fullPath)){
            int c;
            while((c = in.read(buffer)) != -1){
                out.write(buffer, 0, c);
            }
        } catch (IOException e){
            LOG.error("error getting file " +  fullPath);
            return null;
        }

        return out.toByteArray();
    }
}
