package com.src.wugang;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by lwg on 2016/6/1.
 */
public class Translate {
    public String query;
    public int errorCode;
    public List<Web> web;

    public class Web {
        public String key;
        public List<String> value;

    }

    public List<String> translation;

    public Basic basic;

    public class Basic {
        @SerializedName("us-phonetic")
        public String us_phonetic;
        public String phonetic;
        @SerializedName("uk-phonetic")
        public String uk_phonetic;
        public List<String> explains;

    }
}
