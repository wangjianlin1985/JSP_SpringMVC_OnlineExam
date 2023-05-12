// 
// 
// 

package exam.model.page;

import java.util.List;

public class PageBean<T>
{
    private List<T> records;
    private int pageSize;
    private int currentPage;
    private int recordCount;
    private int pageNumber;
    private int pageBeginIndex;
    private int pageEndIndex;
    private int pageCount;
    
    public PageBean(final List<T> records, final int pageSize, final int currentPage, final int recordCount, final int pageNumber) {
        this.pageNumber = ((pageNumber < 2) ? 10 : pageNumber);
        this.records = records;
        this.pageSize = pageSize;
        this.currentPage = currentPage;
        this.recordCount = recordCount;
        this.count();
    }
    
    private void count() {
        this.pageCount = (this.recordCount + this.pageSize - 1) / this.pageSize;
        if (this.pageCount <= this.pageNumber) {
            this.pageBeginIndex = 1;
            this.pageEndIndex = this.pageCount;
        }
        else {
            int pre = 0;
            int rear = 0;
            if (this.pageNumber % 2 == 0) {
                pre = this.pageNumber >> 0;
                rear = this.pageNumber >> 1;
            }
            else {
                pre = this.pageNumber >> 1;
                rear = this.pageNumber >> 1;
            }
            this.pageBeginIndex = this.currentPage - pre;
            this.pageEndIndex = this.currentPage + rear;
            if (this.pageBeginIndex < 1) {
                this.pageBeginIndex = 1;
                this.pageEndIndex = this.pageNumber;
            }
            else if (this.pageEndIndex > this.pageCount) {
                this.pageEndIndex = this.pageCount;
                this.pageBeginIndex = this.pageEndIndex - this.pageNumber + 1;
            }
        }
    }
    
    public List<T> getRecords() {
        return this.records;
    }
    
    public void setRecords(final List<T> records) {
        this.records = records;
    }
    
    public int getPageSize() {
        return this.pageSize;
    }
    
    public void setPageSize(final int pageSize) {
        this.pageSize = pageSize;
    }
    
    public int getPageCount() {
        return this.pageCount;
    }
    
    public void setPageCount(final int pageCount) {
        this.pageCount = pageCount;
    }
    
    public int getPageBeginIndex() {
        return this.pageBeginIndex;
    }
    
    public void setPageBeginIndex(final int pageBeginIndex) {
        this.pageBeginIndex = pageBeginIndex;
    }
    
    public int getPageEndIndex() {
        return this.pageEndIndex;
    }
    
    public void setPageEndIndex(final int pageEndIndex) {
        this.pageEndIndex = pageEndIndex;
    }
    
    public int getCurrentPage() {
        return this.currentPage;
    }
    
    public void setCurrentPage(final int currentPage) {
        this.currentPage = currentPage;
    }
    
    public int getRecordCount() {
        return this.recordCount;
    }
    
    public void setRecordCount(final int recordCount) {
        this.recordCount = recordCount;
    }
}
