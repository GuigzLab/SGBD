package up.mi.sgbdr;

import java.nio.ByteBuffer;
import java.util.ArrayList;

public class Record {

    private final RelationInfo relationInfo;
    ArrayList<String> values;

    public Record (RelationInfo relationInfo) {
        this.relationInfo = relationInfo;
    }

    public void writeToBuffer(byte[] buff, int pos){
        ByteBuffer byteBuffer = ByteBuffer.wrap(buff);

    }

    public void readFromBuffer(byte[] buff, int pos){


    }

}
