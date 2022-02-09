package com.github.peixunxposed.modle;

public class Peixun {
    private String name;
    private String type;

    public String getName() {
        return this.name;
    }

    public String getType() {
        return this.type;
    }

    public void setName(String str) {
        this.name = str;
    }

    public void setType(String str) {
        this.type = str;
    }

    public String toString() {
        return "Peixun{name='" + this.name + "', type='" + this.type + "'}";
    }

}
