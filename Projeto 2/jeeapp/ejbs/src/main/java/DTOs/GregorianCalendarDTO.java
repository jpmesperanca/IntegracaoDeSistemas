package DTOs;

import java.io.Serializable;

public class GregorianCalendarDTO implements Serializable {

    private Integer year;
    private Integer month;
    private Integer day;

    public GregorianCalendarDTO() {
    }

    public GregorianCalendarDTO(Integer year, Integer month, Integer day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public Integer getYear() {
        return year;
    }

    public Integer getMonth() {
        return month;
    }

    public Integer getDay() {
        return day;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public void setDay(Integer day) {
        this.day = day;
    }
}
