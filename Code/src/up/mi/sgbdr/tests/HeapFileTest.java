package up.mi.sgbdr.tests;

import up.mi.sgbdr.*;
import up.mi.sgbdr.Record;

import java.util.ArrayList;
import java.util.Arrays;

public class HeapFileTest {
    public static void main(String[] args) {
        DBParams.DBPath = "DB/";
        DBParams.pageSize = 4096;
        DBParams.frameCount = 2;

        String relName = "REL";
        ArrayList<String> colNames = new ArrayList<>(Arrays.asList("id", "name"));
        ArrayList<String> colTypes = new ArrayList<>(Arrays.asList("int", "string3"));
        int colNumber = colNames.size();
        RelationInfo rel = new RelationInfo(relName, colNumber, colNames, colTypes, 0);

        HeapFile heapFile = new HeapFile(rel);

        Frame frame = BufferManager.getInstance().getFrames().get(0);

//        System.out.println("NEW");
//        heapFile.createNewOnDisk();

//        System.out.println("ADDDATAPAGE");
//        heapFile.addDataPage();

        PageID free = heapFile.getFreeDataPageId();

//        System.out.println(free.getFileIdx() + "," + free.getPageIdx());

        Record record = new Record(rel);
        record.setValues(new ArrayList<String>(Arrays.asList("3", "acb")));
        Rid rid = heapFile.writeRecordToDataPage(record, free);

        BufferManager.getInstance().FlushAll();



    }
}
