package org.simplefs.fs.meta.redis;

import org.apache.commons.lang3.StringUtils;
import org.simplefs.fs.meta.FileMeta;
import org.simplefs.fs.meta.FileMetaManager;
import org.simplefs.fs.meta.serial.proto.FileMetaProtos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Properties;

/**
 * File Meta management by redis.
 */
public class RedisFileMetaManager implements FileMetaManager{

    private static final Logger LOG = LoggerFactory.getLogger(RedisFileMetaManager.class);

    private static final String SIMPLE_NAMESPACE = "fs.simplefs";

    private JedisPool jedisPool;

    public RedisFileMetaManager() {
        jedisPool = new JedisPool("localhost");
    }

    public RedisFileMetaManager(Properties config) {
        LOG.debug("initializing File Meta Manager, using config:{}", config);

        JedisPoolConfig plConfig = new JedisPoolConfig();
        plConfig.setMaxTotal(Integer.valueOf(config.getProperty("redis.maxTotal", "300")));
        plConfig.setMaxIdle(Integer.valueOf(config.getProperty("redis.maxIdle", "30")));
        plConfig.setMaxWaitMillis(Integer.valueOf(config.getProperty("redis.maxWait", "1000")));
        plConfig.setTestOnBorrow(Boolean.valueOf(config.getProperty("redis.testOnBorrow", "true")));

        String host = config.getProperty("redis.host", "localhost");
        int port = Integer.valueOf(config.getProperty("redis.port", "6379"));

        jedisPool = new JedisPool(plConfig, host, port);
    }

    public FileMeta get(String fileId) {
        if(StringUtils.isEmpty(fileId)){
            return null;
        }
        FileMeta meta = null;
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            byte[] bytes = jedis.get((SIMPLE_NAMESPACE + "." + fileId).getBytes());
            if(bytes != null){
                FileMetaProtos.FileMeta mt = FileMetaProtos.FileMeta.parseFrom(bytes);
                meta = new FileMeta(mt.getFileId(),
                        mt.getFileName(),
                        mt.getFileSize(),
                        mt.getCreateTime(),
                        mt.getInnerFileId());
            }
        }catch (Exception e){
            LOG.error("error getting file meta of " + fileId);
            throw new RuntimeException(e.getCause());
        }finally {
            jedisPool.returnResource(jedis);
        }
        return meta;
    }

    public void save(FileMeta fileMeta) {
        if(fileMeta != null){
            //check fid and inner fid
            if(StringUtils.isEmpty(fileMeta.getFileId())){
                throw new IllegalArgumentException("file id can't be null");
            }

            LOG.debug("saving {}", fileMeta);

            FileMetaProtos.FileMeta.Builder b = FileMetaProtos.FileMeta.newBuilder();
            b.setFileId(fileMeta.getFileId())
                    .setFileSize(fileMeta.getFileSize())
                    .setInnerFileId(fileMeta.getInnerFileId())
                    .setCreateTime(fileMeta.getCreateTime());
            if(StringUtils.isNotEmpty(fileMeta.getFileName())){
                b.setFileName(fileMeta.getFileName());
            }
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                jedis.set((SIMPLE_NAMESPACE + "." + fileMeta.getFileId()).getBytes(),
                        b.build().toByteArray());
            }catch (Exception e){
                LOG.error("error saving file meta " + fileMeta.getFileId());

                throw new RuntimeException(e.getCause());
            }finally {
                jedisPool.returnResource(jedis);
            }
        }
    }

    public boolean delete(String fileId) {
        Jedis jedis = null;
        Long result = null;
        try{
            jedis = jedisPool.getResource();
            result = jedis.del((SIMPLE_NAMESPACE + "." + fileId).getBytes());
        }catch (Exception e){
            LOG.error("error delete file meta of " + fileId);
            throw new RuntimeException(e.getCause());
        }finally {
            jedisPool.returnResource(jedis);
        }
        return result != null && result == 1;
    }

}
