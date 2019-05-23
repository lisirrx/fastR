package me.lisirrx.fastR.example;

import java.io.Serializable;

/**
 * @author lihan lisirrx@gmail.com
 * @date 2019/5/23
 */
public class People implements Serializable {
    String name;

    public People(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
