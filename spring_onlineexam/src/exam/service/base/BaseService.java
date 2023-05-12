// 
// 
// 

package exam.service.base;

import exam.model.page.PageBean;
import java.util.HashMap;
import java.util.List;

public interface BaseService<T>
{
    List<T> findAll();
    
    void saveOrUpdate(T p0);
    
    void delete(Object p0);
    
    List<T> find(T p0);
    
    PageBean<T> pageSearch(int p0, int p1, int p2, String p3, List<Object> p4, HashMap<String, String> p5);
}
