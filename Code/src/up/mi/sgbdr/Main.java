package up.mi.sgbdr;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException {

        //Initialiser les variables de DBParams
        DBParams.DBPath = "DB/";
        DBParams.pageSize = 4096;
        DBParams.frameCount = 2;

        DBManager.getInstance().Init();

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
