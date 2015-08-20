package org.simplefs.fs.meta.serial;

import org.junit.Test;
import org.simplefs.fs.meta.FileMeta;
import org.simplefs.fs.meta.serial.proto.FileMetaProtos;

import java.util.UUID;

public class FileMetaProtosTest {

    @Test
    public void testBytesOfSingleFileMeta() throws Exception {

        FileMeta m = new FileMeta(UUID.randomUUID().toString(),
                "my.conf", 1000, System.currentTimeMillis(), UUID.randomUUID().toString());

        FileMetaProtos.FileMeta.Builder builder = FileMetaProtos.FileMeta.newBuilder();

        builder.setFileId(m.getFileId())
                .setFileName(m.getFileName())
                .setFileSize(m.getFileSize())
                .setCreateTime(m.getCreateTime())
                .setInnerFileId(m.getInnerFileId());

        byte[] mBytes = builder.build().toByteArray();
        System.out.println("byte number:" + mBytes.length);
    }
}
