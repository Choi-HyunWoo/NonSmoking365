package aftercoffee.org.nonsmoking365.Activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import aftercoffee.org.nonsmoking365.Activity.main.MainActivity;
import aftercoffee.org.nonsmoking365.Data.Login;
import aftercoffee.org.nonsmoking365.Manager.NetworkManager;
import aftercoffee.org.nonsmoking365.Manager.PropertyManager;
import aftercoffee.org.nonsmoking365.Manager.UserManager;
import aftercoffee.org.nonsmoking365.R;

public class SplashActivity extends AppCompatActivity {

    /**
     * Display Splash image during 2 seconds
     * and choice next activity
     */
    Handler mHandler = new Handler(Looper.getMainLooper());

    Boolean isPreviewChecked;
    Boolean isBasisInfoFilledIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // 화면 선택
        isPreviewChecked = PropertyManager.getInstance().getPreviewCheck();
        isBasisInfoFilledIn = PropertyManager.getInstance().getBasisInfoCheck();


        // 자동 로그인 상태 확인
        boolean autoLogin = PropertyManager.getInstance().getAutoLogin();
        // 자동로그인 ON
        if (autoLogin) {
            String id = PropertyManager.getInstance().getAutoLoginId();
            String password = PropertyManager.getInstance().getAutoLoginPassword();

            NetworkManager.getInstance().login(this, id, password, new NetworkManager.OnResultListener<Login>() {
                @Override
                public void onSuccess(Login result) {
                    if (result.status.equals("ok")) {
                        // 자동 로그인 성공 >> 유저 정보 저장
                        UserManager.getInstance().setLoginState(true);
                        UserManager.getInstance().setUser_id(result.user._id);
                        UserManager.getInstance().setUserEmail(result.user.email);
                        UserManager.getInstance().setUserPassword(result.user.password);
                        UserManager.getInstance().setUserNickname(result.user.nick);
                        choiceNextActivity();
                    } else {
                        // 자동 로그인 실패 >> 비회원으로 접속
                        UserManager.getInstance().setLoginState(false);
                        choiceNextActivity();
                    }
                }

                @Override
                public void onFail(int code) {
                    // 서버와의 연결 실패 >> 로그 띄우고 비회원으로 접속
                    UserManager.getInstance().setLoginState(false);
                    Log.d("Network error/splash", "" + code);
                    choiceNextActivity();
                }
            });
        }
        // 자동로그인 OFF
        else {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    choiceNextActivity();
                }
            }, 2000);
        }
    }

    private void choiceNextActivity() {
        if (!isPreviewChecked) {
            goPreview();
        } else if (!isBasisInfoFilledIn) {
            goBasisInfo();
        } else {
            goMain();
        }
    }

    private void goPreview() {
        Intent intent = new Intent(SplashActivity.this, PreviewActivity.class);
        startActivity(intent);
        finish();
    }

    private void goBasisInfo() {
        Intent intent = new Intent(SplashActivity.this, BasisInfoActivity.class);
        startActivity(intent);
        finish();
    }

    private void goMain() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
