package aftercoffee.org.nonsmoking365.main;

import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.Toast;

import aftercoffee.org.nonsmoking365.Manager.NetworkManager;
import aftercoffee.org.nonsmoking365.Manager.UserManager;
import aftercoffee.org.nonsmoking365.R;

public class MainActivity extends AppCompatActivity {

    TabHost tabHost;
    ViewPager pager;
    TabsAdapter mAdapter;
    boolean isLogined;

    private static final String TAB_TAG = "currentTab";
    private static final String TAB_ID_PROGRESS = "tab_progress";
    private static final String TAB_ID_COUNT = "tab_count";
    private static final String TAB_ID_SETTINGS = "tab_settings";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setIcon(R.drawable.logo);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("금연 진행 현황");
        actionBar.setElevation(0);
        isLogined = UserManager.getInstance().getLoginState();

        tabHost = (TabHost) findViewById(android.R.id.tabhost);
        tabHost.setup();

        pager = (ViewPager)findViewById(R.id.pager);
        mAdapter = new TabsAdapter(this, getSupportFragmentManager(), tabHost, pager);

        mAdapter.addTab(tabHost.newTabSpec(TAB_ID_PROGRESS).setIndicator("",getResources().getDrawable(R.drawable.selector_tab_progress)), ProgressFragment.class, null);
        mAdapter.addTab(tabHost.newTabSpec(TAB_ID_COUNT).setIndicator("",getResources().getDrawable(R.drawable.selector_tab_count)), CountFragment.class, null);
        mAdapter.addTab(tabHost.newTabSpec(TAB_ID_SETTINGS).setIndicator("",getResources().getDrawable(R.drawable.selector_tab_setting)), OptionsFragment.class, null);
        setTabColor(tabHost);

        mAdapter.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                setTabColor(tabHost);
                if (tabId.equals(TAB_ID_PROGRESS)) {
                    actionBar.setTitle("금연 진행 현황");
                } else if (tabId.equals(TAB_ID_COUNT)) {
                    actionBar.setTitle("금연 카운트");
                } else {
                    actionBar.setTitle("설정");
                }
            }
        });
        if (savedInstanceState != null) {
            mAdapter.onRestoreInstanceState(savedInstanceState);
            tabHost.setCurrentTabByTag(savedInstanceState.getString(TAB_TAG));
        }
    }
    public void setTabColor(TabHost tabhost) {
        for(int i=0;i<tabhost.getTabWidget().getChildCount();i++) {
            tabhost.getTabWidget().getChildAt(i).setBackgroundColor(getResources().getColor(R.color.colorDark)); //unselected
        }
        tabhost.getTabWidget().getChildAt(tabhost.getCurrentTab()).setBackgroundColor(getResources().getColor(R.color.colorAccent)); // selected
    }

    /* Back key (뒤로가기) 처리 */
    private static final int MESSAGE_BACKKEY_TIMEOUT = 1;
    private static final long TIMEOUT_BACKKEY_DELAY = 2000;
    Boolean isBackPressed = false;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_BACKKEY_TIMEOUT :
                    isBackPressed = false;
                    break;
            }
        }
    };

    @Override
    public void onBackPressed() {
        if (!isBackPressed) {
            isBackPressed = true;
            Toast.makeText(MainActivity.this, "뒤로가기를 한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
            mHandler.sendEmptyMessageDelayed(MESSAGE_BACKKEY_TIMEOUT, TIMEOUT_BACKKEY_DELAY);
        } else {
            mHandler.removeMessages(MESSAGE_BACKKEY_TIMEOUT);
            super.onBackPressed();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mAdapter.onSaveInstanceState(outState);
        outState.putString(TAB_TAG, tabHost.getCurrentTabTag());
    }

    @Override
    protected void onResume() {
        super.onResume();
        isLogined = UserManager.getInstance().getLoginState();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NetworkManager.getInstance().cancelAll(this);
    }
}
