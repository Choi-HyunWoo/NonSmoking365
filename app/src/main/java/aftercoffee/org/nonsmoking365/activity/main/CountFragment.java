package aftercoffee.org.nonsmoking365.activity.main;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import aftercoffee.org.nonsmoking365.PropertyManager;
import aftercoffee.org.nonsmoking365.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class CountFragment extends Fragment {

    GridView countGridView;
    CountGridAdapter mAdapter;
    TextView countStartView;
    TextView countPositionView;
    TextView countRestView;

    public CountFragment() {
        // Required empty public constructor
    }

    boolean isCounting;             // 카운트 중인가?
    long countStartTime;            // 카운트 시작 시간
    long nextMidNight;              // 다음날 자정 시간
    long countingTime;              // countStartTime ~ NextMidnight까지의 시간
    long countRestTime;             // 다음 카운트까지 남은 시간

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_count, container, false);

        // View initialize
        countGridView = (GridView)view.findViewById(R.id.gridView);
        mAdapter = new CountGridAdapter();
        countGridView.setAdapter(mAdapter);
        countStartView = (TextView)view.findViewById(R.id.text_countStartDate);
        countPositionView = (TextView)view.findViewById(R.id.text_countPosition);
        countRestView = (TextView)view.findViewById(R.id.text_countRestTime);

        // Initialize saved count info
        mAdapter.initCount();
        countStartTime = PropertyManager.getInstance().getCountStartTime();
        if (countStartTime != 0L) isCounting = true;
        else isCounting = false;

        // 금연 카운트 진행 현황 표시, 카운트 버튼 실시간 업데이트 (Handler)
        mHandler.removeCallbacks(countRunnable);
        mHandler.post(countRunnable);

        // Count Buttons
        countGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                CountItem c = (CountItem) mAdapter.getItem(position);
                switch (c.itemMode) {
                    case CountItem.MODE_ON :
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("금연 카운트");
                        builder.setMessage((position + 1) + "일차 금연에 성공하셨습니까?");

                        builder.setPositiveButton("성공", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mAdapter.nonSmokingSuccess(position);
                                isCounting = true;
                            }
                        });
                        builder.setNegativeButton("실패", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mAdapter.nonSmokingFailed(position);
                                isCounting = true;
                            }
                        });
                        AlertDialog dlg = builder.create();
                        dlg.show();
                        break;
                    case CountItem.MODE_OFF :
                        // 다음 카운트까지 남은 시간 계산
                        countRestTime = nextMidNight - System.currentTimeMillis();
                        int restHour = (int) countRestTime / (60 * 60 * 1000);
                        int restMin = (int) countRestTime / (60 * 1000) - (restHour * 60);
                        int restSec = (int) countRestTime / 1000 - (restHour * 60 * 60) - (restMin * 60);
                        Toast.makeText(getActivity(), "다음 카운트까지 " + restHour + "시간 " + restMin + " 분 " + restSec + " 초 남았습니다.", Toast.LENGTH_SHORT).show();
                        break;
                    case CountItem.MODE_O :
                        break;
                    case CountItem.MODE_X :
                        break;
                    default:
                        break;
                }
            }
        });

        Button btn = (Button) view.findViewById(R.id.btn_reset);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 금연 카운트를 초기화하시겠습니까?
                mAdapter.nonSmokingCountReset();
                Toast.makeText(getActivity(), "금연 카운트가 초기화되었습니다.", Toast.LENGTH_SHORT).show();
                countPositionView.setText("");
                countStartView.setText("금연 카운트를 시작해 주세요");
                isCounting = false;
            }
        });

        return view;
    }



    Handler mHandler = new Handler();
    private static final int TIME_INTERVAL = 1000;
    Runnable countRunnable = new Runnable() {
        @Override
        public void run() {
            // 현재시간과 어제 자정, 내일 자정 시간 계산
            Calendar c = Calendar.getInstance(Locale.KOREA);
            c.set(Calendar.HOUR_OF_DAY, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);
            c.add(Calendar.DAY_OF_YEAR, 1);
            nextMidNight = c.getTimeInMillis();

            // Count 시작일 setting
            Date startDate = new Date(countStartTime);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            // 오늘의 Count Position 계산
            countingTime = nextMidNight - countStartTime;
            int todayCountPos = (int) countingTime / (24 * 60 * 60 * 1000);         // 오늘의 Count position이 몇인지?

            // 다음 Count까지 남은시간 계산
            countRestTime = nextMidNight - System.currentTimeMillis();
            int restHour = (int) countRestTime / (60 * 60 * 1000);
            int restMin = (int) countRestTime / (60 * 1000) - (restHour * 60);
            int restSec = (int) countRestTime / 1000 - (restHour * 60 * 60) - (restMin * 60);

            if (isCounting) {
                countRestView.setText("다음 카운트까지 " + restHour + "시간 " + restMin + " 분 " + restSec + " 초 남았습니다.");
                countPositionView.setText("금연 카운트 " + (todayCountPos + 1) + "일차");
                countStartView.setText("금연 카운트 시작일 : " + sdf.format(startDate));
            } else {
                countRestView.setText("");
                countPositionView.setText("");
                countStartView.setText("금연 카운트를 시작해 주세요");
            }

            // 시간에 따른 카운트 버튼 활성화
            if (countStartTime != 0) {
                if (mAdapter.isModeOff(todayCountPos)                               // 오늘 카운트 position이 OFF 모드이고
                        && todayCountPos <= mAdapter.COUNT_ITEMS_MAX_POSITION) {    // 오늘 카운트 position이 최대 카운트를 넘어서지 않았다면
                    mAdapter.setModeOn(todayCountPos);                              // ON으로!
                }
            }

            mHandler.postDelayed(this, TIME_INTERVAL);
        }
    };


    @Override
    public void onStop() {
        super.onStop();
        mHandler.removeCallbacks(countRunnable);
    }

    @Override
    public void onPause() {
        super.onPause();
        mHandler.removeCallbacks(countRunnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        mHandler.removeCallbacks(countRunnable);
        mHandler.post(countRunnable);
    }

}
