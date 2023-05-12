// 
// 
// 

package exam.dao.base;

import java.sql.SQLException;
import java.sql.PreparedStatement;

public interface GenerateKeyCallback
{
    void setParameters(PreparedStatement p0, Object p1) throws SQLException;
}
