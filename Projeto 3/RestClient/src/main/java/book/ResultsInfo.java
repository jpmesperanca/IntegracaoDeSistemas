package book;

import java.io.Serializable;

public class ResultsInfo implements Serializable {

    private String topic;
    private Double value;

    public ResultsInfo() {
    }

    public ResultsInfo(String topic, Double value) {
        this.topic = topic;
        this.value = value;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}