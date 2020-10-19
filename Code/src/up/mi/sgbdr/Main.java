package up.mi.sgbdr;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        //Initialiser les variables de DBParams
        DBParams.DBPath = "DB/";
        DBParams.pageSize = 4096;

        DBManager.getInstance().Init();

        ArrayList<String> personnes = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);

        while (true) {

            System.out.println(">");

            String command = scanner.nextLine();

            if (command.equals("EXIT")) {
                DBManager.getInstance().Finish();
                System.out.println("Goodbye !");
                break;
            } else {
                DBManager.getInstance().ProcessCommand(command);
            }
        }

        scanner.close();
    }
}
