package up.mi.sgbdr;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

public class DiskManager {

    private static DiskManager INSTANCE;
    private String info = "Disk Manager";

    private DiskManager() {
    }

    public static DiskManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DiskManager();
        }

        return INSTANCE;
    }

    /**
     * Crée dans le sous dossier DBParams.DBPath un fichier Data_fileIdx.rf vide
     *
     * @param fileIdx - Entier >= 0 qui est l'identifiant du fichier
     */

    public void CreateFile(int fileIdx) {

        try {
            RandomAccessFile r = new RandomAccessFile(DBParams.DBPath + "Data_" + fileIdx + ".rf", "rw");
            r.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * Rajoute une page au fichier spécifié (rajoute DBParams.pageSize octets à la fin du fichier)
     *
     * @param fileIdx - Identifiant du fichier
     * @return PageID
     */
    public PageID AddPage(int fileIdx) {

        //FIXME
        // - byte[] buff to ByteBuffer buff

        byte[] byteBuffer = new byte[DBParams.pageSize];

        int pageId = 0;
        try {
            RandomAccessFile file = new RandomAccessFile(DBParams.DBPath + "Data_" + fileIdx + ".rf", "rw");
            file.seek(file.length());


            for (int i = 0; i < DBParams.pageSize; i++) {
                file.writeByte(byteBuffer[i]);
            }

            pageId = (int) (file.length() / 4096 - 1);
            file.close();


        } catch (IOException e) {
            e.printStackTrace();
        }
        return new PageID(fileIdx, pageId);
    }

    /**
     * Remplie l'argument buff avec le contenu du disque de la page identifiée par l'argument pageId
     *
     * @param pageId - Identifiant de la page
     * @param buff   - Buffer
     */

    public void ReadPage(PageID pageId, byte[] buff) {

        String path = DBParams.DBPath + "Data_" + pageId.getFileIdx() + ".rf";
        RandomAccessFile file;

        try {
            file = new RandomAccessFile(path, "r");
            long pos = pageId.getPageIdx() * DBParams.pageSize;
            byte[] array = new byte[DBParams.pageSize];

            file.seek(pos);

            for (int i = 0; i < DBParams.pageSize; i++) {
//                array[i] = file.readByte();
                buff[i] = file.readByte();
                pos++;
                file.seek(pos);
            }
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


//            buff.wrap(array);

    }

    /**
     * Ecrit le contenu de l'argument buff dans le fichier et dans la page indiquée par pageId
     *
     * @param pageId - Identifiant de la page
     * @param buff   - Buffer
     */

    public void WritePage(PageID pageId, byte[] buff) {

        //TODO Exception si le buffer est plus gros que la taille d'une page

        String path = DBParams.DBPath + "Data_" + pageId.getFileIdx() + ".rf";
        RandomAccessFile file;

        try {
            file = new RandomAccessFile(path, "rw");
            int pos = DBParams.pageSize * (pageId.getPageIdx());

            if (pageId.getPageIdx() != 0) {
                file.seek(pos);
            }
            file.write(buff, 0, DBParams.pageSize);

            file.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
