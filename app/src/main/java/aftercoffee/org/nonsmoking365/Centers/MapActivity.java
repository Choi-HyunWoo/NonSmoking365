package aftercoffee.org.nonsmoking365.Centers;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.skp.Tmap.TMapData;
import com.skp.Tmap.TMapPOIItem;
import com.skp.Tmap.TMapPoint;
import com.skp.Tmap.TMapView;

import java.util.ArrayList;

import aftercoffee.org.nonsmoking365.R;

public class MapActivity extends AppCompatActivity {

    TMapView mapView;
    LocationManager mLM;
    String mProvider = LocationManager.NETWORK_PROVIDER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // View Initialize
        mapView = (TMapView)findViewById(R.id.map);
        new RegisterTask().execute();       // T MAP 초기화 (APP 키 등록)
        mLM = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        Button btn = (Button)findViewById(R.id.btn_current);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current();
            }
        });



    }

    // LocationProvider에서 위치정보 획득
    boolean isFirst = true;
    private void registerLocationListener() {
        // 위치 정보 설정이 Enabled 상태인지 확인
        // Enabled 상태가 아니라면
        if (!mLM.isProviderEnabled(mProvider)) {
            if (isFirst) {
                isFirst = false;
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));        // 위치정보 설정으로 이동
            } else {
                Toast.makeText(MapActivity.this, "위치 정보 설정이 꺼져 있습니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
            return;
        }
        // Enable 상태라면
        isFirst = true;
        Location location = mLM.getLastKnownLocation(mProvider);
        if (location != null) {
            mListener.onLocationChanged(location);
        }
        mLM.requestSingleUpdate(mProvider, mListener, null);
        //mLM.requestLocationUpdates(mProvider, 2000, 10, mListener);
        Message msg = mHandler.obtainMessage(MESSAGE_TIMEOUT_LOCATION_UPDATE);
        mHandler.sendMessageDelayed(msg, TIMEOUT_LOCATION_UPDATE);
    }
    private void unregisterLocationListener() {
        mLM.removeUpdates(mListener);
        mHandler.removeMessages(MESSAGE_TIMEOUT_LOCATION_UPDATE);
    }

    // 위치정보 받아오기 Timeout 처리
    private static final int MESSAGE_TIMEOUT_LOCATION_UPDATE = 1;
    private static final int TIMEOUT_LOCATION_UPDATE = 60 * 1000;
    Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_TIMEOUT_LOCATION_UPDATE :
                    Toast.makeText(MapActivity.this, "Timeout location update", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };


    public void current() {
        mLM.requestSingleUpdate(LocationManager.GPS_PROVIDER, mListener, null);
        Location location = mLM.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null) {
            moveMap(location.getLatitude(), location.getLongitude());
        }
    }

    /**
     * T Map *********************************************************************************************************
     */

    // T MAP 초기화를 위한 App Key 등록
    Boolean isInitialized = false;          // 초기화되었다면 TRUE로
    class RegisterTask extends AsyncTask<String,Void,Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            mapView.setSKPMapApiKey("7bf0afee-4fea-3563-97f5-c993e7af68e1");
            mapView.setLanguage(TMapView.LANGUAGE_KOREAN);
            return true;
        }
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            isInitialized = true;
            setUpMap();
        }
    }

    Location cacheLocation;
    // Application이 Location Service로부터 위치와 관련된 정보를 수신하기 위해 사용하는 interface
    LocationListener mListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            // 새로 fix된 위치정보가 있을 시 호출
            mHandler.removeMessages(MESSAGE_TIMEOUT_LOCATION_UPDATE);
            if (isInitialized) {                                                    // TmapView가 초기화되었다면
                moveMap(location.getLatitude(), location.getLongitude());           // 현재 위치로 맵을 이동
                setMyLocation(location.getLatitude(), location.getLongitude());     // 내 위치를 현재 위치로 지정
            } else {                                                                // TmapView가 초기화되지 않았다면
                cacheLocation = location;                                           // location을 cache로 저장해두었다가 TmapView의 초기화가 완료된 다음 이동하도록 함
            }
        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // Provider의 상태 변경시 호출. status는 LocationProvider에 정의되어있음
            switch (status) {
                case LocationProvider.AVAILABLE :
                    Toast.makeText(MapActivity.this, "위치정보 기능을 사용할 수 있습니다", Toast.LENGTH_SHORT).show();
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    Toast.makeText(MapActivity.this, "현재 위치정보 기능을 받아올 수 없습니다", Toast.LENGTH_SHORT).show();
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    Toast.makeText(MapActivity.this, "위치정보 기능을 사용할 수 없습니다", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
        @Override
        public void onProviderEnabled(String provider) {
            // 설정에서 등록된 Provider가 enabled로 설정되면 호출
        }
        @Override
        public void onProviderDisabled(String provider) {
            // 설정에서 등록된 Provider가 disabled로 설정되면 호출
        }
    };

    // 맵 이동 함수 (위도, 경도)
    public void moveMap(double lat, double lng) {       // latitude:위도 , longtitude:경도
        mapView.setCenterPoint(lng, lat);               // setCenterPoint 함수의 parameter는 경도, 위도 순임을 주의..
        mapView.setZoomLevel(17);
    }

    // 내 위치 설정 함수 (위도, 경도)
    public void setMyLocation(double lat, double lng) {
        mapView.setLocationPoint(lng, lat);             // setLocationPoint 함수의 parameter도 경도, 위도 순임을 주의..
        Bitmap bm = ((BitmapDrawable)getResources().getDrawable(R.drawable.icon_man_active)).getBitmap();
        mapView.setIcon(bm);
        mapView.setIconVisibility(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerLocationListener();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterLocationListener();
    }

    // Map을 어떻게 보여줄지 정의
    public void setUpMap() {
        // 저장해둔 Location이 있다면
        if (cacheLocation != null) {
            moveMap(cacheLocation.getLatitude(), cacheLocation.getLongitude());
            setMyLocation(cacheLocation.getLatitude(), cacheLocation.getLongitude());
            cacheLocation = null;
        }
    }
}

