package org.blueskywalker.zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.KeeperException.Code;
import java.io.IOException;
import java.util.List;

/**
 * Created by kkim on 8/3/15.
 */
public class ZKMain implements Watcher,AsyncCallback.ChildrenCallback {

    final static String zkEnsemble = "rtzk01:2181,rtzk02:2181,rtzk03:2181";
    final static int zkTimeout = 3000;
    final static String zkPath = "/hello";
    String guidPath;
    final ZooKeeper zookeeper;

    public ZKMain() throws IOException, KeeperException, InterruptedException {
        this.zookeeper = new ZooKeeper(zkEnsemble, zkTimeout,this);
        createFolder();
    }

    protected void createFolder() throws KeeperException, InterruptedException {
        if(null == zookeeper.exists(zkPath,false)) {
            final String path = zookeeper.create(zkPath,null, ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
            System.out.println(path);
        }
        guidPath = zookeeper.create(zkPath +"/guid-",null,ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL_SEQUENTIAL);

    }

    void checkPath() {
        zookeeper.getChildren(zkPath,true,this,null);

    }

    public void run() throws InterruptedException {

        while(true) {
            System.out.print(".");
            Thread.sleep(1000);
        }
    }

    public static void main(String[] args) {
        try {
            ZKMain main = new ZKMain();

            main.run();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void process(WatchedEvent event) {
        System.out.println(event.toString());

        String path = event.getPath();
        if(null!=path && path.equals(zkPath)) {
            System.out.println(event.getPath());
        }

        System.out.println(event.getType());

        switch (event.getState()) {
            case AuthFailed:
                System.out.println("1");
                break;
            case ConnectedReadOnly:
                System.out.println("2");
                break;
            case Disconnected:
                System.out.println("3");
                break;
            case Expired:
                System.out.println("4");
                break;
            case SaslAuthenticated:
                System.out.println("5");
                break;
            case SyncConnected:
                System.out.println("6");
                checkPath();
                break;

        }
    }

    void printCode(int rc) {

        switch (Code.get(rc)) {
            case APIERROR:
                System.out.println(Code.APIERROR);break;
            case AUTHFAILED:
                System.out.println(Code.AUTHFAILED); break;
            case BADARGUMENTS:
                System.out.println(Code.BADARGUMENTS); break;
            case BADVERSION:
                System.out.println(Code.BADVERSION); break;
            case CONNECTIONLOSS:
                System.out.println(Code.CONNECTIONLOSS); break;
            case DATAINCONSISTENCY:
                System.out.println(Code.DATAINCONSISTENCY); break;
            case INVALIDACL:
                System.out.println(Code.INVALIDACL); break;
            case INVALIDCALLBACK:
                System.out.println(Code.INVALIDCALLBACK); break;
            case MARSHALLINGERROR:
                System.out.println(Code.MARSHALLINGERROR); break;
            case NOAUTH:
                System.out.println(Code.NOAUTH); break;
            case NOCHILDRENFOREPHEMERALS:
                System.out.println(Code.NOCHILDRENFOREPHEMERALS); break;
            case NODEEXISTS:
                System.out.println(Code.NODEEXISTS); break;
            case NONODE:
                System.out.println(Code.NONODE); break;
            case NOTEMPTY:
                System.out.println(Code.NOTEMPTY); break;
            case NOTREADONLY:
                System.out.println(Code.NOTREADONLY); break;
            case OK:
                System.out.println(Code.OK); break;
            case OPERATIONTIMEOUT:
                System.out.println(Code.OPERATIONTIMEOUT); break;
            case RUNTIMEINCONSISTENCY:
                System.out.println(Code.RUNTIMEINCONSISTENCY); break;
            case SESSIONEXPIRED:
                System.out.println(Code.SESSIONEXPIRED); break;
            case SESSIONMOVED:
                System.out.println(Code.SESSIONMOVED); break;
            case SYSTEMERROR:
                System.out.println(Code.SYSTEMERROR); break;
            case UNIMPLEMENTED:
                System.out.println(Code.UNIMPLEMENTED); break;
        }
    }

    public void processResult(int rc, String path, Object ctx, List<String> children) {

        System.out.println(path);

        for(String c: children) {
            System.out.println(c);
        }

        printCode(rc);
    }
}
