package aftercoffee.org.nonsmoking365.manager;

import android.content.Context;

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

import aftercoffee.org.nonsmoking365.data.Community;
import aftercoffee.org.nonsmoking365.data.CommunityDocs;
import aftercoffee.org.nonsmoking365.data.CommunityResult;
import aftercoffee.org.nonsmoking365.data.SearchPOIInfo;
import aftercoffee.org.nonsmoking365.data.BoardResult;
import aftercoffee.org.nonsmoking365.data.LikesResult;
import aftercoffee.org.nonsmoking365.data.LoginResult;
import aftercoffee.org.nonsmoking365.data.NoticeResult;
import aftercoffee.org.nonsmoking365.data.POIResult;
import aftercoffee.org.nonsmoking365.MyApplication;
import aftercoffee.org.nonsmoking365.data.Notice;
import aftercoffee.org.nonsmoking365.data.Board;
import aftercoffee.org.nonsmoking365.data.BoardDocs;
import aftercoffee.org.nonsmoking365.data.Login;

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


    // Server URL
    private static final String SERVER = "http://52.68.247.34:3000";
    private static final String WITHDRAW_URL = SERVER + "/withdraws";


    /** 로그인, 로그아웃
     *
     */
    private static final String LOGIN_URL = SERVER + "/login";
    public void login(Context context, String userEmail, String password, final OnResultListener<Login> listener) {
        RequestParams params = new RequestParams();
        params.put("username", userEmail);
        params.put("password", password);
        client.post(context, LOGIN_URL, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                listener.onFail(statusCode);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                LoginResult result = gson.fromJson(responseString, LoginResult.class);
                listener.onSuccess(result.data);
            }
        });
    }
    private static final String LOGOUT_URL = SERVER + "/logout";
    public void logout(Context context, final OnResultListener<String> listener) {
        client.get(context, LOGOUT_URL, new TextHttpResponseHandler() {
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


    /** 금연 정보 게시판
     *
     */
    private static final String BOARD_URL = SERVER + "/infos";
    // 금연정보 글목록 가져오기 (GET)
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
    // 금연정보 글 진입 시 글내용, 댓글목록 가져오기 (GET)
    public void getBoardContentAndComments(Context context, String docID, final OnResultListener<BoardDocs> listener) {
        RequestParams params = new RequestParams();
        client.get(context, BOARD_URL + "/" + docID, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                listener.onFail(statusCode);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                BoardDocs result = gson.fromJson(responseString, BoardDocs.class);
                listener.onSuccess(result);
            }
        });
    }
    // 정보글 좋아요 클릭 (POST)
    public void postBoardLike(Context context, String docID, String user_id, final OnResultListener<LikesResult> listener) {
        RequestParams params = new RequestParams();
        params.put("_id", user_id);
        client.post(context, BOARD_URL + "/" + docID + "/likes", params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                listener.onFail(statusCode);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                LikesResult result = gson.fromJson(responseString, LikesResult.class);
                listener.onSuccess(result);
            }
        });
    }
    // 금연정보 댓글 작성 (POST)
    public void postBoardComment(Context context, String docID, String user_id, String content, final OnResultListener<BoardDocs> listener) {
        RequestParams params = new RequestParams();
        params.put("user_id", user_id);
        params.put("content", content);
        client.post(context, BOARD_URL + "/" + docID + "/comments", params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                listener.onFail(statusCode);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                BoardDocs result = gson.fromJson(responseString, BoardDocs.class);
                listener.onSuccess(result);
            }
        });
    }
    // 금연정보 댓글 삭제 (DELETE)
    public void deleteBoardCommentDelete(Context context, String docID, String comment_id, final OnResultListener<BoardDocs> listener) {
        client.delete(context, BOARD_URL + "/" + docID + "/comments" + "/" + comment_id , new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                listener.onFail(statusCode);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                BoardDocs result = gson.fromJson(responseString, BoardDocs.class);
                listener.onSuccess(result);
            }
        });
    }
    // 댓글 수정


    /** 금연 커뮤니티
     *
     */
    private static final String COMMUNITY_URL = SERVER + "/communities";
    // 금연커뮤니티 글목록 가져오기 (GET)
    public void getCommunityData(Context context, int perPage, int page, final OnResultListener<Community> listener) {
        RequestParams params = new RequestParams();        // param 설정
        params.put("perPage", perPage);
        params.put("page", page);
        client.get(context, COMMUNITY_URL, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                listener.onFail(statusCode);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                CommunityResult result = gson.fromJson(responseString, CommunityResult.class);
                listener.onSuccess(result.community);
            }
        });
    }
    // 금연커뮤니티 글 진입 시 상세 글내용, 댓글목록 가져오기 (GET)
    public void getCommunityContentAndComments(Context context, String docID, final OnResultListener<CommunityDocs> listener) {
        RequestParams params = new RequestParams();
        client.get(context, COMMUNITY_URL + "/" + docID, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                listener.onFail(statusCode);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                CommunityDocs result = gson.fromJson(responseString, CommunityDocs.class);
                listener.onSuccess(result);
            }
        });
    }
    // 커뮤니티글 좋아요 클릭 (POST)
    public void postCommunityLike(Context context, String docID, String user_id, final OnResultListener<LikesResult> listener) {
        RequestParams params = new RequestParams();
        params.put("_id", user_id);
        client.post(context, COMMUNITY_URL + "/" + docID + "/likes", params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                listener.onFail(statusCode);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                LikesResult result = gson.fromJson(responseString, LikesResult.class);
                listener.onSuccess(result);
            }
        });
    }
    // 금연커뮤니티 댓글 작성 (POST)
    public void postCommunityComment(Context context, String docID, String user_id, String content, final OnResultListener<CommunityDocs> listener) {
        RequestParams params = new RequestParams();
        params.put("user_id", user_id);
        params.put("content", content);
        client.post(context, COMMUNITY_URL + "/" + docID + "/comments", params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                listener.onFail(statusCode);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                CommunityDocs result = gson.fromJson(responseString, CommunityDocs.class);
                listener.onSuccess(result);
            }
        });
    }
    // 금연커뮤니티 댓글 삭제 (DELETE)
    public void deleteCommunityCommentDelete(Context context, String docID, String comment_id, final OnResultListener<CommunityDocs> listener) {
        client.delete(context, COMMUNITY_URL + "/" + docID + "/comments" + "/" + comment_id , new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                listener.onFail(statusCode);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                CommunityDocs result = gson.fromJson(responseString, CommunityDocs.class);
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
