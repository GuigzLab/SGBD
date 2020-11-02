package up.mi.sgbdr;

import java.util.ArrayList;

public class DBManager {

    private static DBManager INSTANCE;
    private String info = "Initial info class";

    private DBManager() {
    }

    public static DBManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DBManager();
        }

        return INSTANCE;
    }

    public void Init() {
        DBInfo.getInstance().Init();
    }

    public void Finish() {
        DBInfo.getInstance().Finish();
        BufferManager.getInstance().FlushAll();
    }

    public void CreateRelation(String name, int number, ArrayList<String> colNames, ArrayList<String> colTypes) {
        RelationInfo newRel = new RelationInfo(name, number, colNames, colTypes);

        DBInfo.getInstance().list.add(newRel);
    }

    public void ProcessCommand(String arg) {

        String[] words = arg.split(" ");

        switch (words[0]) {
            case "CREATEREL" -> {
                String relName = words[1];
                int colNumber = words.length - 2;
                ArrayList<String> colNames = new ArrayList<>();
                ArrayList<String> colTypes = new ArrayList<>();
                for (int i = 2; i < words.length; i++) {
                    String[] col = words[i].split(":");
                    colNames.add(col[0]);
                    colTypes.add(col[1]);
                }
                CreateRelation(relName, colNumber, colNames, colTypes);
                DBInfo.getInstance().count++;
                DBInfo.getInstance().displayList();

            }

            case "DBPARAMS" ->  {
                System.out.println("Path: " + DBParams.DBPath);
                System.out.println("Page Size: " + DBParams.pageSize);
                System.out.println("Rel count: " + DBInfo.getInstance().count);
            }

            default -> System.out.println("Mauvaise commande");
        }
    }

}
