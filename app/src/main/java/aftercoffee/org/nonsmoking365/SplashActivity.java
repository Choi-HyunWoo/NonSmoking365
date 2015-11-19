package aftercoffee.org.nonsmoking365;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import aftercoffee.org.nonsmoking365.main.MainActivity;
import aftercoffee.org.nonsmoking365.Manager.PropertyManager;

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
            /*
            NetworkManager.getInstance().login(this, id, password, new NetworkManager.OnResultListener<String>() {
                @Override
                public void onSuccess(String result) {
                    if (result.equals("ok")) {
                        UserManager.getInstance().setLoginState(true);
                        goMain();
                    } else {
                        UserManager.getInstance().setLoginState(false);
                        goMain();
                    }
                }
                @Override
                public void onFail(int code) {
                    Log.d("Network error/splash", ""+code);
                }
            });
            */
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
