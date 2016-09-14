package com.tonsstudio.bunk;

import java.io.Serializable;

/**
 * Created by tommyjepsen on 14/09/16.
 */
public class LoopSequence implements Serializable {
    public int id;
    public int nr;
    public int time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getNr() {
        return nr;
    }

    public void setNr(int nr) {
        this.nr = nr;
    }
}
