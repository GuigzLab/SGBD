package up.mi.sgbdr;

public class Rid {
    private PageID pageID;
    private int slotIdx;

    public Rid(PageID pageID, int slotIdx) {
        this.pageID = pageID;
        this.slotIdx = slotIdx;
    }

    public int getSlotIdx() {
        return slotIdx;
    }

    public PageID getPageID() {
        return pageID;
    }
}
