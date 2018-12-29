package com.github.adizbek.sper;

import android.content.Context;
import android.content.SharedPreferences;
import com.github.adizbek.sper.helper.login.ILogin;

import java.util.HashMap;

public class LoginHelper {
    private static LoginHelper instance;
    private SharedPreferences preferences;
    private HashMap<String, String> cache;
    private ILogin loginHandler;

    static void init(Context context, ILogin loginHandler) {
        instance = new LoginHelper(context, loginHandler);
    }

    private LoginHelper(Context context, ILogin loginHandler) {
        preferences = context.getSharedPreferences("login", Context.MODE_PRIVATE);
        this.loginHandler = loginHandler;
        cache = new HashMap<>();
    }

    public LoginHelper write(String key, String value) {
        preferences.edit().putString(key, value).apply();
        cache.put(key, value);

        return this;
    }

    public String read(String key) {
        if (cache.keySet().contains(key)) {
            return cache.get(key);
        }

        return preferences.getString(key, "");
    }

    public void remove(String... keys) {
        SharedPreferences.Editor editor = preferences.edit();

        for (String key : keys) {
            editor.remove(key);
            cache.remove(key);
        }

        editor.apply();
    }

    public static void login(String... params) {
        instance.loginHandler.login(instance, params);
        System.out.println("Saved credentials");
    }

    public static void logout() {
        instance.loginHandler.logout(instance);
    }

    public static boolean isLoggedIn() {
        boolean status = instance.loginHandler.isLoggedIn(instance);
        System.out.println(String.format("Login status %s", status));

        return status;
    }

    public static String getCredintals(String key) {
        return instance.read(key);
    }
}
