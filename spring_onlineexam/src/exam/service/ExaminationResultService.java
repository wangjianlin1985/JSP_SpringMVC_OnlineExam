// 
// 
// 

package exam.service;

import exam.dto.StudentReport;
import java.util.List;
import exam.dto.StatisticsData;
import exam.dto.ERView;
import exam.model.ExaminationResult;
import exam.service.base.BaseService;

public interface ExaminationResultService extends BaseService<ExaminationResult>
{
    ERView getViewById(int p0);
    
    StatisticsData getStatisticsData(int p0);
    
    List<StudentReport> getReportData(int p0);
}
