package aftercoffee.org.nonsmoking365;

import android.app.Application;
import android.content.Context;

/**
 * Created by Tacademy on 2015-11-02.
 */
public class MyApplication extends Application {

    private static Context mContext;

    public static Context getContext() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }
}
