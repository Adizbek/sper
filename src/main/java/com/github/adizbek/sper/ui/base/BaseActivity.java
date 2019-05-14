package com.github.adizbek.sper.ui.base;

import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;

import com.github.adizbek.sper.BaseApplication;

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(BaseApplication.localeManager.setLocale(base));
    }

}
