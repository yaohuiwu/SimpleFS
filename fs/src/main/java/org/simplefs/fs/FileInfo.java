package org.simplefs.fs;

/**
 * Meta info of a file.
 */
public class FileInfo {

    /**
     * The only important thing you need to know to enjoy the SimpleFileSystem.
     */
    final String fileId;

    final String fileName;

    final long fileSize;

    final long createTime;

    public FileInfo(String fileId, String fileName, long fileSize, long createTime) {
        this.fileId = fileId;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.createTime = createTime;
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

    @Override
    public String toString() {
        return "FileInfo{" +
                "fileId='" + fileId + '\'' +
                ", fileName='" + fileName + '\'' +
                ", fileSize=" + fileSize +
                ", createTime=" + createTime +
                '}';
    }
}
