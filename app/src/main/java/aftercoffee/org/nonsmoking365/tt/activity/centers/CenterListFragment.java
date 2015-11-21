package aftercoffee.org.nonsmoking365.tt.activity.centers;


import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import aftercoffee.org.nonsmoking365.tt.data.POI;
import aftercoffee.org.nonsmoking365.tt.data.SearchPOIInfo;
import aftercoffee.org.nonsmoking365.tt.manager.NetworkManager;
import aftercoffee.org.nonsmoking365.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CenterListFragment extends Fragment {

    ListView centerListView;
    CentersItemAdapter mAdapter;

    LocationManager mLM;
    String mProvider = LocationManager.NETWORK_PROVIDER;

    public CenterListFragment() {
        // Required empty public constructor
        this.setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_center_list, container, false);
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle("주변 보건소 및 금연 상담센터");

        centerListView = (ListView)view.findViewById(R.id.list_centers);
        mAdapter = new CentersItemAdapter();
        centerListView.setAdapter(mAdapter);

        mLM = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);

        return view;
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
                Toast.makeText(getActivity(), "위치 정보 설정이 꺼져 있습니다.", Toast.LENGTH_SHORT).show();
                ((CentersActivity)getActivity()).popMapFragment();
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
                    Toast.makeText(getActivity(), "Timeout location update", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    // Application이 Location Service로부터 위치와 관련된 정보를 수신하기 위해 사용하는 interface
    LocationListener mListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            // 새로 fix된 위치정보가 있을 시 호출
            mHandler.removeMessages(MESSAGE_TIMEOUT_LOCATION_UPDATE);
            NetworkManager.getInstance().findPOI(getActivity(), location.getLatitude(), location.getLongitude(), 10, new NetworkManager.OnResultListener<SearchPOIInfo>() {
                @Override
                public void onSuccess(SearchPOIInfo result) {
                    mAdapter.clear();
                    for (POI item : result.pois.poilist) {
                        mAdapter.add(item);
                    }
                }
                @Override
                public void onFail(int code) {
                    Log.d("CenterListFragment ", "network error/" + code);
                }
            });
        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // Provider의 상태 변경시 호출. status는 LocationProvider에 정의되어있음
            switch (status) {
                case LocationProvider.AVAILABLE :
                    Toast.makeText(getActivity(), "위치정보 기능을 사용할 수 있습니다", Toast.LENGTH_SHORT).show();
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    Toast.makeText(getActivity(), "현재 위치정보 기능을 받아올 수 없습니다", Toast.LENGTH_SHORT).show();
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    Toast.makeText(getActivity(), "위치정보 기능을 사용할 수 없습니다", Toast.LENGTH_SHORT).show();
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


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_center_fragment, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home :
                getActivity().finish();
                break;
            case R.id.action_goto_provider :
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));        // 위치정보 설정으로 이동
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        registerLocationListener();
    }

    @Override
    public void onStop() {
        super.onStop();
        unregisterLocationListener();
    }
}
