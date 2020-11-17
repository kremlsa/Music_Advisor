package advisor;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpServer;

import javax.swing.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Scanner;

public class Model {
    public static  String authServ = "https://accounts.spotify.com";
    public static  String apiServ = "https://api.spotify.com";
    public static  String id = "da072c60fcee469e8b0f4140aa4480d5";
    public static  String secret = "8ada13093c704487b57c3a660448884e";
    public static  String authoriz = "/authorize";
    public static  String response = "code";
    public static  String code = "";
    public static  String tok = "/api/token";
    public static  String grant = "authorization_code";
    public static  String redirect = "http://localhost:8080";
    public static String access_token = "BQCUv9fBBSlA30Ed7fTNPm7sqyReT63qMLEMNDAi7TkHfb0T5bXEq5s2bLX0ljikZV9MXTYXnjgAsF-hXhYFBLEtJs-xeXNBeSfCDGfXu1JNawR-Vj_g7JgOpLTSBPYn3sWLK5Rt5CPUvqz_H_iJxoqsUGnPYNgr3w";
    public static void setAuthServer(String server){
        authServ = server;
    }
    public static void setApiServ(String server){
        apiServ = server;
    }
    public static void setAccess_token(String access) {access_token = access;}
    public static ArrayList<String> names;
    public static ArrayList<String> urls;
    public static ArrayList<String> artists;
    static boolean isAuth = false;
    public static boolean getAuth(){return isAuth;};
    public static int page = 5;
    public static void setPage(int p){page = p;}

    public static HttpResponse<String> httpReq(String urlPath) throws IOException, InterruptedException {
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + access_token)
                .uri(URI.create(urlPath))
                .GET()
                .build();

        HttpResponse<String> httpResp = HttpClient.newBuilder().build().send(httpRequest, HttpResponse.BodyHandlers.ofString());
        return httpResp;
    }

    public static void parserNew(HttpResponse<String> httpReq) {
        names = new ArrayList<>();
        urls = new ArrayList<>();
        artists = new ArrayList<>();
        JsonObject jo = JsonParser.parseString(httpReq.body())
                .getAsJsonObject()
                .getAsJsonObject("albums");
        for (JsonElement item : jo.getAsJsonArray("items")) {
            String album = item.getAsJsonObject()
                    .get("name")
                    .getAsString();
            names.add(album);
            String name = "";
            for (JsonElement artist : item.getAsJsonObject().getAsJsonArray("artists")) {
                name += artist.getAsJsonObject().get("name").getAsString() + ", ";
            }
            name = name.substring(0, name.length() - 2);
            artists.add("[" + name + "]");
            String link = item.getAsJsonObject()
                    .getAsJsonObject("external_urls")
                    .get("spotify")
                    .getAsString();
            urls.add(link);
        }
    }
    public static void parserFeat(HttpResponse<String> httpReq) {
        names = new ArrayList<>();
        urls = new ArrayList<>();
        artists = new ArrayList<>();
        JsonObject jo = JsonParser.parseString(httpReq.body())
                .getAsJsonObject()
                .getAsJsonObject("playlists");
        for (JsonElement item : jo.getAsJsonArray("items")) {
            String desc = item.getAsJsonObject()
                    //.get("description")
                    .get("name")
                    .getAsString();
            names.add(desc);
            String link = item.getAsJsonObject().getAsJsonObject("external_urls").get("spotify").getAsString();
            urls.add(link);
        }
    }

    public static void parserCat(HttpResponse<String> httpReq) {
        names = new ArrayList<>();
        urls = new ArrayList<>();
        artists = new ArrayList<>();
        JsonObject jo = JsonParser.parseString(httpReq.body())
                .getAsJsonObject()
                .getAsJsonObject("categories");
        for (JsonElement item : jo.getAsJsonArray("items")) {
            String name = item.getAsJsonObject()
                    .get("name")
                    .getAsString();
            names.add(name);
        }
    }

    public static boolean parserPlay(HttpResponse<String> httpReq, String cname) throws IOException, InterruptedException {
        names = new ArrayList<>();
        urls = new ArrayList<>();
        artists = new ArrayList<>();


        JsonObject jo = JsonParser.parseString(httpReq.body())
                .getAsJsonObject()
                .getAsJsonObject("categories");

        String id = "";
        for (JsonElement item : jo.getAsJsonArray("items")) {
            String name = item.getAsJsonObject()
                    .get("name")
                    .getAsString();
            // System.out.println("***" + name + "*****" + cname + "******");
            if (cname.equals(name)) {
                id = item.getAsJsonObject().get("id").getAsString();
            }
        }

        if (!id.equals("")) {
            String urlPath = apiServ + "/v1/browse/categories/" + id + "/playlists";
            HttpResponse<String> responsePl;
            responsePl = Model.httpReq(urlPath);

            JsonObject joPl = JsonParser.parseString(responsePl.body())
                    .getAsJsonObject()
                    .getAsJsonObject("playlists");

            if (responsePl.body().contains("error")) {

                JsonObject error = JsonParser
                        .parseString(responsePl.body())
                        .getAsJsonObject()
                        .getAsJsonObject("error");
                System.out.println(error.get("message"));
            } else {
                for (JsonElement item : joPl.getAsJsonArray("items")) {
                    String desc = item.getAsJsonObject()
                            .get("name")
                            .getAsString();
                    names.add(desc);
                    String link = item.getAsJsonObject().getAsJsonObject("external_urls").get("spotify").getAsString();
                    urls.add(link);
                }
            }
            return true;
        } else {
            return false;
        }
    }


    public static void makeServer() throws IOException, InterruptedException, NullPointerException {
        HttpServer server = HttpServer.create();
        server.bind(new InetSocketAddress(8080), 0);
        server.start();
        View.printLink();

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
        while (code.equals("")) {
            Thread.sleep(10);
        }
        server.stop(5);
    }
    public static void getToken() throws IOException, InterruptedException, NullPointerException {

        System.out.println("Making http request for access_token..." + code);

        HttpRequest requestForAccessToken = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(
                        "client_id=" +id
                                + "&client_secret=" + secret
                                + "&grant_type=" + grant
                                + "&code=" + code
                                + "&redirect_uri=" + redirect))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .uri(URI.create(authServ + tok))
                .build();

        HttpResponse<String> responseWithAccessToken = HttpClient
                .newBuilder()
                .build()
                .send(requestForAccessToken,
                        HttpResponse.BodyHandlers.ofString());

        String fullToken = responseWithAccessToken.body();
        System.out.println("Success!");
        isAuth = true;
        String json = fullToken;
        JsonObject jo = JsonParser.parseString(json).getAsJsonObject();
        setAccess_token(jo.get("access_token").getAsString());
    }

    public static int navigation(int i, int page, int total) {
        Scanner sc = new Scanner(System.in);

        boolean isWork = true;
        int result = 0;
        while (isWork) {
            String input = sc.nextLine();
            switch (input) {
                case "prev":
                    if (i - page >= page) {
                        isWork = false;
                        result = (i - (page*2));
                    } else {
                        System.out.println("No more pages.");
                    }
                        break;
                case "next":
                    if (i + page <= total) {
                        isWork = false;
                        result = i;
                    } else {
                        System.out.println("No more pages.");
                    }
                    break;
                case "exit":
                    result = -1;
                    isWork = false;
                    break;
            }
            return result;
        }
        return result;
    }

    public static void  outputNameUrl(){
        int numPage = names.size()/page;
        if (names.size()%page != 0) {
            numPage++;
        }
        for (int i = 1; i <= names.size(); i++) {
            System.out.println(names.get(i-1));
            System.out.println(urls.get(i-1));
            System.out.println();
            if (i % page == 0) {
                System.out.println("---PAGE " + i/page + " OF " + numPage + "---");
                i = navigation(i, page, names.size());
                if (i == -1) {
                    break;
                }
            }
        }

    }

    public static void  outputName(){
        int numPage = names.size()/page;
        if (names.size()%page != 0) {
            numPage++;
        }

        for (int i = 1; i <= names.size(); i++) {
            System.out.println(names.get(i-1));
            if (i % page == 0) {
               System.out.println("---PAGE " + i/page + " OF " + numPage + "---");
               i = navigation(i, page, names.size());
               if (i == -1) {
                   break;
               }
            }

        }

    }


    public static void  outputNameArtUrl(){
        int numPage = names.size()/page;
        if (names.size()%page != 0) {
            numPage++;
        }
        for (int i = 1; i <= names.size(); i++) {
            System.out.println(names.get(i-1));
            System.out.println(artists.get(i-1));
            System.out.println(urls.get(i-1));
            System.out.println();
            if (i % page == 0) {
                System.out.println("---PAGE " + i/page + " OF " + numPage + "---");
                i = navigation(i, page, names.size());
                if (i == -1) {
                    break;
                }
            }
        }

    }

}
