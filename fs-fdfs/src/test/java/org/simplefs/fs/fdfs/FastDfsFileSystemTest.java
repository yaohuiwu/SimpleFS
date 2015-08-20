package org.simplefs.fs.fdfs;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.simplefs.fs.FileInfo;
import org.simplefs.fs.SimpleFileSystem;
import org.simplefs.fs.common.FileSystems;
import org.simplefs.fs.common.cfg.FastDFSConfig;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FastDfsFileSystemTest {

    SimpleFileSystem simpleFs;

    @Before
    public void setUp() throws Exception {

        URL url = FastDfsFileSystemTest.class.getResource("/fdfs_client.conf");
        Assert.assertNotNull(url);

        FastDFSConfig fdfsConfig = new FastDFSConfig();
        fdfsConfig.setClientConfig(url.getFile());

        simpleFs = FileSystems.get(fdfsConfig);
    }

    @Test
    public void testSaveGetDelete() throws Exception {

        Path loggerFile = Paths.get(FastDfsFileSystemTest.class.getResource("/").getFile(), "simplelogger.properties");
        Assert.assertNotNull(loggerFile);

        byte[] allBytes = Files.readAllBytes(loggerFile);

        //save
        String fileId = simpleFs.save(allBytes, "simplelogger.properties");
        Assert.assertNotNull(fileId);

        //get meta
        FileInfo fileInfo = simpleFs.getMeta(fileId);
        Assert.assertNotNull(fileInfo);
        System.out.println(fileInfo);

        //get content
        byte[] content = simpleFs.get(fileId);

        System.out.println(new String(content));

        Assert.assertTrue(simpleFs.delete(fileId));

    }
}
