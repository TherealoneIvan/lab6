package bmstu;

import akka.actor.AbstractActor;

import java.util.ArrayList;

public class StoreConfActor extends AbstractActor {
    private ArrayList<String> servers;
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(GetServerMsg.class , msg ->{

                })
                .match(ServerChangerdMsg.class , msg ->{
                    this.servers = msg.getServers();
                })
                .build();
    }
    private String getRandomServer() {
        return 
    }
}
