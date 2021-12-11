package data;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Results implements Serializable {

    @Id
    private String topic;
    private Double value;

    public Results() {
    }

    public Results(String topic, Double value) {
        this.topic = topic;
        this.setValue(value);
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