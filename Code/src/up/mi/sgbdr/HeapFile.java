package up.mi.sgbdr;

import java.nio.ByteBuffer;
import java.util.ArrayList;

public class HeapFile {
    private RelationInfo relationInfo;

    public HeapFile(RelationInfo relationInfo) {
        this.relationInfo = relationInfo;
    }

    public void createNewOnDisk() {
        int fileIdx = this.relationInfo.getFileIdx();
        DiskManager.getInstance().CreateFile(fileIdx);

        PageID headerPage = DiskManager.getInstance().AddPage(fileIdx);

        byte[] buff = BufferManager.getInstance().GetPage(headerPage);

        ByteBuffer byteBuffer = ByteBuffer.wrap(buff);
        byteBuffer.position(0);
        for (int i = 0; i < DBParams.pageSize / 4; i++) {
            byteBuffer.putInt(0);
        }
        byteBuffer.flip();
        byteBuffer.get(buff, 0, byteBuffer.remaining());

        BufferManager.getInstance().FreePage(headerPage, true);
    }

    /**
     * Cette méthode rajoute une page de données vierge et actualise les infos de la Header Page;
     *
     * @return PageID de la data page nouvellement créée
     */
    public PageID addDataPage() {

        int fileIdx = this.relationInfo.getFileIdx();
        PageID pageID = DiskManager.getInstance().AddPage(fileIdx);

        PageID headerPage = new PageID(fileIdx, 0);

        byte[] headerPageBuffer = BufferManager.getInstance().GetPage(headerPage);

        ByteBuffer byteBuffer = ByteBuffer.wrap(headerPageBuffer);

        byteBuffer.position(0);
        // On actualise le nombre de DataPages
        byteBuffer.putInt(pageID.getPageIdx());

        int slotCount = relationInfo.getSlotCount();

        byteBuffer.position(pageID.getPageIdx() * 4);
        byteBuffer.putInt(slotCount);

        byteBuffer.flip();
        byteBuffer.get(headerPageBuffer, 0, byteBuffer.remaining());

        BufferManager.getInstance().FreePage(headerPage, true);

        return pageID;
    }

    public PageID getFreeDataPageId() {
        /*Cette méthode doit retourner le PageId d’une page de données sur laquelle il reste des cases libres.
                Pour cela, il faudra simplement lire les informations de la Header Page.
                N’oubliez pas d’obtenir la Header Page via le BufferManager, et de la libérer après lecture.*/

        int fileIdx = this.relationInfo.getFileIdx();
        PageID headerPage = new PageID(fileIdx, 0);
        byte[] headerPageBuffer = BufferManager.getInstance().GetPage(headerPage);

        ByteBuffer byteBuffer = ByteBuffer.wrap(headerPageBuffer);
        byteBuffer.position(0);
        int nbDataPages = byteBuffer.getInt();

        int pos;
        for (int i = 1; i <= nbDataPages; i++) {
            pos = 4 * i;
            if (byteBuffer.getInt(pos) > 0) {
                return new PageID(fileIdx, i);
            }
        }

        return null;
    }

    public Rid writeRecordToDataPage(Record record, PageID pageID) {

        /*Cette méthode doit écrire l’enregistrement record dans la page de données identifiée par pageId, et
        renvoyer son Rid !*/
        //Record.WriteToBuffer() slot

        int pos;

        byte[] dataPageBuffer = BufferManager.getInstance().GetPage(pageID);
        for (int i = 0; i < relationInfo.getSlotCount(); i++) {
            if (dataPageBuffer[i*4 + 3] == 0){
                dataPageBuffer[i*4 + 3] = 1;
                //TODO - Ecrire le record
                System.out.println(i);
                pos = (relationInfo.getSlotCount() * 4) + (i * relationInfo.getRecordSize());
                System.out.println(pos);
                record.writeToBuffer(dataPageBuffer, pos);
                BufferManager.getInstance().FreePage(pageID, true);

                return new Rid(pageID, i);
            }
        }

        return null;
    }

    public ArrayList<Record> getRecordsInDataPage (PageID pageID) {

        return null;
    }

}
