package org.blueskywalker.zookeeper;
import org.apache.log4j.Logger;
import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.KeeperException.Code;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

/**
 *
 * @author kkim
 */
public class ZNodeDeletionMonitor implements Watcher, AsyncCallback.StatCallback {
    public static final Logger logger = Logger.getLogger(ZNodeDeletionMonitor.class);

    ZooKeeper zk=null;
    String zNodePath;
    boolean isZookeeperSesionClosed = false;
    ZNodeDeletionMonitorListener listener = null;



    public interface ZNodeDeletionMonitorListener {
        void onZNodeDeleted();
        void onZooKeeperSessionClosed();
    }

    private ZooKeeper getZooKeeper() {
        assert null != zk;
        return zk;
    }

    private final String getZNodePath() {
        return zNodePath;
    }

    public boolean getIsZookeeperSessionClosed() {
        return isZookeeperSesionClosed;
    }

    private ZNodeDeletionMonitorListener getListener() {
        assert null != listener;
        return listener;
    }

    public ZNodeDeletionMonitor(ZooKeeper zookeeper,String zNodePath,
                                ZNodeDeletionMonitorListener listener) {
        this.zk = zookeeper;
        this.zNodePath = zNodePath;
        this.listener = listener;

        checkZNodeExistsAsync();
    }

    private void checkZNodeExistsAsync() {
        getZooKeeper().exists(getZNodePath(), true, this,null);
    }

    private void onZooKeeperSessionClosed() {
        isZookeeperSesionClosed=true;
        getListener().onZooKeeperSessionClosed();
    }


    public void process(WatchedEvent event) {
        String path = event.getPath();
        if (event.getType() == Event.EventType.None) {
            switch(event.getState()) {
                case SyncConnected:
                    break;
                case Expired:
                    onZooKeeperSessionClosed();
                    break;
            }
        } else {
            if(path!=null && path.equals(getZNodePath())) {
                checkZNodeExistsAsync();
            }
        }
    }


    @SuppressWarnings("deprecation")
    public void processResult(int rc, String path, Object ctx, Stat stat) {
        if (rc == Code.NoNode /*ordinal()*/) {
            getListener().onZNodeDeleted();
        } else if ( rc == Code.Ok /*ordinal()*/) {
            logger.info("Node exists");
        } else if (rc == Code.SessionExpired ||
                rc == Code.NoAuth) {
            onZooKeeperSessionClosed();
        } else {
            checkZNodeExistsAsync();
        }
    }
}

