package org.simplefs.fs;

import org.simplefs.fs.cfg.Config;

public abstract class AbstractSimpleFileSystem implements SimpleFileSystem{

    protected Config config;

    protected abstract void setConfig(Config config);

    @Override
    public String save(byte[] fileContent) {
        return save(fileContent, null);
    }
}
