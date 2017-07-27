package learning.mockServer;

import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.socket.PortFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Created by isaac on 03/07/2017.
 */
public class Main {
    private static int port = PortFactory.findFreePort();
    private static ClientAndServer mockServer = ClientAndServer.startClientAndServer(port);

    public static void main(String[] args){
        mockServer.when(HttpRequest.request().withPath("/test")).respond(HttpResponse.response().withBody("mock server test"));
        RestTemplate restTemplate = new RestTemplate();
        String forObject = restTemplate.getForObject(String.format("http://localhost:%s/test",port), String.class);
        System.out.println("===response:===="+forObject);
    }
}
