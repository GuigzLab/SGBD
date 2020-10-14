package up.mi.sgbdr;

public class PageID {

    private int FileIdx;
    private int PageIdx;

    public PageID(int FileIdx, int PageIdx) {
        this.FileIdx = FileIdx;
        this.PageIdx = PageIdx;
    }

    public int getFileIdx() {
        return this.FileIdx;
    }

    public int getPageIdx() {
        return this.PageIdx;
    }


}
