package up.mi.sgbdr;

import java.util.ArrayList;
import java.util.Arrays;

public class DBInfo {
    int count;
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

    public void Finish() {

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
