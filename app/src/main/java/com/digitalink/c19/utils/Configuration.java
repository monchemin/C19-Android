package com.digitalink.c19.utils;

import com.digitalink.c19.BuildConfig;

public class Configuration {

    public static String apiHost() {

        switch (BuildConfig.FLAVOR) {
            default:
                return "http://3.217.233.250:8080";
        }
    }
}
