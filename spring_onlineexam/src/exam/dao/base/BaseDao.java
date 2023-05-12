// 
// 
// 

package exam.dao.base;

import exam.model.page.PageBean;
import java.util.HashMap;
import org.springframework.jdbc.core.RowMapper;
import java.util.List;

public interface BaseDao<T>
{
    void executeSql(String p0, Object[] p1);
    
    List<T> find(T p0);
    
    void executeSql(String p0);
    
    List<T> queryBySQL(String p0);
    
    List<T> queryBySQL(String p0, Object... p1);
    
    Object queryForObject(String p0, Class<?> p1);
    
    int[] batchUpdate(String... p0);
    
    int getKeyHelper(String p0, GenerateKeyCallback p1, Object p2);
    
    String getCountSql();
    
    String getSql();
    
    RowMapper<T> getRowMapper();
    
    PageBean<T> pageSearch(int p0, int p1, int p2, String p3, List<Object> p4, HashMap<String, String> p5);
}
