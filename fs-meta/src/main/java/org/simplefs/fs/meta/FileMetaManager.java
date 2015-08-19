package org.simplefs.fs.meta;

public interface FileMetaManager {

    /**
     * Get file meta by fid.
     * @param fileId id of the file.
     * @return meta of the file.
     */
    FileMeta get(String fileId);

    /**
     * Save the meta.
     * @param fileMeta meta of a file.
     */
    void save(FileMeta fileMeta);

    /**
     * Delete the meta by fid.
     * @param fileId if of the file.
     * @return true if deleted successfully.
     */
    boolean delete(String fileId);
}
