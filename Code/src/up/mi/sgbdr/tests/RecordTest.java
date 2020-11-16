package up.mi.sgbdr.tests;

import up.mi.sgbdr.DBParams;
import up.mi.sgbdr.Record;
import up.mi.sgbdr.RelationInfo;

import java.util.ArrayList;
import java.util.Arrays;

public class RecordTest {
    public static void main(String[] args) {
        DBParams.DBPath = "DB/";
        DBParams.pageSize = 4096;
        DBParams.frameCount = 2;

        String relName = "REL";
        ArrayList<String> colNames = new ArrayList<>(Arrays.asList("id", "name"));
        ArrayList<String> colTypes = new ArrayList<>(Arrays.asList("int", "string:3"));
        int colNumber = colNames.size();
        RelationInfo rel = new RelationInfo(relName, colNumber, colNames, colTypes);

        ArrayList<String> values = new ArrayList<>(Arrays.asList("3", "abc"));

        Record record = new Record(rel);
        record.setValues(values);

        byte[] byteBuffer = new byte[DBParams.pageSize];
        byteBuffer[0] = 5;

        record.writeToBuffer(byteBuffer, 0);

        /*for (byte b : byteBuffer) {
            System.out.println(b);
        }*/

        System.out.println(record.getValues());

        Record newRecord = new Record(rel);
        newRecord.readFromBuffer(byteBuffer, 0);

        System.out.println(newRecord.getValues());

    }
}
