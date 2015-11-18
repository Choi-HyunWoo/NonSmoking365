package aftercoffee.org.nonsmoking365.Manager;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.MySSLSocketFactory;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.apache.http.client.HttpClient;
import org.apache.http.message.BasicHeader;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import aftercoffee.org.nonsmoking365.Centers.SearchPOIInfo;
import aftercoffee.org.nonsmoking365.MyApplication;
import aftercoffee.org.nonsmoking365.Notice.Image;
import aftercoffee.org.nonsmoking365.Notice.Notice;
import aftercoffee.org.nonsmoking365.board.Board;
import aftercoffee.org.nonsmoking365.board.Docs;

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
    Header[] headers;

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

        headers = new Header[2];
        headers[0] = new BasicHeader("Accept", "application/json");
        headers[1] = new BasicHeader("appKey", "7bf0afee-4fea-3563-97f5-c993e7af68e1");
    }

    public interface OnResultListener<T> {
        public void onSuccess(T result);
        public void onFail(int code);
    }

    // UniversalImageloader setting
    public HttpClient getHttpClient() {
        return client.getHttpClient();
    }



    ///////////////////////////////////////////////////////////////////////////////Sample
    Handler mHadler = new Handler(Looper.getMainLooper());
    public void login(String userid, String password, final OnResultListener<String> listener) {
        mHadler.postDelayed(new Runnable() {
            @Override
            public void run() {
                listener.onSuccess("ok");
            }
        }, 1000);
    }
    ///////////////////////////////////////////////////////////////////////////////

    // Server URL
    private static final String SERVER = "http://52.68.247.34:3000";
    private static final String WITHDRAW_URL = SERVER + "/withdraws";

    /** 로그인
     *
     */
    private static final String LOGIN_URL = SERVER + "/login";
    public void postLogin(Context context, String userEmail, String password, final OnResultListener<LoginResult> listener) {
        RequestParams params = new RequestParams();
        params.put("email", userEmail);
        params.put("password", password);
        client.post(context, LOGIN_URL, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                listener.onFail(statusCode);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                LoginResult result = gson.fromJson(responseString, LoginResult.class);
                listener.onSuccess(result);
            }
        });
    }


    /** 금연 정보 게시판
     *
     */
    // 금연 정보 글 목록 get
    private static final String BOARD_URL = SERVER + "/infos";
    public void getBoardData(Context context, int perPage, int page, final OnResultListener<Board> listener) {
        RequestParams params = new RequestParams();        // param 설정
        params.put("perPage", perPage);
        params.put("page", page);
        client.get(context, BOARD_URL, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                listener.onFail(statusCode);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                BoardResult result = gson.fromJson(responseString, BoardResult.class);
                listener.onSuccess(result.board);
            }
        });
    }
    // 금연 정보 글 선택시, 선택한 ID로 해당 글의 content, comments get
    public void getBoardContentAndComments(Context context, String docID, final OnResultListener<Docs> listener) {
        RequestParams params = new RequestParams();
        client.get(context, BOARD_URL + "/" + docID, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                listener.onFail(statusCode);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Docs result = gson.fromJson(responseString, Docs.class);
                listener.onSuccess(result);
            }
        });
    }
    // 정보글에 좋아요 클릭 시 post
    public void postBoardLike(Context context, String docID, String userID, final OnResultListener<LikesResult> listener) {
        RequestParams params = new RequestParams();
        params.put("_id", userID);
        client.post(context, BOARD_URL+"/" + docID + "/likes", params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                listener.onFail(statusCode);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                //Docs result = gson.fromJson(responseString, Docs.class);
                LikesResult result = gson.fromJson(responseString, LikesResult.class);
                listener.onSuccess(result);
            }
        });
    }

    /** 공지사항
     *
     */
    // 공지사항 글 목록, 내용 get
    private static final String NOTICE_URL = SERVER + "/notices";
    public void getNoticeData (Context context, final OnResultListener<Notice> listener) {
        client.get(context, NOTICE_URL, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                listener.onFail(statusCode);
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                NoticeResult result = gson.fromJson(responseString, NoticeResult.class);
                listener.onSuccess(result.notice);
            }
        });
    }


    /** 문의하기
     *
     */
    // 문의하기 글 제목, 내용 post
    private static final String QUESTION_URL = SERVER + "/questions";
    public void postQuestionData (Context context, String user_id, String title, String content, final OnResultListener<String> listener) {
        RequestParams params = new RequestParams();
        params.put("user_id", user_id);
        params.put("title", title);
        params.put("content", content);
        client.post(context, QUESTION_URL, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                listener.onFail(statusCode);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                listener.onSuccess(responseString);
            }
        });
    }


    /*
    public void login (Context context, String email, String password, final OnResultListener<String> listener) {
        RequestParams params = new RequestParams();
        params.put("email", email);
        params.put("password", password);
        client.post(context, USERS_URL, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

            }
        });
    }
    */

    /** 회원가입
     *
     */
    // 회원가입 post
    private static final String USERS_URL = SERVER + "/users";
    public void signUp (Context context, String email, String password, String nickname, final OnResultListener<String> listener) {
        RequestParams params = new RequestParams();
        params.put("email", email);
        params.put("password", password);
        params.put("nick", nickname);
        client.post(context, USERS_URL, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                listener.onFail(statusCode);
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                listener.onSuccess(responseString);
            }
        });
    }

    /** T Map
     *  보건소 리스트 받아오기
     */
    // 보건소 리스트 받아오기
    private static final String FIND_POI_URL = "https://apis.skplanetx.com/tmap/pois/search/around";
    public void findPOI(Context context, double my_lat, double my_lon, int radius, final OnResultListener<SearchPOIInfo> listener) {
        RequestParams params = new RequestParams();
        params.put("version", 1);
        params.put("categories", "보건소");
        params.put("centerLat", my_lat);
        params.put("centerLon", my_lon);
        params.put("radius", radius);
        params.put("reqCoordType", "WGS84GEO");
        params.put("resCoordType", "WGS84GEO");

        client.get(context, FIND_POI_URL, headers, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                listener.onFail(statusCode);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                POIResult result = gson.fromJson(responseString, POIResult.class);
                listener.onSuccess(result.searchPoiInfo);
            }
        });
    }


    // 접속 끊기
    public void cancelAll(Context context) {
        client.cancelRequests(context, true);
    }
}
