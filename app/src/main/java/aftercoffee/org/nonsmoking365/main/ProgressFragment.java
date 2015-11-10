package aftercoffee.org.nonsmoking365.main;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import aftercoffee.org.nonsmoking365.Manager.PropertyManager;
import aftercoffee.org.nonsmoking365.R;
import aftercoffee.org.nonsmoking365.Community.CommunityActivity;
import aftercoffee.org.nonsmoking365.board.BoardActivity;
import aftercoffee.org.nonsmoking365.Centers.CentersActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProgressFragment extends Fragment {

    private static final long TIME_SEC = 1000;
    private static final long TIME_MIN = 60 * TIME_SEC;
    private static final long TIME_HOUR = 60 * TIME_MIN;
    private static final long TIME_DAY = 24 * TIME_HOUR;
    private static final long TIME_YEAR = 365 * TIME_DAY;

    TextView gradeView, mottoView, timeView, savedMoneyView, statusView;

    int packPrice, numOfCigar, oneDaySaved, currentSaved;
    String startDateStr;
    long nonSmokingTime;

    public ProgressFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_progress, container, false);

        // Initialize view
        gradeView = (TextView)view.findViewById(R.id.text_gradeView);
        mottoView = (TextView)view.findViewById(R.id.text_mottoView);
        timeView = (TextView)view.findViewById(R.id.text_timeView);
        savedMoneyView = (TextView)view.findViewById(R.id.text_savedmoneyView);
        statusView = (TextView)view.findViewById(R.id.text_statusView);

        // 회원 등급
        /** 회원 등급 Network에서 받아와서 처리할 것 */
        gradeView.setText("일반 회원");

        // 금연 목표
        String motto = PropertyManager.getInstance().getBasisMotto();
        mottoView.setText(motto);

        // 금연 진행 시간, 절약 금액, 건강 상태 (Handler)
        startDateStr = PropertyManager.getInstance().getBasisStartDate();
        packPrice = Integer.parseInt(PropertyManager.getInstance().getBasisPackPrice());
        numOfCigar = Integer.parseInt(PropertyManager.getInstance().getBasisNumOfCigar());
        mHandler.removeCallbacks(updateRunnable);
        mHandler.post(updateRunnable);

        // Banner
        RelativeLayout banner = (RelativeLayout)view.findViewById(R.id.banner);
        banner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://nosmoking365.com"));
                startActivity(intent);
            }
        });

        // Btn
        Button btn = (Button)view.findViewById(R.id.btn_board);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BoardActivity.class);
                startActivity(intent);
            }
        });
        btn = (Button)view.findViewById(R.id.btn_community);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CommunityActivity.class);
                startActivity(intent);
            }
        });
        btn = (Button)view.findViewById(R.id.btn_centers);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CentersActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    Handler mHandler = new Handler();
    private static final int TIME_INTERVAL = 1000;

    Runnable updateRunnable = new Runnable() {
        @Override
        public void run() {
            // 시간 계산
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date startDate = sdf.parse(startDateStr);       // String > Date 변환
                long startTime = startDate.getTime();           // Date > long (Millisec)
                nonSmokingTime = System.currentTimeMillis() - startTime;
            } catch (ParseException e) {
                e.printStackTrace();
            }

            // 뷰 설정
            if (nonSmokingTime > 0) {
                // 현재시간 설정
                int nonSmokingDays = (int) (nonSmokingTime / TIME_DAY);
                int nonSmokingHours= (int) (nonSmokingTime / TIME_HOUR) - (nonSmokingDays * 24);
                int nonSmokingMins = (int) (nonSmokingTime / TIME_MIN) - (nonSmokingDays * 24 * 60) - (nonSmokingHours * 60);
                int nonSmokingSecs = (int) (nonSmokingTime / TIME_SEC) - (nonSmokingDays * 24 * 60 * 60) - (nonSmokingHours * 60 * 60) - (nonSmokingMins * 60);
                //SimpleDateFormat sdf = new SimpleDateFormat("dd일 (hh:mm:ss)");
                //Calendar.getInstance().set(0, 0, nonSmokingDays, nonSmokingHours, nonSmokingMins, nonSmokingSecs);
                //sdf.setCalendar();
                String s = String.format("%d일 %d시간 %d분 %d초", nonSmokingDays, nonSmokingHours, nonSmokingMins, nonSmokingSecs);
                timeView.setText(s);

                // 절약금액
                currentSaved = packPrice/20 * numOfCigar * nonSmokingDays;
                savedMoneyView.setText(currentSaved+" 원 절약");

                // 건강 상태 (12단계 등급)
                if (nonSmokingTime > 15*TIME_YEAR) {
                    statusView.setText("1등급");
                }else if (nonSmokingTime > 10*TIME_YEAR) {
                    statusView.setText("2등급");
                }else if (nonSmokingTime > 5*TIME_YEAR) {
                    statusView.setText("3등급");
                }else if (nonSmokingTime > TIME_YEAR) {
                    statusView.setText("4등급");
                }else if (nonSmokingTime > 180 * TIME_DAY) {
                    statusView.setText("5등급");
                }else if (nonSmokingTime > 14 * TIME_DAY) {
                    statusView.setText("6등급");
                }else if (nonSmokingTime > 72 * TIME_HOUR) {
                    statusView.setText("7등급");
                }else if (nonSmokingTime > 48 * TIME_HOUR) {
                    statusView.setText("8등급");
                }else if (nonSmokingTime > 24 * TIME_HOUR) {
                    statusView.setText("9등급");
                }else if (nonSmokingTime > 8 * TIME_HOUR) {
                    statusView.setText("10등급");
                }else if (nonSmokingTime > 2 * TIME_HOUR) {
                    statusView.setText("11등급");
                }else if (nonSmokingTime > 20 * TIME_MIN) {
                    statusView.setText("12등급");
                }else {
                    statusView.setText("13등급");
                }
            } else {
                timeView.setText("아직 금연을 시작하지 않으셨습니다");
                savedMoneyView.setText("금연 시작하시고 담배값을 절약하세요!");
                statusView.setText("13등급");
            }
            mHandler.postDelayed(this, TIME_INTERVAL);
        }
    };

    @Override
    public void onStop() {
        super.onStop();
        mHandler.removeCallbacks(updateRunnable);
    }

    @Override
    public void onPause() {
        super.onPause();
        mHandler.removeCallbacks(updateRunnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        mHandler.removeCallbacks(updateRunnable);
        mHandler.post(updateRunnable);
    }
}