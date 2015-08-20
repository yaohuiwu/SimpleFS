package org.simplefs.fs.local;

import org.junit.Assert;
import org.junit.Test;
import org.simplefs.fs.FileInfo;
import org.simplefs.fs.SimpleFileSystem;
import org.simplefs.fs.common.FileSystems;
import org.simplefs.fs.common.cfg.LocalFSConfig;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LocalFileSystemTest {

    @Test
    public void testSaveGetDelete() throws Exception {

        URL classRootUrl = LocalFileSystemTest.class.getResource("/");
        Assert.assertNotNull(classRootUrl);

        Path path = Paths.get(classRootUrl.getFile(), "simplefs");
        if(!Files.exists(path)){
            Files.createDirectory(path);
        }

        LocalFSConfig localFSConfig = new LocalFSConfig();
        localFSConfig.setBaseDir(path.toString());

        SimpleFileSystem simpleFs = FileSystems.get(localFSConfig);

        Assert.assertNotNull(simpleFs);
        Assert.assertTrue(simpleFs instanceof LocalFileSystem);
        Assert.assertNotNull(((LocalFileSystem)simpleFs).getBaseDir());

        Path loggerFile = Paths.get(classRootUrl.getFile(), "simplelogger.properties");
        Assert.assertNotNull(loggerFile);

        byte[] allBytes = Files.readAllBytes(loggerFile);

        //save
        String fileId = simpleFs.save(allBytes, "simplelogger.properties");

        //get meta
        FileInfo fileInfo = simpleFs.getMeta(fileId);
        System.out.println(fileInfo);

        //get content
        byte[] content = simpleFs.get(fileId);

        System.out.println(new String(content));

        Assert.assertTrue(simpleFs.delete(fileId));
    }


}
