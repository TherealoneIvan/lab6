package bmstu;

import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.server.AllDirectives;
import akka.http.javadsl.server.Route;
import akka.pattern.Patterns;
import akka.stream.ActorMaterializer;
import akka.stream.Materializer;
import akka.stream.javadsl.Flow;
import org.apache.zookeeper.KeeperException;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.CompletionStage;

public class MainZooApplication  extends AllDirectives {
    public static final Duration TIMEOUT_MILLIS =  Duration.ofMillis(5000);
    public static final String ROUTES = "routes";
    public static final String LOCALHOST = "localhost";
    public static final int PORT1 = 8080;
    public static final String HTTP_CREATE_STRING = "http://%s:%s?url=%s&count=%d";
    public static Http http;
    public static ActorRef storeActor;
    public static String PORT;
    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
        System.out.println("start!");
        ActorSystem system = ActorSystem.create(ROUTES);

        ActorRef storeActor = system.actorOf(Props.create(StoreConfActor.class));
        final Http http = Http.get(system);
        Zoo zoo = new Zoo(storeActor , "servers/" + PORT);
        final MainZooApplication app = new MainZooApplication();
        final ActorMaterializer materializer =
                ActorMaterializer.create(system);
        final Flow<HttpRequest, HttpResponse, NotUsed> routeFlow = app.createRoute().flow(system, materializer);
        final CompletionStage<ServerBinding> binding = http.bindAndHandle(
                routeFlow,
                ConnectHttp.toHost(LOCALHOST, PORT1),
                materializer
        );
        System.out.println("Server online at http://localhost:8080/\nPress RETURN to stop...");
        System.in.read();
        binding
                .thenCompose(ServerBinding::unbind)
                .thenAccept(unbound -> system.terminate());
    }
    private static CompletionStage<HttpResponse> singleReq (String url){
        return http.singleRequest(HttpRequest.create(url));
    }
    private static CompletionStage<HttpResponse> fetch (String url , String host, String sUrl , String count){
        return  http.singleRequest(HttpRequest.create(String.format(
                HTTP_CREATE_STRING,
                host , sUrl , url,Integer.parseInt(count) - 1)
        ));
    }
    private Route createRoute(){
        return get(()->
                parameter("url", url->
                        parameter("count" , count ->{
                            int countNew = Integer.parseInt(count);
                            if (countNew == 0)
                                return completeWithFuture(singleReq(url));
                            else
                                return completeWithFuture(Patterns.ask(storeActor ,new GetServerMsg("Req"),TIMEOUT_MILLIS)
                                                .thenApply(sUrl -> (String)sUrl)
                                                .thenCompose(sUrl -> fetch(LOCALHOST,sUrl , url , count))
                                );
                        })
                )
        );
    }
}
