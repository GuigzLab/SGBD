package up.mi.sgbdr;

public class BufferManager {

    private static BufferManager INSTANCE;
    private String info = "Disk Manager";

    private BufferManager() {
    }

    public static BufferManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new BufferManager();
        }

        return INSTANCE;
    }

    public byte[] GetPage(PageID pageID) {
        return new byte[]{0};
    }

    public void FreePage(PageID pageID, int dirty) {

    }

    public void FlushAll() {

    }

}
