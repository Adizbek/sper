package com.github.adizbek.sper;

import android.content.res.Resources;
import android.os.Handler;
import android.support.multidex.MultiDexApplication;

import com.github.adizbek.sper.helper.Helper;

public class BaseApplication extends MultiDexApplication {
    public static Handler handler = new Handler();

    public static BaseApplication c;
    public static Resources r;

    @Override
    public void onCreate() {
        super.onCreate();

        Helper.INSTANCE.loadSavedLang();
    }
}
