package org.simplefs.fs.common;

import org.simplefs.fs.FileInfo;
import org.simplefs.fs.SimpleFileSystem;
import org.simplefs.fs.common.cfg.Config;
import org.simplefs.fs.meta.FileMeta;
import org.simplefs.fs.meta.FileMetaManager;
import org.simplefs.fs.meta.MetaManagers;

public abstract class AbstractSimpleFileSystem implements SimpleFileSystem {

    protected Config config;
    protected FileMetaManager fileMetaManager;

    public AbstractSimpleFileSystem() {
        //decide to use a custom file meta manager or default
        fileMetaManager = MetaManagers.getDefault();
    }

    protected abstract void setConfig(Config config);

    @Override
    public String save(byte[] fileContent) {
        return save(fileContent, null);
    }

    public FileInfo getMeta(String fileId) {
        if(fileId == null){
            return null;
        }
        FileMeta meta = fileMetaManager.get(fileId);
        if(meta != null){
            return new FileInfo(meta.getFileId(),
                    meta.getFileName(),
                    meta.getFileSize(),
                    meta.getCreateTime());
        }
        return null;
    }
}
