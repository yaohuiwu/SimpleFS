package org.simplefs.fs.fdfs.connection;

import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.StorageClient1;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Ignore
public class TrackServerPoolTest {

    private TrackerServerPool pool;

    @Before
    public void setUp() throws Exception {

        URL url = TrackServerPoolTest.class.getResource("/fdfs_client.conf");
        Assert.assertNotNull(url);

        pool = new TrackerServerPool(url.getFile());
    }

    @After
    public void tearDown() throws Exception {
        pool.close();
    }

    @Test
    public void testUse() throws Exception {

        URL url = TrackServerPoolTest.class.getResource("/fdfs_client.conf");
        Path path = Paths.get(url.toURI());
        final byte[] bytes = Files.readAllBytes(path);

        String fileId = pool.use(new TrackerServerPool.StorageCallBack<String>() {
            @Override
            public String use(StorageClient1 client) {
                String fileId;
                try {
                    fileId =  client.upload_file1(bytes, "", new NameValuePair[0]);
                } catch (IOException | MyException e) {
                    e.printStackTrace();
                    return null;
                }
                return fileId;
            }
        });

        System.out.println("file id:" +  fileId);
    }
}
