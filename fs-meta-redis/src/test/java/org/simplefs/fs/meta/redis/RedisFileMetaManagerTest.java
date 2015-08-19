package org.simplefs.fs.meta.redis;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.simplefs.fs.meta.FileMeta;

/**
 * Unit test of {@link RedisFileMetaManager}.
 * <p>
 *     Start a localhost redis server before run the tests.
 * </p>
 */
@Ignore
public class RedisFileMetaManagerTest {

    @Test
    public void testSaveGetDelete() throws Exception {


        RedisFileMetaManager metaManager = new RedisFileMetaManager();

        FileMeta m = new FileMeta("fid001", "hello.png", 100, System.currentTimeMillis(), "inner-id-001");

        metaManager.save(m);
        FileMeta loadM = metaManager.get(m.getFileId());

        Assert.assertNotNull(loadM);
        System.out.println("loaded meta:" +  loadM);

//        boolean deleted = metaManager.delete(m.getFileId());
//        Assert.assertTrue(deleted);
    }

    @Test
    public void testSaveNoFileNameMeta() throws Exception {

        RedisFileMetaManager metaManager = new RedisFileMetaManager();

        //leave file name to null
        FileMeta m = new FileMeta("fid001", null, 100, System.currentTimeMillis(), "inner-id-001");
        metaManager.save(m);

        Assert.assertTrue(metaManager.delete(m.getFileId()));
    }
}
