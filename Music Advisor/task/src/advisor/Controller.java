package advisor;

import java.io.IOException;
import java.net.http.HttpResponse;

public class Controller {


    public static void getNew() throws IOException, InterruptedException {
        String urlPath = Model.apiServ + "/v1/browse/new-releases";
        HttpResponse<String> resp;
         resp = Model.httpReq(urlPath);
         Model.parserNew(resp);
         Model.outputNameArtUrl();
    }

    public static void getFeature() throws IOException, InterruptedException {
        String urlPath = Model.apiServ + "/v1/browse/featured-playlists";
        HttpResponse<String> resp;
        resp = Model.httpReq(urlPath);
        Model.parserFeat(resp);
        Model.outputNameUrl();
    }

    public static void getCat() throws IOException, InterruptedException {
        String urlPath = Model.apiServ + "/v1/browse/categories";
        HttpResponse<String> resp;
        resp = Model.httpReq(urlPath);
        Model.parserCat(resp);
        Model.outputName();
    }

    public static void getPlay(String cname) throws IOException, InterruptedException {
        String urlPath = Model.apiServ + "/v1/browse/categories";
        HttpResponse<String> resp;
        resp = Model.httpReq(urlPath);
        if (Model.parserPlay(resp, cname)) {
            Model.outputNameUrl();
        } else {
            System.out.println("Unknown category name.");
        }
    }


    public void auth() throws IOException, InterruptedException {
        Model.makeServer();
        Model.getToken();
    }

}
