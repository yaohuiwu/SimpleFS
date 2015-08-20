package org.simplefs.fs.fdfs.connection;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.simplefs.fs.common.cfg.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;

public class TrackerServerPool implements Closeable{

    private static final Logger LOG = LoggerFactory.getLogger(TrackerServerPool.class);

    final private GenericObjectPool<TrackerServer> pool;
    final private TrackerClient trackerClient;

    public TrackerServerPool(String configPath) {
        try {
            ClientGlobal.init(configPath);
        } catch (IOException|MyException e) {
            LOG.error(e.getMessage(), e);
        }

        LOG.debug("network_timeout=" + ClientGlobal.g_network_timeout + "ms");
        LOG.debug("charset=" + ClientGlobal.g_charset);

        trackerClient = new TrackerClient();

        pool = new GenericObjectPool<>(new TrackerServerObjectFactory(trackerClient),
                buildPoolConfig(new Config()));
    }

    public TrackerServerPool(Config config) {
        LOG.debug("initializing track server pool. using {}", config);
        try {
            ClientGlobal.init(config.get("client_config_file"));
        } catch (IOException|MyException e) {
            LOG.error(e.getMessage(), e);
        }

        LOG.debug("network_timeout=" + ClientGlobal.g_network_timeout + "ms");
        LOG.debug("charset=" + ClientGlobal.g_charset);

        trackerClient = new TrackerClient();
        pool = new GenericObjectPool<>(new TrackerServerObjectFactory(trackerClient),
                buildPoolConfig(config));
    }

    private GenericObjectPoolConfig buildPoolConfig(Config config){
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();

        poolConfig.setMaxIdle(config.getInteger("max_idle", "5"));
        poolConfig.setMaxTotal(config.getInteger("max_total", "10"));
        poolConfig.setMinIdle(config.getInteger("min_idle", "2"));
        poolConfig.setMinEvictableIdleTimeMillis(config.getInteger("min_evictable_idle_time_millis", "60000"));
        poolConfig.setMaxWaitMillis(config.getInteger("max_wait_millis","5000"));

        return poolConfig;
    }

    public TrackerServer get(){
        try {
            return pool.borrowObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void release(TrackerServer server){
        if(server != null){
            pool.returnObject(server);
        }
    }

    public void close(){
        LOG.debug("closing pool");
        pool.close();
    }

    public TrackerClient getTrackerClient(){
        return trackerClient;
    }

    public <T> T use(StorageCallBack<T> callBack){
        TrackerServer trackerServer = null;
        try{
            trackerServer = get();
            //create connection to storage once a time.
            StorageClient1 client1 = new StorageClient1(trackerServer, null);
            return callBack.use(client1);
        }finally {
            release(trackerServer);
        }
    }


    /**
     * A call back interface help avoiding template codes.
     * @param <T> object you choose to return.
     */
    public interface StorageCallBack<T>{

        T use(StorageClient1 storageClient);
    }
}
