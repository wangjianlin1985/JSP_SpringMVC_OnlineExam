// 
// 
// 

package exam.dto;

import java.util.Collection;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

public class StatisticsData
{
    private String title;
    private int personCount;
    private int examPoints;
    private int sixtyPoint;
    private int eighttyPoint;
    private int ninetyPoint;
    private List<String> highestNames;
    private int highestPoint;
    private List<String> lowestNames;
    private int lowestPoint;
    private List<Integer> underSixty;
    private List<Integer> sixtyAndEighty;
    private List<Integer> eightyAndNinety;
    private List<Integer> aboveNinety;
    
    public StatisticsData() {
        this.highestNames = new ArrayList<String>();
        this.lowestNames = new ArrayList<String>();
        this.underSixty = new ArrayList<Integer>();
        this.sixtyAndEighty = new ArrayList<Integer>();
        this.eightyAndNinety = new ArrayList<Integer>();
        this.aboveNinety = new ArrayList<Integer>();
    }
    
    public String getTitle() {
        return this.title;
    }
    
    public void setTitle(final String title) {
        this.title = title;
    }
    
    public int getPersonCount() {
        return this.personCount;
    }
    
    public void setPersonCount(final int personCount) {
        this.personCount = personCount;
    }
    
    public int getSixtyPoint() {
        return this.sixtyPoint;
    }
    
    public void setSixtyPoint(final int sixtyPoint) {
        this.sixtyPoint = sixtyPoint;
    }
    
    public int getEighttyPoint() {
        return this.eighttyPoint;
    }
    
    public void setEighttyPoint(final int eighttyPoint) {
        this.eighttyPoint = eighttyPoint;
    }
    
    public int getNinetyPoint() {
        return this.ninetyPoint;
    }
    
    public void setNinetyPoint(final int ninetyPoint) {
        this.ninetyPoint = ninetyPoint;
    }
    
    public int getExamPoints() {
        return this.examPoints;
    }
    
    public void setExamPoints(final int examPoints) {
        this.examPoints = examPoints;
    }
    
    public List<String> getHighestNames() {
        return Collections.unmodifiableList((List<? extends String>)this.highestNames);
    }
    
    public void addHightestName(final List<String> names) {
        this.highestNames.addAll(names);
    }
    
    public int getHighestPoint() {
        return this.highestPoint;
    }
    
    public void setHighestPoint(final int highestPoint) {
        this.highestPoint = highestPoint;
    }
    
    public List<String> getLowestNames() {
        return Collections.unmodifiableList((List<? extends String>)this.lowestNames);
    }
    
    public void addLowestNames(final List<String> names) {
        this.lowestNames.addAll(names);
    }
    
    public int getLowestPoint() {
        return this.lowestPoint;
    }
    
    public void setLowestPoint(final int lowestPoint) {
        this.lowestPoint = lowestPoint;
    }
    
    public List<Integer> getUnderSixty() {
        return Collections.unmodifiableList((List<? extends Integer>)this.underSixty);
    }
    
    public void addUnderSixty(final int point) {
        this.underSixty.add(point);
    }
    
    public List<Integer> getSixtyAndEighty() {
        return Collections.unmodifiableList((List<? extends Integer>)this.sixtyAndEighty);
    }
    
    public void addSixtyAndEighty(final int point) {
        this.sixtyAndEighty.add(point);
    }
    
    public List<Integer> getEightyAndNinety() {
        return Collections.unmodifiableList((List<? extends Integer>)this.eightyAndNinety);
    }
    
    public void addEightyAndNinety(final int point) {
        this.eightyAndNinety.add(point);
    }
    
    public List<Integer> getAboveNinety() {
        return Collections.unmodifiableList((List<? extends Integer>)this.aboveNinety);
    }
    
    public void addAboveNinety(final int point) {
        this.aboveNinety.add(point);
    }
}
