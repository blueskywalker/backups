package org.blueskywalker.zookeeper;


import java.util.Collections;
import java.util.List;
import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.blueskywalker.twitter.hosebird.YamlType;

/**
 *
 * @author kkim
 */
public abstract class ZKElectableClient implements Watcher, ZNodeMonitor.ZNodeMonitorListener {

    public static final Logger logger = Logger.getLogger(ZKElectableClient.class);

    private final ZooKeeper zookeeper;
    private boolean isLeader;

    private final String ELECTION_ZNODE_PATH;
    private final String SERVER_HOSTS;
    private final int TIMEOUT;
    ZNodeMonitor zNodeMonitor;
    private String electionGUIDZNodePath;

    public ZKElectableClient(YamlType properties) throws Exception {
        YamlType opt = properties.getYaml("zookeeper");
        this.ELECTION_ZNODE_PATH = opt.getString("path");
        this.SERVER_HOSTS = opt.getString("hosts");
        this.TIMEOUT = opt.getInteger("timeout");

        zookeeper = new ZooKeeper(getHosts(), getTimeout(), this);
        conditionalCreateElectionZNode();
        createElectionGUIDZNode();

    }

    public void close() throws InterruptedException {
        zookeeper.close();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        close();
    }

    public final String getElectionZNodePath() {
        return ELECTION_ZNODE_PATH;
    }

    public final String getHosts() {
        return SERVER_HOSTS;
    }

    public final int getTimeout() {
        return TIMEOUT;
    }

    private ZooKeeper getZooKeeper() {
        assert null != zookeeper;
        return zookeeper;
    }

    protected boolean getCachedIsLeader() {
        return isLeader;
    }

    private ZNodeMonitor getZNodeDeletionMonitor() {
        assert null != zNodeMonitor;
        return zNodeMonitor;
    }

    protected final String getElectionGUIDZNodePath() {
        assert null != electionGUIDZNodePath;
        return electionGUIDZNodePath;
    }

    private String formatElectionGUIDZNodePath(String zNodeGUID) {
        return getElectionZNodePath() + "/" + zNodeGUID;
    }

    private String getLeaderElectionGUIDZNodePath(List<String> optionalGUIDs) throws KeeperException, InterruptedException {
        List<String> guids = ((optionalGUIDs == null) || optionalGUIDs.isEmpty())
                ? getZooKeeper().getChildren(getElectionZNodePath(), false /*bWatch*/) : optionalGUIDs;
        if (!guids.isEmpty()) {
            String leaderGUID = formatElectionGUIDZNodePath(Collections.min(guids));
            logger.info("ZooElectableClient::getLeaderElectionGUIDZNodePath:: " + leaderGUID);
            return leaderGUID;
        } else {
            logger.info("ZooElectableClient::getLeaderElectionGUIDZNodePath:: no GUIDS exist!");
            return null;
        }
    }

    private String getZNodePathToWatch(List<String> optionalGUIDs) throws KeeperException, InterruptedException {
        if (getCachedIsLeader()) {
            logger.info("ZooElectableClient::getZNodePathToWatch:: (" + getElectionGUIDZNodePath() + ") -> " + getElectionGUIDZNodePath());
            return getElectionGUIDZNodePath();
        }

        List<String> guids = ((optionalGUIDs == null) || optionalGUIDs.isEmpty())
                ? getZooKeeper().getChildren(getElectionZNodePath(), false) : optionalGUIDs;

        if (!guids.isEmpty()) {
            String zNodePathToWatch = null;
            int itrGUID = 0;
            for (; itrGUID < guids.size(); ++itrGUID) {
                String guid = formatElectionGUIDZNodePath(guids.get(itrGUID));
                if (guid.compareTo(getElectionGUIDZNodePath()) < 0) {
                    zNodePathToWatch = guid;
                    break;
                }
            }

            assert null != zNodePathToWatch;

            for (; itrGUID < guids.size(); ++itrGUID) {
                String guid = formatElectionGUIDZNodePath(guids.get(itrGUID));
                if ((guid.compareTo(zNodePathToWatch) > 0)
                        && (guid.compareTo(getElectionGUIDZNodePath()) < 0)) {
                    zNodePathToWatch = guid;
                }
            }

            logger.info("ZooElectableClient::getZNodePathToWatch:: (" + getElectionGUIDZNodePath() + ") -> " + zNodePathToWatch);
            return zNodePathToWatch;
        } else {
            logger.info("ZooElectableClient::getZNodePathToWatch:: no GUIDS exist!");
            return null;
        }
    }

    private void createParentDirs(String paths) throws KeeperException, InterruptedException {
        int start = 0;
        int index = paths.indexOf('/');
        while (index >= 0) {
            String parent = paths.substring(0, index);
            if (!parent.isEmpty()) {
                if (null == getZooKeeper().exists(getElectionZNodePath(), false)) {
                    final String path = getZooKeeper().create(parent,
                            null, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                    logger.info("ZooElectableClient::conditionalCreateElectionZNode:: created with path:" + path);
                }
            }
            start = index + 1;
            index = paths.indexOf('/', start);
        }
    }

    private void conditionalCreateElectionZNode() throws KeeperException, InterruptedException {

        createParentDirs(getElectionZNodePath());

        if (null == getZooKeeper().exists(getElectionZNodePath(), false)) {
            try {
                final String path = getZooKeeper().create(getElectionZNodePath(),
                        null, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                logger.info("ZooElectableClient::conditionalCreateElectionZNode:: created with path:" + path);
            } catch (KeeperException.NodeExistsException ne) {
                logger.info("ZooElectableClient::conditionalCreateElectionZNode:: failed (NodeExistsException)");
            }
        } else {
            logger.info("ZooElectableClient::conditionalCreateElectionZNode:: already created.");
        }
    }

    private void createElectionGUIDZNode() throws KeeperException, InterruptedException {
        electionGUIDZNodePath = getZooKeeper().create(getElectionZNodePath() + "/guid-", null, Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL_SEQUENTIAL);
        logger.info("ZooElectableClient::createElectionGUIDZNode:: created with path:" + electionGUIDZNodePath);
    }

    private void electAndCacheLeader() throws KeeperException, InterruptedException {
        isLeader = getElectionGUIDZNodePath().equals(getLeaderElectionGUIDZNodePath(null));
        logger.info("ZooElectableClient::electAndCacheLeader:: " + isLeader);
    }

    private void resetZNodeDeletionMonitor() throws KeeperException, InterruptedException {
        zNodeMonitor = new ZNodeMonitor(getZooKeeper(), getZNodePathToWatch(null), this);
    }

    // watcher

    public void process(WatchedEvent event) {
        if (null != zNodeMonitor) {
            getZNodeDeletionMonitor().process(event);
        }
    }


    public void onZNodeDeleted() {
        try {
            determineAndPerformRole();
        } catch (Exception e) {
            // nothing
        }
    }


    public void onZooKeeperSessionClosed() {
        synchronized (this) {
            notifyAll();
        }
    }

    private void determineAndPerformRole() throws KeeperException, InterruptedException {
        electAndCacheLeader();
        performRole();
        resetZNodeDeletionMonitor();
    }

    public void run() throws KeeperException, InterruptedException {
        determineAndPerformRole();

        try {
            synchronized (this) {
                while (!getZNodeDeletionMonitor().getIsZookeeperSessionClosed()) {
                    wait();
                }
            }
        } catch (InterruptedException e) {
            // nothing
        }
    }

    abstract protected void performRole();
}

