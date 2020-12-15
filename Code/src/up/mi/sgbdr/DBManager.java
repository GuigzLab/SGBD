package up.mi.sgbdr;

import javax.xml.catalog.Catalog;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

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

    public void Init() throws IOException, ClassNotFoundException {
        DBInfo.getInstance().Init();
        FileManager.getInstance().Init();
    }

    public void Finish() throws IOException {
        BufferManager.getInstance().FlushAll();
        DBInfo.getInstance().Finish();
    }

    public void CreateRelation(String name, int number, ArrayList<String> colNames, ArrayList<String> colTypes) {
        RelationInfo newRel = new RelationInfo(name, number, colNames, colTypes, DBInfo.getInstance().count);
        DBInfo.getInstance().list.add(newRel);
        FileManager.getInstance().CreateRelationFile(newRel);
    }

    public void ProcessCommand(String arg) {

        String[] words = arg.split(" ");

        switch (words[0]) {
            case "CREATEREL": {
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
                break;
            }

            case "DBPARAMS": {
                System.out.println("Path: " + DBParams.DBPath);
                System.out.println("Page Size: " + DBParams.pageSize);
                System.out.println("Rel count: " + DBInfo.getInstance().count);
                break;
            }

            case "RESET": {
                this.Reset();
                break;
            }

            case "INSERT": {
                if (words[1].equals("INTO")) {
                    String relName = words[2];
                    if (words[3].equals("RECORD")) {
                        String values = words[4];
                        if (values.startsWith("(") && values.endsWith(")")) {
                            values = values.substring(1, values.length() - 1);
                            String[] valuesArray = values.split(",");
                            Record record = new Record(FileManager.getInstance().getRelationInfoFromRelName(relName));
                            record.setValues(new ArrayList<>(Arrays.asList(valuesArray)));
                            FileManager.getInstance().InsertRecordInRelation(record, relName);
                        }
                    }
                }
                break;
            }

            case "BATCHINSERT": {
                //BATCHINSERT INTO nomRelation FROM FILE nomFichier.csv
                if (words.length == 6 && words[1].equals("INTO") && words[3].equals("FROM") && words[4].equals("FILE")) {
                    try (BufferedReader bufferedReader = Files.newBufferedReader(Paths.get(DBParams.DBPath + words[5]))) {
                        String line;
                        String relName = words[2];
                        Record record = new Record(FileManager.getInstance().getRelationInfoFromRelName(relName));
                        while ((line = bufferedReader.readLine()) != null) {
                            record.setValues(new ArrayList<>(Arrays.asList(line.split(","))));
                            FileManager.getInstance().InsertRecordInRelation(record, relName);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            }

            case "SELECTALL": {
                //SELECTALL FROM rel
                if (words.length == 3 && words[1].equals("FROM")) {
                    String relName = words[2];
                    ArrayList<Record> records = FileManager.getInstance().SelectAllFromRelation(relName);
                    int count = 0;
                    for (Record record : records) {
                        String value = String.join(" ; ", record.getValues());
                        System.out.println(value + ".");
                        count++;
                    }
                    System.out.println("Total records=" + count);
                }
                break;
            }

            case "SELECTS": {
                //SELECTS FROM nomRelation WHERE nomColonne=valeur
                if (words.length == 5 && words[1].equals("FROM") && words[3].equals("WHERE")) {
                    String relName = words[2];
                    String[] condition = words[4].split("=");
                    String colName = condition[0];
                    String colValue = condition[1];
                    int count = 0;
                    ArrayList<Record> records = FileManager.getInstance().SelectAllFromRelation(relName);
                    if (records.get(0).getRelationInfo().getColNames().contains(colName)) {
                        int colPos = records.get(0).getRelationInfo().getColNames().indexOf(colName);
                        for (Record record : records) {
                            if (record.getValues().get(colPos).equals(colValue)) {
                                String value = String.join(" ; ", record.getValues());
                                System.out.println(value + ".");
                                count++;
                            }
                        }
                    }
                    System.out.println("Total records=" + count);

                }
                break;
            }

            case "SELECTC": {
                //SELECTC FROM nomRelation WHERE nomColonne1OPvaleur1 AND...

                if (words.length >= 5 && words[1].equals("FROM") && words[3].equals("WHERE")) {
                    ArrayList<String> conditions = new ArrayList<>();
                    String relName = words[2];
                    int count = 0;
                    for (int i = 4; i < words.length; i++) {
                        if (!words[i].equalsIgnoreCase("AND"))
                            conditions.add(words[i]);
                    }
                    ArrayList<Record> records = FileManager.getInstance().SelectAllFromRelation(relName);

                    for (String condition : conditions) {
                        if (condition.contains("<=")) {
                            String[] args = condition.split("<=");
                            String colName = args[0];
                            if (records.get(0).getRelationInfo().getColNames().contains(colName)) {
                                int colPos = records.get(0).getRelationInfo().getColNames().indexOf(colName);
                                String colType = records.get(0).getRelationInfo().getColTypes().get(colPos);
                                switch (colType) {
                                    case "int": {
                                        int val = Integer.parseInt(args[1]);
                                        records.removeIf(record -> !(Integer.parseInt(record.getValues().get(colPos)) <= val));
                                        break;
                                    }

                                    case "float": {
                                        float val = Float.parseFloat(args[1]);
                                        records.removeIf(record -> !(Float.parseFloat(record.getValues().get(colPos)) <= val));
                                        break;
                                    }

                                    default: {
                                        break;
                                    }
                                }
                            }

                        } else if (condition.contains(">=")) {
                            String[] args = condition.split(">=");
                            String colName = args[0];
                            if (records.get(0).getRelationInfo().getColNames().contains(colName)) {
                                int colPos = records.get(0).getRelationInfo().getColNames().indexOf(colName);
                                String colType = records.get(0).getRelationInfo().getColTypes().get(colPos);
                                switch (colType) {
                                    case "int": {
                                        int val = Integer.parseInt(args[1]);
                                        records.removeIf(record -> !(Integer.parseInt(record.getValues().get(colPos)) >= val));
                                        break;
                                    }

                                    case "float": {
                                        float val = Float.parseFloat(args[1]);
                                        records.removeIf(record -> !(Float.parseFloat(record.getValues().get(colPos)) >= val));
                                        break;
                                    }

                                    default: {
                                        break;
                                    }
                                }
                            }

                        } else if (condition.contains("<")) {
                            String[] args = condition.split("<");
                            String colName = args[0];
                            if (records.get(0).getRelationInfo().getColNames().contains(colName)) {
                                int colPos = records.get(0).getRelationInfo().getColNames().indexOf(colName);
                                String colType = records.get(0).getRelationInfo().getColTypes().get(colPos);
                                switch (colType) {
                                    case "int": {
                                        int val = Integer.parseInt(args[1]);
                                        records.removeIf(record -> !(Integer.parseInt(record.getValues().get(colPos)) < val));
                                        break;
                                    }

                                    case "float": {
                                        float val = Float.parseFloat(args[1]);
                                        records.removeIf(record -> !(Float.parseFloat(record.getValues().get(colPos)) < val));
                                        break;
                                    }

                                    default: {
                                        break;
                                    }
                                }
                            }

                        } else if (condition.contains(">")) {
                            String[] args = condition.split(">");
                            String colName = args[0];
                            if (records.get(0).getRelationInfo().getColNames().contains(colName)) {
                                int colPos = records.get(0).getRelationInfo().getColNames().indexOf(colName);
                                String colType = records.get(0).getRelationInfo().getColTypes().get(colPos);
                                switch (colType) {
                                    case "int": {
                                        int val = Integer.parseInt(args[1]);
                                        records.removeIf(record -> !(Integer.parseInt(record.getValues().get(colPos)) > val));
                                        break;
                                    }

                                    case "float": {
                                        float val = Float.parseFloat(args[1]);
                                        records.removeIf(record -> !(Float.parseFloat(record.getValues().get(colPos)) > val));
                                        break;
                                    }

                                    default: {
                                        break;
                                    }
                                }
                            }

                        } else if (condition.contains("=")) {
                            String[] args = condition.split("=");
                            String colName = args[0];
                            if (records.get(0).getRelationInfo().getColNames().contains(colName)) {
                                int colPos = records.get(0).getRelationInfo().getColNames().indexOf(colName);
                                records.removeIf(record -> !record.getValues().get(colPos).equals(args[1]));
                            }
                        }
                    }

                    for (Record record : records) {
                        String value = String.join(" ; ", record.getValues());
                        System.out.println(value + ".");
                        count++;
                    }

                    System.out.println("Total records=" + count);
                }
                break;
            }

            case "UPDATE" : {
                System.out.println(":(");
                break;
            }

            case "BM": {
                BufferManager.getInstance().DisplayFrames();
                break;
            }

            case "RELS": {
                DBInfo.getInstance().displayList();
                break;
            }

            default:
                System.out.println("Mauvaise commande");
                break;
        }
    }

    private void Reset() {
        FileManager.getInstance().Reset();
        BufferManager.getInstance().Reset();
        DBInfo.getInstance().Reset();
        File folder = new File("DB/");
        File fList[] = folder.listFiles();
        for (int i = 0; i < fList.length; i++) {
            File pes = fList[i];
            if (pes.getName().endsWith(".rf") || pes.getName().equals("Catalog.def")) {
                boolean success = (new File(folder.getName() + "/" + pes.getName()).delete());
            }
        }
    }

}
