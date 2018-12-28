package com.github.adizbek.sper.helper.login;

import com.github.adizbek.sper.LoginHelper;

public class BaseLoginHandler implements ILogin {

    private static final String KEY_LOGIN = "login";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_ID = "id";

    @Override
    public boolean isLoggedIn(LoginHelper helper) {
        try {

            String id = helper.read(KEY_ID);
            String login = helper.read(KEY_LOGIN);
            String password = helper.read(KEY_PASSWORD);

            return login.length() > 0 && password.length() > 0 && id.length() > 0 && Integer.parseInt(id) > 0;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void logout(LoginHelper helper) {
        helper.remove(KEY_LOGIN);
        helper.remove(KEY_PASSWORD);
        helper.remove(KEY_ID);
    }

    /**
     * @param helper Helper instance
     * @param params Login credentials, Login, Password, Id
     */
    @Override
    public void login(LoginHelper helper, String... params) {
        if (params.length < 3) {
            throw new RuntimeException("Login excepts 3 parameters");
        }

        helper.write(KEY_LOGIN, params[0]);
        helper.write(KEY_PASSWORD, params[1]);
        helper.write(KEY_ID, params[2]);
    }
}
