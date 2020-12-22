package bmstu;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.List;

public class Zoo {
    ZooKeeper zoo = new ZooKeeper("localhost", 3000, this);
    zoo.create("/servers/s",
    ZooDefs.Ids.OPEN_ACL_UNSAFE ,
    CreateMode.EPHEMERAL_SEQUENTIAL);
    List<String> servers = zoo.getChildren("/servers", this);
    for (String s : servers) {
        byte[] data = zoo.getData("/servers/" + s, false, null);
        System.out.println("server " + s + " data=" + new String(data));
    }
    private Watcher watcher 

    public Zoo() throws IOException {
    }
}
