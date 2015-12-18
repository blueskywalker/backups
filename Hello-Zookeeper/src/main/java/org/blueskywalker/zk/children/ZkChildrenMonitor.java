package org.blueskywalker.zk.children;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by kkim on 8/4/15.
 */
public class ZkChildrenMonitor implements Watcher, AsyncCallback.ChildrenCallback {
    final ZooKeeper zookeeper;
    final String zkPath;
    final String zkHosts;
    final int timeout;
    final String guidPath;
    final ZKChildrenListener listener;
    boolean inProcess;
    boolean nodeChange;
    int noChildren;



    public interface ZKChildrenListener {

        void start(List<String> children);
        void shutdown();
        void run();
    }

    public ZkChildrenMonitor(final String zkHosts, final String zkPath, final int timeout,
                             final ZKChildrenListener listener) throws IOException, KeeperException, InterruptedException {
        this.listener=listener;
        this.zkHosts = zkHosts;
        this.zkPath = zkPath;
        this.timeout = timeout;

        noChildren = 0;
        inProcess=false;
        nodeChange=false;

        zookeeper = new ZooKeeper(zkHosts, timeout, this);
        if (null == zookeeper.exists(zkPath, false)) {
            final String path = zookeeper.create(zkPath, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            System.out.println(path);
        }
        guidPath = zookeeper.create(zkPath + "/guid-", null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
    }

    public String getGuidPath() {
        return guidPath;
    }

    public int getNoChildren() {
        return noChildren;
    }

    void checkPath() {
        zookeeper.getChildren(zkPath, true, this, null);

    }

    void onChildrenChanged() {
        nodeChange=true;

        if(inProcess) {
            listener.shutdown();
            inProcess=false;
        }
    }

    void onOk2Run(List<String> children) {
        noChildren=children.size();
        if(nodeChange && !inProcess) {
            //final String guidFile= new File(getGuidPath()).getName();
            listener.start(children);
            inProcess=true;
            synchronized (this) {
                notifyAll();
            }
            nodeChange=false;
        }
    }

    public void process(WatchedEvent event) {
        System.out.println(event);
        switch (event.getState()) {
            case SyncConnected:
                if (event.getType() == Event.EventType.NodeChildrenChanged
                        && event.getPath().equals(zkPath)) {
                    onChildrenChanged();
                }
                checkPath();
                break;
            default:
        }

    }

    public void processResult(int rc, String path, Object ctx, List<String> children) {

        switch (KeeperException.Code.get(rc)) {
            case OK:
                if(path.equals(zkPath))
                    onOk2Run(children);
                break;
            default:
        }

    }

    static void printDot() {
        System.out.print(".");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void run() {

        while (true) {
            synchronized (this) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            while(inProcess) {
                listener.run();
            }
        }

    }

    public static class ZKChildrenMonitorListener implements ZKChildrenListener {

        public void start(List<String> children) {
            System.out.printf("start up with %d\n", children.size());
        }

        public void shutdown() {
            System.out.println("shutdown");
        }

        public void run() {
            ZkChildrenMonitor.printDot();
        }
    }

    public static void main(String[] args) {
        String ensemble = "rtzk01:2181,rtzk02:2181,rtzk03:2181";
        String path = "/hello";
        int timeout = 3000;

        try {

            ZkChildrenMonitor monitor = new ZkChildrenMonitor(ensemble, path, timeout,new ZKChildrenMonitorListener());

            monitor.run();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }
}
