package up.mi.sgbdr;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class DBInfo {
    public int count;
    public ArrayList<RelationInfo> list = new ArrayList<>();

    private static DBInfo INSTANCE;
    private String info = "Initial info class";

    private DBInfo() {
    }

    public static DBInfo getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DBInfo();
        }

        return INSTANCE;
    }

    public void Init() {

    }

    public void Finish() throws IOException {
        /*FileOutputStream file = new FileOutputStream("DB/Catalog.def");
        ObjectOutputStream outputStream = new ObjectOutputStream(file);
        outputStream.writeInt(count);
        for (RelationInfo relationInfo : list) {
            outputStream.writeObject(relationInfo);
        }
        outputStream.close();*/
    }

    public void displayList() {
        for (RelationInfo relationInfo : list) {
            System.out.println("======");
            System.out.println("Nom de la relation: " + relationInfo.getName());
            for (int j = 0; j < relationInfo.getCols(); j++) {
                System.out.println(relationInfo.getColNames().get(j) + ":" + relationInfo.getColTypes().get(j));
            }
            System.out.println("======");
        }

    }
}
