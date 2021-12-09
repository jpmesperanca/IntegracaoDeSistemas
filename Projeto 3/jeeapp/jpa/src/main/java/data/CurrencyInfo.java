package data;

import java.io.Serializable;

public class CurrencyInfo implements Serializable {

    private String name;
    private Double conversionRate;

    public CurrencyInfo() {
    }

    public CurrencyInfo(String name, Double conversionRate) {
        this.name = name;
        this.conversionRate = conversionRate;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getConversionRate() {
        return this.conversionRate;
    }

    public void setConversionRate(Double conversionRate) {
        this.conversionRate = conversionRate;
    }

}