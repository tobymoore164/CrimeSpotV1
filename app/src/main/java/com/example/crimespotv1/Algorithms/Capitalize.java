package com.example.crimespotv1.Algorithms;

public class Capitalize {

    public String getCapitalized(String string) {
        String mString = string.substring(0,1).toUpperCase() + string.substring(1).toLowerCase();
        return mString;
    }
}
