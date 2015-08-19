package org.simplefs.fs.local;

import org.junit.Assert;
import org.junit.Test;
import org.simplefs.fs.meta.FileMetaManager;
import org.simplefs.fs.meta.MetaManagers;
import org.simplefs.fs.meta.redis.RedisFileMetaManager;

/**
 * Created by wuyaohui on 15-8-19.
 */
public class MetaManagersTest {

    @Test
    public void testGetDefault() throws Exception {

        FileMetaManager metaManager = MetaManagers.getDefault();

        Assert.assertNotNull(metaManager);
        Assert.assertTrue(metaManager instanceof RedisFileMetaManager);
    }
}
