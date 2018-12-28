package com.github.adizbek.sper.helper.login;

import com.github.adizbek.sper.LoginHelper;

public interface ILogin {
    boolean isLoggedIn(LoginHelper helper);

    void logout(LoginHelper helper);

    void login(LoginHelper helper, String... params);
}
