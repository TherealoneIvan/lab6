package bmstu;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.List;

public class Zoo {
    String a = "asd";
    ZooKeeper zoo = new ZooKeeper("localhost", 3000, null);
    zoo.create("/servers/s",
    a.getBytes(),
    ZooDefs.Ids.OPEN_ACL_UNSAFE ,
    CreateMode.EPHEMERAL_SEQUENTIAL);

    private Watcher watcher () throws KeeperException, InterruptedException {
        List<String> servers = zoo.getChildren("/servers", null);
        for (String s : servers) {
            byte[] data = zoo.getData("/servers/" + s, false, null);
            System.out.println("server " + s + " data=" + new String(data));
        }
    }

    public Zoo() throws IOException {
    }
}
