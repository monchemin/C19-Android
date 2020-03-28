package com.digitalink.c19.utils;

import com.digitalink.c19.BuildConfig;

public class Configuration {

    public static String apiHost() {

        switch (BuildConfig.FLAVOR) {
            default:
                return "http://ec2-34-219-177-100.us-west-2.compute.amazonaws.com:8080";
        }
    }
}
