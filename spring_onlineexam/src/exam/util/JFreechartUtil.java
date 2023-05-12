// 
// 
// 

package exam.util;

import java.io.IOException;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartUtilities;
import java.io.File;
import java.awt.Paint;
import java.awt.SystemColor;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import org.jfree.chart.title.TextTitle;
import org.jfree.chart.plot.PiePlot;
import java.awt.Font;
import org.jfree.data.general.PieDataset;
import org.jfree.chart.ChartFactory;
import org.jfree.data.general.DefaultPieDataset;
import exam.dto.StatisticsData;

public abstract class JFreechartUtil
{
    public static void generateChart(final StatisticsData data, final String path) throws IOException {
        final DefaultPieDataset dataSet = new DefaultPieDataset();
        dataSet.setValue((Comparable)"<60%", (double)data.getUnderSixty().size());
        dataSet.setValue((Comparable)"60%-80%", (double)data.getSixtyAndEighty().size());
        dataSet.setValue((Comparable)"80%-90%", (double)data.getEightyAndNinety().size());
        dataSet.setValue((Comparable)">90%", (double)data.getAboveNinety().size());
        final JFreeChart pie = ChartFactory.createPieChart("\u5404\u5206\u6570\u6bb5\u5b66\u751f\u4eba\u6570\u7edf\u8ba1", (PieDataset)dataSet, true, true, false);
        final Font font = new Font("\u5fae\u8f6f\u96c5\u9ed1", 0, 12);
        final PiePlot plot = (PiePlot)pie.getPlot();
        pie.setTitle(new TextTitle("\u5404\u5206\u6570\u6bb5\u5b66\u751f\u4eba\u6570\u7edf\u8ba1", font));
        plot.setLabelFont(font);
        pie.getLegend().setItemFont(font);
        plot.setNoDataMessage("\u6ca1\u6709\u6570\u636e");
        plot.setLabelGenerator((PieSectionLabelGenerator)new StandardPieSectionLabelGenerator("{0}={1}({2})", NumberFormat.getNumberInstance(), (NumberFormat)new DecimalFormat("0.00%")));
        plot.setLegendLabelGenerator((PieSectionLabelGenerator)new StandardPieSectionLabelGenerator("{0}={1}({2})"));
        plot.setBackgroundPaint((Paint)SystemColor.WHITE);
        ChartUtilities.saveChartAsPNG(new File(path), pie, 500, 500);
    }
}
