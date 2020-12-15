package up.mi.sgbdr;

import java.time.LocalDateTime;

public class Frame implements Comparable<Frame> {
    private PageID pageID;
    public byte[] buffer;
    private int pinCount;
    private boolean dirty;
    private LocalDateTime unpinned;

    public Frame() {
        this.resetFrame();
    }

    public PageID getPageID() {
        return pageID;
    }

    public void setPageID(PageID pageID) {
        this.pageID = pageID;
    }

    public boolean isDirty() {
        return dirty;
    }

    public void setDirty(boolean dirty) {
        if (!this.dirty)
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

    @Override
    public int compareTo(Frame o) {
        return getUnpinned().compareTo(o.getUnpinned());
    }

    public void resetFrame() {
        this.pageID = null;
        this.buffer = new byte[DBParams.pageSize];
        this.pinCount = 0;
        this.dirty = false;
        this.unpinned = LocalDateTime.now().plusYears(1);
    }

    public void incrementPinCount() {
        this.pinCount++;
    }
}
