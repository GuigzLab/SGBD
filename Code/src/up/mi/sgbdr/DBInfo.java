package up.mi.sgbdr;

import java.io.*;
import java.util.ArrayList;

public class DBInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    public int count = 0;
    public ArrayList<RelationInfo> list = new ArrayList<>();

    private static DBInfo INSTANCE;
    private final String info = "DB Info";

    private DBInfo() {
    }

    public static DBInfo getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DBInfo();
        }

        return INSTANCE;
    }

    public void Init() throws IOException, ClassNotFoundException {
        FileInputStream file = new FileInputStream("DB/Catalog.def");
        ObjectInputStream inputStream = new ObjectInputStream(file);

        INSTANCE = (DBInfo) inputStream.readObject();

        inputStream.close();
        file.close();
    }

    public void Finish() throws IOException {
        if (this.count > 0) {
            FileOutputStream file = new FileOutputStream("DB/Catalog.def");
            ObjectOutputStream outputStream = new ObjectOutputStream(file);

            outputStream.writeObject(INSTANCE);

            outputStream.close();
            file.close();
        }
    }

    public void Reset() {
        this.count = 0;
        this.list.clear();
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
