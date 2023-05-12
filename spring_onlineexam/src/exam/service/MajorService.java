// 
// 
// 

package exam.service;

import java.util.List;
import exam.model.Major;
import exam.service.base.BaseService;

public interface MajorService extends BaseService<Major>
{
    Major findByName(String p0);
    
    List<Major> findByGrade(int p0);
    
    List<Major> findAll();
}
