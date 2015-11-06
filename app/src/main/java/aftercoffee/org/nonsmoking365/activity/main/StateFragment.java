package aftercoffee.org.nonsmoking365.activity.main;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import aftercoffee.org.nonsmoking365.PropertyManager;
import aftercoffee.org.nonsmoking365.R;
import aftercoffee.org.nonsmoking365.activity.board.BoardActivity;
import aftercoffee.org.nonsmoking365.activity.CentersActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class StateFragment extends Fragment {

    TextView gradeView, mottoView, timeView, savedMoneyView, statusView;

    int packPrice, numOfCigar, oneDaySaved, currentSaved;
    String startDateStr;
    Date startDate;
    long nonSmokingTime;
    int nonSmokingDays;

    public StateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_state, container, false);

        // Initialize view
        gradeView = (TextView)view.findViewById(R.id.text_gradeView);
        mottoView = (TextView)view.findViewById(R.id.text_mottoView);
        timeView = (TextView)view.findViewById(R.id.text_timeView);
        savedMoneyView = (TextView)view.findViewById(R.id.text_savedmoneyView);
        statusView = (TextView)view.findViewById(R.id.text_status);

        // 회원 등급
        /** 등급 Network에서 받아와서 처리할 것 */
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

        /** 건강 상태 << 기획 받는대로 추가 */

        // Btn
        Button btn = (Button)view.findViewById(R.id.btn_warning);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BoardActivity.class);
                intent.putExtra(BoardActivity.PARAM_BOARD_TYPE, BoardActivity.TYPE_WARNING);
                startActivity(intent);
            }
        });
        btn = (Button)view.findViewById(R.id.btn_tips);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BoardActivity.class);
                intent.putExtra(BoardActivity.PARAM_BOARD_TYPE, BoardActivity.TYPE_TIPS);
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
                int nonSmokingDays = (int) nonSmokingTime / (24 * 60 * 60 * 1000);
                int nonSmokingHours = (int) nonSmokingTime / (60 * 60 * 1000) - (nonSmokingDays * 24);
                int nonSmokingMins = (int) nonSmokingTime / (60 * 1000) - (nonSmokingDays * 24 * 60) - (nonSmokingHours * 60);
                int nonSmokingSecs = (int) nonSmokingTime / 1000 - (nonSmokingDays * 24 * 60 * 60) - (nonSmokingHours * 60 * 60) - (nonSmokingMins * 60);
                String s = String.format("%d일 %d시간 %d분 %d초", nonSmokingDays, nonSmokingHours, nonSmokingMins, nonSmokingSecs);
                timeView.setText(s);

                currentSaved = packPrice/20 * numOfCigar * nonSmokingDays;
                savedMoneyView.setText(currentSaved+" 원 절약");

            } else {
                timeView.setText("아직 금연을 시작하지 않으셨습니다");
            }
            mHandler.postDelayed(this, TIME_INTERVAL);
        }
    };


}