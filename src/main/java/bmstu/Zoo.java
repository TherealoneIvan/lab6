package bmstu;

import org.apache.zookeeper.*;

import java.util.List;

public class Zoo {
    ZooKeeper zoo = new ZooKeeper("localhost", 3000, this);
    zoo.create("/servers/s",
    ZooDefs.Ids.OPEN_ACL_UNSAFE ,
    CreateMode.EPHEMERAL_SEQUENTIAL);
    Watcher watcher = new Watcher() {
        @Override
        public void process(WatchedEvent watchedEvent) {

        }
    }

}
