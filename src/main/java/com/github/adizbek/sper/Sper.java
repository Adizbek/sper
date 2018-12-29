package com.github.adizbek.sper;

import android.content.Context;
import com.github.adizbek.sper.helper.DateHelper;
import com.github.adizbek.sper.helper.login.BaseLoginHandler;
import com.github.adizbek.sper.helper.login.ILogin;
import kotlin.jvm.JvmStatic;

import java.lang.ref.WeakReference;
import java.util.Locale;

public class Sper {
    private static WeakReference<Sper> instance;

    private Context context;
    private Locale locale;

    public static Sper getInstance() {
        return instance.get();
    }

    public Locale getLocale() {
        return locale;
    }

    private Sper(Builder builder) {
        LoginHelper.init(builder.mContext, builder.loginHandler);
        DateHelper.init(builder.dateFormat);
        instance = new WeakReference<>(this);
        this.context = builder.mContext;
        this.locale = new Locale(builder.locale);
    }

    public static Context getContext() {
        return instance.get().context;
    }

    public static class Builder {
        private Context mContext;
        private ILogin loginHandler;
        private String dateFormat = DateHelper.getDefaultDateFormat();
        private String locale = "ru";

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

        public Builder setDefaultDateFormat(String dateFormat) {
            this.dateFormat = dateFormat;

            return this;
        }

        public Builder setLocale(String locale) {
            this.locale = locale;

            return this;
        }
    }
}
