// 
// 
// 

package exam.service;

import exam.model.role.Manager;

public interface ManagerService
{
    Manager login(String p0, String p1);
    
    void updatePassword(int p0, String p1);
}
