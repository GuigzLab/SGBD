package up.mi.sgbdr;

import java.util.ArrayList;

public class RelationInfo {

    private String name;
    private int cols;
    private ArrayList<String> colNames;
    private ArrayList<String> colTypes;
    private int fileIdx;
    private int recordSize = 0;
    private int slotCount = 0;

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

    public int getRecordSize() {
        return recordSize;
    }

    public int getFileIdx() {
        return fileIdx;
    }

    public int getSlotCount() {
        return slotCount;
    }

    public RelationInfo(String name, int cols, ArrayList<String> colNames, ArrayList<String> colTypes, int fileIdx) {
        this.name = name;
        this.cols = cols;
        this.colNames = colNames;
        this.colTypes = colTypes;
        this.fileIdx = fileIdx;

        for (String col : colTypes) {
            switch (col) {
                case "int":
                case "float": {
                    this.recordSize += 4;
                    break;
                }
                default: {
                    int length = Integer.parseInt(String.valueOf(col.charAt(6)));
                    this.recordSize += length * 2;
                }
            }
        }

        this.slotCount = DBParams.pageSize / this.recordSize;
        if (DBParams.pageSize - this.slotCount - (this.slotCount * this.recordSize) < 0) {
            this.slotCount -= 1;
        }
    }

}
