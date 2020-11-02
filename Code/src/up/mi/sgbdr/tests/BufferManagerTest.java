package up.mi.sgbdr.tests;

import up.mi.sgbdr.BufferManager;
import up.mi.sgbdr.DBParams;
import up.mi.sgbdr.DiskManager;
import up.mi.sgbdr.PageID;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class BufferManagerTest {
    public static void main(String[] args) throws InterruptedException {
        DBParams.DBPath = "DB/";
        DBParams.pageSize = 4096;
        DBParams.frameCount = 2;

        DiskManager DM = DiskManager.getInstance();
        BufferManager BM = BufferManager.getInstance();

        PageID page = new PageID(1,0);
        PageID page2 = new PageID(1,1);
        PageID page3 = new PageID(1,2);
        System.out.println("GETPAGE 1 --------------");
        byte[] byteBuffer = BM.GetPage(page);
        System.out.println("GETPAGE 2 --------------");
        byte[] byteBuffer2 = BM.GetPage(page2);

        System.out.println("FREEPAGE 2 --------------");
        BM.FreePage(page2, 1);

        Thread.sleep(4000);

        System.out.println("FREEPAGE 1 --------------");
        BM.FreePage(page, 1);


        System.out.println("GETPAGE 3 --------------");
        byteBuffer = BM.GetPage(page3);

    }
}
