package up.mi.sgbdr.tests;

import up.mi.sgbdr.DBParams;
import up.mi.sgbdr.DiskManager;
import up.mi.sgbdr.PageID;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class DiskManagerTest {
    public static void main(String[] args) {
        DBParams.DBPath = "DB/";
        DBParams.pageSize = 4096;

        DiskManager DM = DiskManager.getInstance();

        DM.CreateFile(1);

        DM.AddPage(1);
        DM.AddPage(1);
        DM.AddPage(1);
        DM.AddPage(1);
        DM.AddPage(1);
        /*PageID pageID = DM.AddPage(1);

        System.out.println("FileID: " + pageID.getFileIdx());
        System.out.println("PageID: " + pageID.getPageIdx());*/

//        ByteBuffer byteBuffer = ByteBuffer.allocate(DBParams.pageSize);
        byte[] byteBuffer = new byte[DBParams.pageSize];

//        DM.ReadPage(pageID, byteBuffer);

        byteBuffer[1] = 49;
        byteBuffer[2] = 32;
        byteBuffer[0] = 33;

        PageID page2 = new PageID(1,0);

        DM.WritePage(page2, byteBuffer);

    }
}
