package bmstu;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Zoo {
    String a = "asd";
    static ZooKeeper zoo;
    static {
        try {
            zoo = new ZooKeeper("localhost", 3000, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void serverInit() throws KeeperException, InterruptedException {
        zoo.create("/servers/s",
                "data".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE ,
                CreateMode.EPHEMERAL_SEQUENTIAL);
    }

    private Watcher watcher = watchedEvent -> {
        if (watchedEvent.getType() == Watcher.Event.EventType.NodeChildrenChanged) {
            ArrayList<String> servers = new ArrayList<>();
            try {
                List<String> serversList = zoo.getChildren("/servers", null);
                for (String s : servers) {
                    byte[] data = zoo.getData("/servers/" + s, false, null);
                }
            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    public Zoo() throws IOException {
    }
}
