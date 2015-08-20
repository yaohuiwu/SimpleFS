package org.simplefs.fs.fdfs.connection;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Factory that handle lifecycle event of TrackServer.
 */
public class TrackerServerObjectFactory implements PooledObjectFactory<TrackerServer> {

    private static final Logger LOG = LoggerFactory.getLogger(TrackerServerObjectFactory.class);

    private TrackerClient tracker;

    public TrackerServerObjectFactory(TrackerClient tracker) {
        this.tracker = tracker;
    }

    public PooledObject<TrackerServer> makeObject() throws Exception {
        return new DefaultPooledObject<TrackerServer>(tracker.getConnection());
    }

    public void destroyObject(PooledObject<TrackerServer> pooledObject) throws Exception {
        LOG.debug("closing track server");
        pooledObject.getObject().close();
    }

    public boolean validateObject(PooledObject<TrackerServer> pooledObject) {
        TrackerServer trackerServer = pooledObject.getObject();
        try{
            tracker.listGroups(trackerServer);
            LOG.debug("valide tracker server " + trackerServer);
        }catch (Exception e){
            LOG.error(e.getMessage(), e);
            return false;
        }

        return true;
    }

    public void activateObject(PooledObject<TrackerServer> pooledObject) throws Exception {
    }

    public void passivateObject(PooledObject<TrackerServer> pooledObject) throws Exception {
    }
}
