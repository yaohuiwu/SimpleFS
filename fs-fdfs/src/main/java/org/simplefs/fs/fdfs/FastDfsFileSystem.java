package org.simplefs.fs.fdfs;

import org.apache.commons.lang3.StringUtils;
import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.StorageClient1;
import org.simplefs.fs.common.AbstractSimpleFileSystem;
import org.simplefs.fs.common.FileSystems;
import org.simplefs.fs.common.cfg.Config;
import org.simplefs.fs.fdfs.connection.TrackerServerPool;
import org.simplefs.fs.meta.FileMeta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * A fast dfs file system implementation.
 */
public class FastDfsFileSystem extends AbstractSimpleFileSystem {

    private static final Logger LOG = LoggerFactory.getLogger(FastDfsFileSystem.class);

    private TrackerServerPool pool;

    @Override
    protected void setConfig(Config config) {
        pool = new TrackerServerPool(config);
    }

    @Override
    public String save(byte[] fileContent, String fileName) {
        final String fileId = FileSystems.createFileId();
        final byte[] data = fileContent;

        String extName = null;
        if(StringUtils.isNotEmpty(fileName)){
            int dotIndex = fileName.lastIndexOf(".");
            if(dotIndex != -1 && dotIndex < fileName.length()-1){
                extName = fileName.substring(dotIndex + 1);
            }
        }
        final String f_ext_name = extName;

        final NameValuePair[] metaInfo = new NameValuePair[1];
        metaInfo[0] = new NameValuePair("file_name", fileName);

        String innerFileId = pool.use(new TrackerServerPool.StorageCallBack<String>() {
            @Override
            public String use(StorageClient1 client) {
                try {
                    return  client.upload_file1(data, f_ext_name, metaInfo);
                } catch (IOException | MyException e) {
                    LOG.error(e.getMessage(), e);
                    return null;
                }
            }
        });

        if(innerFileId != null){
            FileMeta meta = new FileMeta(fileId, fileName, fileContent.length, System.currentTimeMillis(), innerFileId);
            fileMetaManager.save(meta);
        }
        return fileId;
    }

    @Override
    public boolean delete(String fileId) {

        if(StringUtils.isEmpty(fileId)){
            return false;
        }
        final FileMeta meta = fileMetaManager.get(fileId);
        if(meta == null){
            LOG.info("no meta found for {}, just return.", fileId);
            return false;
        }

        boolean success = pool.use(new TrackerServerPool.StorageCallBack<Boolean>() {
            @Override
            public Boolean use(StorageClient1 client) {
                try {
                    return  client.delete_file1(meta.getInnerFileId()) == 0;
                } catch (IOException | MyException e) {
                    LOG.error(e.getMessage(), e);
                    return false;
                }
            }
        });
        LOG.debug("delete file {}, is success ?{}", fileId, success);

        return success && fileMetaManager.delete(fileId);
    }

    @Override
    public byte[] get(final String fileId) {
        if(StringUtils.isEmpty(fileId)){
            return null;
        }
        final FileMeta meta = fileMetaManager.get(fileId);
        if(meta == null){
            LOG.info("no meta found for {}, just return.", fileId);
            return null;
        }

        return pool.use(new TrackerServerPool.StorageCallBack<byte[]>() {
            @Override
            public byte[] use(StorageClient1 storageClient) {
                try {
                    return storageClient.download_file1(meta.getInnerFileId());
                } catch (IOException|MyException e) {
                    LOG.error("error getting data of file " + fileId, e);
                    return null;
                }
            }
        });
    }

}
