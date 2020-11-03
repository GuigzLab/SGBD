package up.mi.sgbdr;

import java.time.LocalDateTime;

public class Frame implements Comparable<Frame>{
    private PageID pageID;
    public byte[] buffer = new byte[DBParams.pageSize];
    private int pinCount = 0;
    private int dirty = 0;
    private LocalDateTime unpinned = LocalDateTime.now().plusYears(1);

//    public Frame (PageID pageID) {
//        this.pageID = pageID;
//    }

    public PageID getPageID() {
        return pageID;
    }

    public void setPageID(PageID pageID) {
        this.pageID = pageID;
    }

    public int getDirty() {
        return dirty;
    }

    public void setDirty(int dirty) {
        this.dirty = dirty;
    }

    public int getPinCount() {
        return pinCount;
    }

    public void setPinCount(int pinCount) {
        this.pinCount = pinCount;
    }

    public LocalDateTime getUnpinned() {
        return unpinned;
    }

    public void setUnpinned(LocalDateTime unpinned) {
        this.unpinned = unpinned;
    }

//    public byte[] getBuffer() {
//        return buffer;
//    }
//
//    public void setBuffer(byte[] buffer) {
//        this.buffer = buffer;
//    }

    @Override
    public int compareTo(Frame o) {
        return getUnpinned().compareTo(o.getUnpinned());
    }
}
