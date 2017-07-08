package com.example.zhou.coolweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Created by zhou on 2017/7/5.
 */

public class Now {

    @SerializedName("tmp")
    public String temperature;

    @SerializedName("cond")
    public More more;
    public class More{

        @SerializedName("txt")
        public String info;
    }
}
