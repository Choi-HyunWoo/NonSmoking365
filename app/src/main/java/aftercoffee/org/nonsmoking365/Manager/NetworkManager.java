package aftercoffee.org.nonsmoking365.Manager;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.MySSLSocketFactory;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import aftercoffee.org.nonsmoking365.MyApplication;
import aftercoffee.org.nonsmoking365.board.Board;

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

    AsyncHttpClient client;
    Gson gson;

    private NetworkManager() {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);
            MySSLSocketFactory socketFactory = new MySSLSocketFactory(trustStore);
            socketFactory.setHostnameVerifier(MySSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            client = new AsyncHttpClient();
            client.setSSLSocketFactory(socketFactory);
            client.setCookieStore(new PersistentCookieStore(MyApplication.getContext()));
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        }
        gson = new Gson();
        client.setCookieStore(new PersistentCookieStore(MyApplication.getContext()));
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



    private static final String SERVER = "http://52.68.247.34:3000";
    private static final String BOARD_INFO_URL = SERVER + "/infos";
    public void getBoardData(Context context,  final OnResultListener<Board> listener) {        // param1, param2 설정
//        RequestParams params = new RequestParams();

        client.get(context, BOARD_INFO_URL, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                listener.onFail(statusCode);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                // MelonResult result = gson.fromJson(responseString, MelonResult.class);
                // JSONParsing interface 생성 (parsing 메소드)
                // GSON 구조화된 데이터 클래스들을 생성.
                // 받을 데이터의 최상위 데이터 클래스를 ~~~Result 클래스로 정의, parse 메소드에서 하위클래스의 객체를 생성하면서 parsing 해나갈 것.
                // listener.onSuccess(result.melon);
                BoardResult result = gson.fromJson(responseString, BoardResult.class);
                listener.onSuccess(result.board);
            }
        });
    }
}
