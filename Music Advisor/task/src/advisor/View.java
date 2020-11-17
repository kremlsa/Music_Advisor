package advisor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

public class View {
    Locale usLocale = Locale.US;
    boolean isRun = true;
    boolean isAuth = false;
    Scanner sc = new Scanner(System.in);
    static String code="";

        public static void printLink() {
        System.out.println("use this link to request the access code:");
        System.out.println(Model.authServ + Model.authoriz
                + "?client_id=" + Model.id
                + "&redirect_uri=" + Model.redirect
                + "&response_type=" + Model.response);
        System.out.println("waiting for code...");
    }
    public String readInput () {
        return sc.nextLine();
    }

    public void run() throws IOException, InterruptedException, NullPointerException {

        while (isRun) {
            String input = readInput();

            String cName = "";
            String[] inputArr = input.split(" ");
            if (inputArr.length > 1) {
                for (int c = 1; c < inputArr.length; c++) {
                    cName += inputArr[c] + " ";
                }
                cName = cName.trim();

            }
            input = (input.split(" "))[0];
            switch (input) {
                case "auth":
                    //auth();
                    Model.makeServer();
                    Model.getToken();
                    break;
                case "featured":
                    if (Model.getAuth()) {
                        Controller.getFeature();
                    } else {System.out.println("Please, provide access for application.");}
                    break;
                case "new":
                    if (Model.getAuth()) {
                        Controller.getNew();
                    } else {System.out.println("Please, provide access for application.");}
                    break;
                case "categories":
                    if (Model.getAuth())  {
                        Controller.getCat();
                    } else {System.out.println("Please, provide access for application.");}
                    break;
                case "playlists":
                    if (Model.getAuth()) {
                      Controller.getPlay(cName);
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
