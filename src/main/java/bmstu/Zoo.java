package bmstu;

import akka.actor.ActorRef;
import org.apache.zookeeper.*;
import org.apache.zookeeper.proto.WatcherEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Zoo {
    public static final String SERVERS_ROOT_STRING = "/servers/";
    public static final String SERVERS = "/servers";
    public static final String SERVERS_NODE_STRING = "/servers/s";
    public static final String LOCALHOST = "localhost";
    public static final int SESSION_TIMEOUT = 3000;
    private ActorRef storeActor;
    private static ZooKeeper zoo;
    public Zoo(ActorRef storeActor , String port) throws IOException, KeeperException, InterruptedException {
        this.storeActor = storeActor;
        this.zoo = new ZooKeeper(LOCALHOST, SESSION_TIMEOUT, null);
        serverInit(port);
        WatchedEvent e = new WatchedEvent(Watcher.Event.EventType.NodeCreated,
                Watcher.Event.KeeperState.SyncConnected, "");
        watcher.process(e);
    }

    private static void serverInit(String port) throws KeeperException, InterruptedException {
        zoo.create(SERVERS_NODE_STRING,
                port.getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE ,
                CreateMode.EPHEMERAL_SEQUENTIAL);

    }

    public Watcher watcher = watchedEvent -> {
        if (watchedEvent.getType() == Watcher.Event.EventType.NodeChildrenChanged) {
            ArrayList<String> servers = new ArrayList<>();
            try {
                List<String> serversList = zoo.getChildren(SERVERS, null);
                for (String s : serversList) {
                    byte[] data = zoo.getData(SERVERS_ROOT_STRING + s, false, null);
                    servers.add(new String(data));
                }
            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        storeActor.tell(new ServerChangerdMsg(servers) , ActorRef.noSender());
        }
    };

    public Zoo() throws IOException {
    }
}
