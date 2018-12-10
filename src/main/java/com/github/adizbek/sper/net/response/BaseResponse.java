package com.github.adizbek.sper.net.response;

import java.util.ArrayList;

public class BaseResponse<T> {
    public boolean success;
    public String reason;

    public ArrayList<T> data;
}
