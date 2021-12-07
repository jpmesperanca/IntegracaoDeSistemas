package data;

import java.io.Serializable;

public class ManagerInfo implements Serializable {

    private String name;

    public ManagerInfo() {
    }

    public ManagerInfo(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}