package bmstu;

import akka.http.javadsl.server.AllDirectives;

public class MainZooApplication  extends AllDirectives {
    final ActorSystem system = ActorSystem.create("test");
    ActorRef router = system.actorOf(Props.create(ActorRouter.class));
    final Http http = Http.get(system);
    final AkkaMainApplication app = new AkkaMainApplication();
    final Materializer materializer = ActorMaterializer.create(system);
    final Flow<HttpRequest, HttpResponse, NotUsed> routeFlow =
            app.createRoute(system , router).flow(system, materializer);
    final CompletionStage<ServerBinding> binding = http.bindAndHandle(
            routeFlow,
            ConnectHttp.toHost("localhost", PORT),
            materializer
    );
        System.out.println("Server online at http://localhost:8080/\nPress RETURN to stop...");
        System.in.read();
        binding
                .thenCompose(ServerBinding::unbind)
            .thenAccept(unbound -> system.terminate());
}
}
