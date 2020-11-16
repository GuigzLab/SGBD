package up.mi.sgbdr;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class Record {

    private final RelationInfo relationInfo;
    ArrayList<String> values = new ArrayList<>();

    public Record(RelationInfo relationInfo) {
        this.relationInfo = relationInfo;
    }

    /**
     * Ecrit les valeurs {@param values} du Record dans le buffer, l’une après l’autre, à partir
     * de position
     *
     * @param buff
     * @param pos
     */
    public void writeToBuffer(byte[] buff, int pos) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(buff);
        // TODO - Pos comprise dans le buffer
        byteBuffer.position(pos);
        for (int i = 0; i < this.relationInfo.getCols(); i++) {
            switch (this.relationInfo.getColTypes().get(i)) {
                case "int": {
                    byteBuffer.putInt(Integer.parseInt(this.values.get(i)));
                    break;
                }
                case "float": {
                    byteBuffer.putFloat(Float.parseFloat(this.values.get(i)));
                    break;
                }
                default: {
                    char[] chars = this.values.get(i).toCharArray();
                    for (char c : chars) {
                        byteBuffer.putChar(c);
                    }
                    break;
                }
            }
        }

        byteBuffer.flip();
        byteBuffer.get(buff, 0, byteBuffer.remaining());

    }

    /**
     * Lit les valeurs {@param values} du Record depuis le buffer, l’une après l’autre, à partir
     * de position.
     *
     * @param buff
     * @param pos
     */
    public void readFromBuffer(byte[] buff, int pos) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(buff);
        // TODO - Pos comprise dans le buffer
        byteBuffer.position(pos);

        for (int i = 0; i < this.relationInfo.getCols(); i++) {
            switch (this.relationInfo.getColTypes().get(i)) {
                case "int": {
                    this.values.add(String.valueOf(byteBuffer.getInt()));
                    break;
                }
                case "float": {
                    this.values.add(String.valueOf(byteBuffer.getFloat()));
                    break;
                }
                default: {
                    int length = Integer.parseInt(String.valueOf(this.relationInfo.getColTypes().get(i).charAt(7)));
                    char[] chars = new char[length];
                    for (int j = 0; j < chars.length; j++) {
                        chars[j] = byteBuffer.getChar();
                    }
                    this.values.add(String.copyValueOf(chars));
                    break;
                }
            }
        }


    }

    public ArrayList<String> getValues() {
        return values;
    }

    public void setValues(ArrayList<String> values) {
        this.values = values;
    }
}
