// 
// 
// 

package exam.util;

import java.io.IOException;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.ByteArrayOutputStream;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import java.io.FileInputStream;
import java.io.File;
import java.io.InputStream;
import exam.dto.StudentReport;
import java.util.List;

public abstract class ExcelUtil
{
    public static InputStream generateExcel(final List<StudentReport> reportData, final String path) throws IOException {
        final File file = new File(path);
        if (file.exists()) {
            return new FileInputStream(file);
        }
        final HSSFWorkbook wb = new HSSFWorkbook();
        final HSSFSheet sheet = wb.createSheet(String.valueOf(reportData.get(0).getTitle()) + "\u6210\u7ee9\u5355");
        HSSFRow row = sheet.createRow(0);
        final HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment((short)2);
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("\u5b66\u53f7");
        cell.setCellStyle(style);
        cell = row.createCell(1);
        cell.setCellValue("\u59d3\u540d");
        cell.setCellStyle(style);
        cell = row.createCell(2);
        cell.setCellValue("\u5206\u6570");
        cell.setCellStyle(style);
        StudentReport data = null;
        for (int i = 0, l = reportData.size(); i < l; ++i) {
            data = reportData.get(0);
            row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(data.getSid());
            row.createCell(1).setCellValue(data.getName());
            row.createCell(2).setCellValue((double)data.getPoint());
        }
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final FileOutputStream fos = new FileOutputStream(file);
        wb.write((OutputStream)baos);
        wb.write((OutputStream)fos);
        wb.close();
        return new ByteArrayInputStream(baos.toByteArray());
    }
}
