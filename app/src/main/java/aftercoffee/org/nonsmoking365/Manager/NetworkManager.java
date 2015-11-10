package aftercoffee.org.nonsmoking365.Manager;

import android.os.Handler;
import android.os.Looper;

/**
 * Created by Tacademy on 2015-11-03.
 */
public class NetworkManager {
    private static NetworkManager instance;
    public static NetworkManager getInstance() {
        if (instance == null) {
            instance = new NetworkManager();
        }
        return instance;
    }
    private NetworkManager() {

    }

    public interface OnResultListener<T> {
        public void onSuccess(T result);
        public void onFail(int code);
    }

    Handler mHadler = new Handler(Looper.getMainLooper());
    public void login(String userid, String password, final OnResultListener<String> listener) {
        mHadler.postDelayed(new Runnable() {
            @Override
            public void run() {
                listener.onSuccess("ok");
            }
        }, 1000);
    }

    public void signup(String userid, String password, final OnResultListener<String> listener) {
        mHadler.postDelayed(new Runnable() {
            @Override
            public void run() {
                listener.onSuccess("ok");
            }
        }, 1000);
    }

}
