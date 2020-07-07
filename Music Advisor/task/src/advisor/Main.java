package advisor;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;
import com.sun.net.httpserver.*;

class Menu {
    boolean isRun = true;
    boolean isAuth = false;
    Scanner sc = new Scanner(System.in);
    String code="";

    public void makeServer() throws IOException, InterruptedException {
        HttpServer server = HttpServer.create();


        server.bind(new InetSocketAddress(8080), 0);

        System.out.println("use this link to request the access code:");
        System.out.println(Util.AUTH_SERVER + Util.AUTHORIZE_PART
                + "?client_id=" + Util.CLIENT_ID
                + "&redirect_uri=" + Util.REDIRECT_URI
                + "&response_type=" + Util.RESPONSE_TYPE);
        System.out.println("waiting for code...");

        //System.out.println("https://accounts.spotify.com/authorize?response_type=code&client_id=9160aebd21f244c2bf424688b92429cc&redirect_uri=http://localhost:8080");
        server.start();
        server.createContext("/",
                exchange -> {
                    String query = exchange.getRequestURI().getQuery();
                    String result;
                    if (query != null && query.contains("code")) {
                    code = query.substring(5);
                    result = "Got the code. Return back to your program.";
                } else {
                    result = "Not found authorization code. Try again.";
                }
                    exchange.sendResponseHeaders(200, result.length());
                    exchange.getResponseBody().write(result.getBytes());
                    exchange.getResponseBody().close();
                    System.out.println(result);

                }
        );

       /* HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://accounts.spotify.com/authorize?response_type=code&client_id=9160aebd21f244c2bf424688b92429cc&redirect_uri=http://localhost:8080"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(response.body());*/

        while (code.equals("")) {
            Thread.sleep(2);
        }
        server.stop(1);

        System.out.println("Making http request for access_token...");

        HttpRequest requestForAccessToken = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(
                        "client_id=" + Util.CLIENT_ID
                                + "&client_secret=" + Util.CLIENT_SECRET
                                + "&grant_type=" + Util.GRANT_TYPE
                                + "&code=" + code
                                + "&redirect_uri=" + Util.REDIRECT_URI))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .uri(URI.create(Util.AUTH_SERVER + Util.TOKEN_PART))
                .build();

        HttpResponse<String> responseWithAccessToken = HttpClient
                .newBuilder()
                .build()
                .send(requestForAccessToken,
                        HttpResponse.BodyHandlers.ofString());

        String fullToken = responseWithAccessToken.body();
        System.out.println(fullToken);


    }

    public String readInput () {
        return sc.nextLine();
    }

    public void outFeatured() {
        System.out.println("---FEATURED---\n" +
                "Mellow Morning\n" +
                "Wake Up and Smell the Coffee\n" +
                "Monday Motivation\n" +
                "Songs to Sing in the Shower\n");
    }

    public void outNew() {
        System.out.println("---NEW RELEASES---\n" +
                "Mountains [Sia, Diplo, Labrinth]\n" +
                "Runaway [Lil Peep]\n" +
                "The Greatest Show [Panic! At The Disco]\n" +
                "All Out Life [Slipknot]");
    }

    public void outCat() {
        System.out.println("---CATEGORIES---\n" +
                "Top Lists\n" +
                "Pop\n" +
                "Mood\n" +
                "Latin\n");
    }

    public void outPlay(String name) {
        System.out.println("---" + name.toUpperCase() + " PLAYLISTS---\n" +
                "Walk Like A Badass  \n" +
                "Rage Beats  \n" +
                "Arab Mood Booster  \n" +
                "Sunday Stroll\n");
    }
    public void auth () throws IOException, InterruptedException {
            System.out.println("https://accounts.spotify.com/authorize?" +
                    "client_id=9160aebd21f244c2bf424688b92429cc&" +
                    "redirect_uri=http://localhost:8080&response_type=code");
            System.out.println("---SUCCESS---");
            makeServer();
            isAuth = true;
    }

    public void run() throws IOException, InterruptedException {
        makeServer();
        while (isRun) {
            String input = readInput();

                String cName = "";
                if (input.split(" ").length > 1) {
                    cName = (input.split(" "))[1];
                }
                input = (input.split(" "))[0];
                switch (input) {
                    case "auth":
                        auth();
                        break;
                    case "featured":
                        if (isAuth) {
                            outFeatured();
                        } else {System.out.println("Please, provide access for application.");}
                        break;
                    case "new":
                        if (isAuth) {
                        outNew();
                        } else {System.out.println("Please, provide access for application.");}
                        break;
                    case "categories":
                        if (isAuth) {
                        outCat();
                        } else {System.out.println("Please, provide access for application.");}
                        break;
                    case "playlists":
                        if (isAuth) {
                        outPlay(cName);
                        } else {System.out.println("Please, provide access for application.");}
                        break;
                    case "exit":
                        System.out.println("---GOODBYE!---");
                        isRun = false;
                        break;
                }

        }


    }
}




public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        if (args.length > 1) {
            Util.setAuthServer(args[1]);
        }
        Menu m = new Menu();
        m.run();
    }
}
