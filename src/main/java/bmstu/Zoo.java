package bmstu;

import akka.actor.ActorRef;
import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Zoo {
    ActorRef storeActor;
    ZooKeeper zoo;

    public Zoo(ActorRef storeActor) throws IOException {
        this.storeActor = storeActor;
        ZooKeeper zoo = new ZooKeeper("localhost", 3000, null);
    }

    private static void serverInit() throws KeeperException, InterruptedException {
        zoo.create("/servers/s",
                "data".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE ,
                CreateMode.EPHEMERAL_SEQUENTIAL);

    }

    public Watcher watcher = watchedEvent -> {
        if (watchedEvent.getType() == Watcher.Event.EventType.NodeChildrenChanged) {
            ArrayList<String> servers = new ArrayList<>();
            try {
                List<String> serversList = zoo.getChildren("/servers", null);
                for (String s : servers) {
                    byte[] data = zoo.getData("/servers/" + s, false, null);
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
