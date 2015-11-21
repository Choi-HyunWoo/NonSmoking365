package aftercoffee.org.nonsmoking365.activity.main;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import aftercoffee.org.nonsmoking365.manager.NetworkManager;
import aftercoffee.org.nonsmoking365.manager.PropertyManager;
import aftercoffee.org.nonsmoking365.manager.UserManager;
import aftercoffee.org.nonsmoking365.R;
import aftercoffee.org.nonsmoking365.service.RegistrationIntentService;
import aftercoffee.org.nonsmoking365.utilities.ServerUtilities;

public class MainActivity extends AppCompatActivity {

    // GCM
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    private static final String TAB_TAG = "currentTab";
    private static final String TAB_ID_PROGRESS = "tab_progress";
    private static final String TAB_ID_COUNT = "tab_count";
    private static final String TAB_ID_SETTINGS = "tab_settings";

    TabHost tabHost;
    ViewPager pager;
    TabsAdapter mAdapter;
    boolean isLogined;

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

        // GCM
        if (!PropertyManager.getInstance().getGCMSended()) {
            mRegistrationBroadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    doRealStart();
                }
            };
            setUpIfNeeded();
            PropertyManager.getInstance().setGCMSended(true);
        }
        //
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
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(RegistrationIntentService.REGISTRATION_COMPLETE));
        isLogined = UserManager.getInstance().getLoginState();
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLAY_SERVICES_RESOLUTION_REQUEST &&
                resultCode == Activity.RESULT_OK) {
            setUpIfNeeded();
        }
    }

    private void setUpIfNeeded() {
        if (checkPlayServices()) {
            String regId = PropertyManager.getInstance().getRegistrationToken();
            if (!regId.equals("")) {
                doRealStart();
            } else {
                Intent intent = new Intent(this, RegistrationIntentService.class);
                startService(intent);
            }
        }
    }

    private void doRealStart() {
        // activity start...
        new AsyncTask<Void,Void,Boolean>(){
            @Override
            protected Boolean doInBackground(Void... params) {
                String regid = PropertyManager.getInstance().getRegistrationToken();
                ServerUtilities.register(MainActivity.this, regid);
                return null;
            }
        }.execute();
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                Dialog dialog = apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST);
                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

                    @Override
                    public void onCancel(DialogInterface dialog) {
                        finish();
                    }
                });
                dialog.show();
            } else {
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NetworkManager.getInstance().cancelAll(this);
    }
}
