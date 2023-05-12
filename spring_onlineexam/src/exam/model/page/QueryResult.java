// 
// 
// 

package exam.model.page;

import java.util.List;

public class QueryResult<T>
{
    private List<T> records;
    private int recordCount;
    
    public QueryResult(final List<T> records, final int recordCount) {
        this.records = records;
        this.recordCount = recordCount;
    }
    
    public QueryResult() {
    }
    
    public List<T> getRecords() {
        return this.records;
    }
    
    public void setRecords(final List<T> records) {
        this.records = records;
    }
    
    public int getRecordCount() {
        return this.recordCount;
    }
    
    public void setRecordCount(final int recordCount) {
        this.recordCount = recordCount;
    }
}
