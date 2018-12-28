package com.github.adizbek.sper;

import android.content.Context;
import com.github.adizbek.sper.helper.login.BaseLoginHandler;
import com.github.adizbek.sper.helper.login.ILogin;

public class Sper {
    private static Sper instance;

    public static Sper getInstance() {
        return instance;
    }

    private Sper(Builder builder) {
        LoginHelper.init(builder.mContext, builder.loginHandler);
    }

    public static void init(Builder builder) {
        instance = new Sper(builder);
    }

    public static class Builder {
        private Context mContext;
        private ILogin loginHandler;

        public Builder setContext(Context context) {
            mContext = context;

            return this;
        }

        public Builder setLoginHandler(ILogin loginHandler) {
            this.loginHandler = loginHandler;

            return this;
        }

        public Sper build() {
            if (loginHandler == null) {
                loginHandler = new BaseLoginHandler();
            }

            return new Sper(this);
        }
    }
}
