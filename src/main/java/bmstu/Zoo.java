package bmstu;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

import java.util.List;

public class Zoo {
    ZooKeeper zoo = new ZooKeeper("localhost", 3000, this);
    zoo.create("/servers/s",
        data.getBytes(),
    ZooDefs.Ids.OPEN_ACL_UNSAFE ,
    CreateMode.EPHEMERAL_SEQUENTIAL);
    Watcher 

}
