package data;

import java.io.Serializable;

public class ManagerInfo implements Serializable {

    private String name;
    private int id;

    public ManagerInfo() {
    }

    public ManagerInfo(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }
}