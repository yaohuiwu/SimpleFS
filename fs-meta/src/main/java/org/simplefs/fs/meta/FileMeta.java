package org.simplefs.fs.meta;

/**
 * Meta info of a file.
 */
public class FileMeta {

    /**
     * The only important thing you need to know to enjoy the SimpleFileSystem.
     */
    final String fileId;

    final String fileName;

    final long fileSize;

    final long createTime;

    final String innerFileId;

    public FileMeta(String fileId, String fileName, long fileSize, long createTime, String innerFileId) {
        this.fileId = fileId;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.createTime = createTime;
        this.innerFileId = innerFileId;
    }

    public String getFileId() {
        return fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public long getCreateTime() {
        return createTime;
    }

    public String getInnerFileId() {
        return innerFileId;
    }

    @Override
    public String toString() {
        return "FileMeta{" +
                "fileId='" + fileId + '\'' +
                ", fileName='" + fileName + '\'' +
                ", fileSize=" + fileSize +
                ", createTime=" + createTime +
                ", innerFileId='" + innerFileId + '\'' +
                '}';
    }
}
