package com.github.adizbek.sper;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Handler;
import androidx.multidex.MultiDexApplication;
import android.util.Log;

import com.github.adizbek.sper.helper.Helper;
import com.github.adizbek.sper.helper.LocaleManager;

public class BaseApplication extends MultiDexApplication {
    public static Handler handler = new Handler();

    public static BaseApplication c;
    public static Resources r;
    public static LocaleManager localeManager;


    @Override
    public void onCreate() {
        super.onCreate();

        c = this;
        Helper.INSTANCE.loadSavedLang();
    }

    @Override
    protected void attachBaseContext(Context base) {
        localeManager = new LocaleManager(base);
        super.attachBaseContext(localeManager.setLocale(base));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        localeManager.setLocale(this);
        Log.d("LOCALE", "onConfigurationChanged: " + newConfig.locale.getLanguage());
    }
}
