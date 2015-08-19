package org.simplefs.fs;

/**
 * All the File API you can use.
 * <p>
 *    The Simple File System is a readonly file system. Once saved, the only thing
 *  you can do with the file is read, delete. get meta info.
 * </p>
 */
public interface SimpleFileSystem {

    /**
     * Save a file into SFS.
     * @param fileContent byte content of the file.
     * @return a string file id return for you to handle the file
     *  after the file saved.
     */
    String save(byte[] fileContent);

    /**
     * Delete the file by file id.
     * @param fileId id of the file.(you got it from save* methods).
     * @return true if the file successfully deleted, otherwise return false.
     */
    boolean delete(String fileId);

    /**
     * Get the file content by its file id.
     * @param fileId id of the file.
     * @return file content in byte array.
     */
    byte[] get(String fileId);

    /**
     * Get meta info of the file by file id.
     * @param fileId id of the file.
     * @return Meta info of the file.
     */
    FileInfo getMeta(String fileId);
}
