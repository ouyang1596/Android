package com.mosheng.common.view.zhenview;

import java.io.Serializable;

/**
 * Created by Ryan on 2017/5/22.
 * 每帧动画的宽高以及位置
 */
public class Frame implements Serializable {
    private int x;
    private int y;
    private int w;
    private int h;

    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }


    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

}
