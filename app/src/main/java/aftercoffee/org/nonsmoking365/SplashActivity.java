package aftercoffee.org.nonsmoking365;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import aftercoffee.org.nonsmoking365.Basisinfo.BasisInfoActivity;
import aftercoffee.org.nonsmoking365.Main.MainActivity;
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
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isPreviewChecked) {
                    goPreview();
                } else if (!isBasisInfoFilledIn) {
                    goBasisInfo();
                } else {
                    goMain();
                }
            }
        }, 2000);
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
