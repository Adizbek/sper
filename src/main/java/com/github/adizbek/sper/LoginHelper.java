package com.github.adizbek.sper;

import android.content.Context;
import android.content.SharedPreferences;
import com.github.adizbek.sper.helper.login.ILogin;

public class LoginHelper {
    private static LoginHelper instance;
    private SharedPreferences preferences;
    private ILogin loginHandler;

    static void init(Context context, ILogin loginHandler) {
        instance = new LoginHelper(context, loginHandler);
    }

    private LoginHelper(Context context, ILogin loginHandler) {
        preferences = context.getSharedPreferences("login", Context.MODE_PRIVATE);
        this.loginHandler = loginHandler;
    }

    public LoginHelper write(String key, String value) {
        preferences.edit().putString(key, value).apply();

        return this;
    }

    public String read(String key) {
        return preferences.getString(key, "");
    }

    public void remove(String... keys) {
        SharedPreferences.Editor editor = preferences.edit();

        for (String key : keys) {
            editor.remove(key);
        }

        editor.apply();
    }

    public static void login(String... params) {
        instance.loginHandler.login(instance, params);
    }

    public static void logout() {
        instance.loginHandler.logout(instance);
    }

    public static boolean isLoggedIn() {
        return instance.loginHandler.isLoggedIn(instance);
    }
}
