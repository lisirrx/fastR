package me.lisirrx.fastR.serialization;

import java.io.Serializable;

/**
 * @author lihan lisirrx@gmail.com
 * @date 2019/5/19
 */

class TestData implements Serializable {
    String inner;

    public TestData(String inner) {
        this.inner = inner;
    }

    public String getInner() {
        return inner;
    }

    public void setInner(String inner) {
        this.inner = inner;
    }
}