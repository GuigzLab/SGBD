package up.mi.sgbdr;
import java.util.ArrayList;

public class RelationInfo {
    private String name;
    private int cols;
    private ArrayList<String> colNames;
    private ArrayList<String> colTypes;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCols() {
        return cols;
    }

    public void setCols(int cols) {
        this.cols = cols;
    }

    public ArrayList<String> getColNames() {
        return colNames;
    }

    public void setColNames(ArrayList<String> colNames) {
        this.colNames = colNames;
    }

    public ArrayList<String> getColTypes() {
        return colTypes;
    }

    public void setColTypes(ArrayList<String> colTypes) {
        this.colTypes = colTypes;
    }

    RelationInfo (String name, int cols, ArrayList<String> colNames, ArrayList<String> colTypes) {
        this.name = name;
        this. cols = cols;
        this.colNames = colNames;
        this.colTypes = colTypes;
    }

    /*public static void ColInfo(){

    }*/
}
