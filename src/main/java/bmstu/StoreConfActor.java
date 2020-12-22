package bmstu;

import akka.actor.AbstractActor;

public class StoreConfActor extends AbstractActor {
    private String[] servers;
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(GetServerMsg.class , msg ->{

                })
                .match()
    }
}
