package com.tomlezmy.goolmathapp.model;

import java.util.List;

public enum EVideoIds {
    ADDITION(new String[] {"mNfOSN6WmOc"}), SUBTRACTION(new String[] {}),
    MULTIPLICATION(new String[] {"JDEGZQbF9sY", "xdX49SjS_Y0", "kg-9riR93lE", "JxMNGksh92o"}),
    DIVISION(new String[] {"AGuYqyz6bo0"}),
    FRACTIONS(new String[] {"aHKtdgt0yK0", "QAs3XbFB8ok", "ZMebVEgW5HQ"}), PERCENTS(new String[] {"5mTvhp6vcsg"}),
    DECIMALS(new String[] {"Dkd5Kxom7oA","ONofLBjr4i8","FKDs66dWQjs"});

    private String[] videoIds;

    EVideoIds(String[] videoIds) {this.videoIds = videoIds;}

    public String[] getAllVideoIds() {return  videoIds;}
    public String getVideoId(int index) {return videoIds[index];}
}
