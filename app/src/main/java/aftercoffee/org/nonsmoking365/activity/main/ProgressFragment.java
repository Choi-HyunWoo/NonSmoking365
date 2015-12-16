package aftercoffee.org.nonsmoking365.activity.main;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import aftercoffee.org.nonsmoking365.activity.BasisInfoActivity;
import aftercoffee.org.nonsmoking365.manager.PropertyManager;
import aftercoffee.org.nonsmoking365.manager.UserManager;
import aftercoffee.org.nonsmoking365.R;
import aftercoffee.org.nonsmoking365.activity.community.CommunityActivity;
import aftercoffee.org.nonsmoking365.activity.ServiceInfoActivity;
import aftercoffee.org.nonsmoking365.activity.board.BoardActivity;
import aftercoffee.org.nonsmoking365.activity.centers.CentersActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProgressFragment extends Fragment {

    boolean isLogined;
    String userGrade;

    private static final long TIME_SEC = 1000;
    private static final long TIME_MIN = 60 * TIME_SEC;
    private static final long TIME_HOUR = 60 * TIME_MIN;
    private static final long TIME_DAY = 24 * TIME_HOUR;
    private static final long TIME_YEAR = 365 * TIME_DAY;

    TextView userGradeView, mottoView, timeView, savedMoneyView, healthGradeView;
    ImageView gradeImageView;

    int packPrice, numOfCigar, currentSaved;
    long startTime;
    long nonSmokingTime;

    public ProgressFragment() {
        // Required empty public constructor
        this.setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_progress, container, false);
        isLogined = UserManager.getInstance().getLoginState();      // 로그인 상태 확인

        // Initialize view
        userGradeView = (TextView)view.findViewById(R.id.text_userGradeView);
        gradeImageView = (ImageView)view.findViewById(R.id.image_gradeView);
        mottoView = (TextView)view.findViewById(R.id.text_mottoView);
        timeView = (TextView)view.findViewById(R.id.text_timeView);
        savedMoneyView = (TextView)view.findViewById(R.id.text_savedmoneyView);
        healthGradeView = (TextView)view.findViewById(R.id.text_healthGradeView);

        // 회원 등급
        userGradeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GradeInfoDialogFragment dlg = new GradeInfoDialogFragment();
                dlg.show(getActivity().getSupportFragmentManager(), "");
            }
        });


        // 금연 목표
        String motto = PropertyManager.getInstance().getBasisMotto();
        mottoView.setText(motto);
        mottoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BasisInfoActivity.class);
                intent.putExtra(BasisInfoActivity.START_MODE, BasisInfoActivity.MODE_MODIFY);
                startActivity(intent);
            }
        });

        // 금연 진행 시간, 절약 금액, 건강 상태 (Handler)
        startTime = PropertyManager.getInstance().getBasisStartTime();
        packPrice = Integer.parseInt(PropertyManager.getInstance().getBasisPackPrice());
        numOfCigar = Integer.parseInt(PropertyManager.getInstance().getBasisNumOfCigar());
        savedMoneyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nonSmokingTime > 0) {
                    Toast.makeText(getActivity(), "절약 금액은 한시간마다 업데이트 됩니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mHandler.removeCallbacks(updateRunnable);
        mHandler.post(updateRunnable);

        // Banner
        ImageView banner = (ImageView)view.findViewById(R.id.banner);
        banner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ServiceInfoActivity.class);
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

        // 건강상태
        healthGradeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nonSmokingTime > 0) {
                    HealthGradeDialogFragment dlg = new HealthGradeDialogFragment();
                    dlg.show(getActivity().getSupportFragmentManager(), "");
                }
            }
        });

        // 로그인 / 비로그인 구분
        setViewLogined();

        return view;
    }

    // 로그인 시 변화될 부분들
    // create, resume 시에 부르자.
    private void setViewLogined() {
        if (isLogined) {
            // 로그인 시
            gradeImageView.setVisibility(View.VISIBLE);
            userGrade = UserManager.getInstance().getUserGrade();       // 회원 등급
            if (userGrade.equals(UserManager.USER_GRADE_REWARD)) {
                // 리워드회원
                // Grade icon 생성, 배너 삭제, 리워드기간 표시..
                gradeImageView.setImageResource(R.drawable.icon_user_reward);
                userGradeView.setTextColor(getResources().getColor(R.color.colorRed));
            } else if (userGrade.equals(UserManager.USER_GRADE_NORMAL)) {
                // 일반회원
                // Grade icon 생성
                gradeImageView.setImageResource(R.drawable.icon_user_normal);
                userGradeView.setTextColor(getResources().getColor(R.color.colorAccent));
            }
        } else {
            // 비회원
            gradeImageView.setVisibility(View.INVISIBLE);
            userGradeView.setTextColor(getResources().getColor(R.color.colorAAAAAA));
            UserManager.getInstance().setUserGrade("비회원");
            userGrade = "비회원";
        }
        userGradeView.setText(userGrade);
    }

    Handler mHandler = new Handler();
    private static final int TIME_INTERVAL = 1000;

    Runnable updateRunnable = new Runnable() {
        @Override
        public void run() {
            // 시간 계산
            nonSmokingTime = System.currentTimeMillis() - startTime;

            // 뷰 설정
            if (nonSmokingTime > 0) {
                // 현재시간 설정
                int nonSmokingDays = (int) (nonSmokingTime / TIME_DAY);
                int nonSmokingHours= (int) (nonSmokingTime / TIME_HOUR) - (nonSmokingDays * 24);
                int nonSmokingMins = (int) (nonSmokingTime / TIME_MIN) - (nonSmokingDays * 24 * 60) - (nonSmokingHours * 60);
                int nonSmokingSecs = (int) (nonSmokingTime / TIME_SEC) - (nonSmokingDays * 24 * 60 * 60) - (nonSmokingHours * 60 * 60) - (nonSmokingMins * 60);
                if (nonSmokingDays==0 && nonSmokingHours==0 && nonSmokingMins==0) {
                    String s = String.format("%d초", nonSmokingSecs);
                    timeView.setText(s);
                } else if (nonSmokingDays==0 && nonSmokingHours==0) {
                    String s = String.format("%d분 %d초", nonSmokingMins, nonSmokingSecs);
                    timeView.setText(s);
                } else if (nonSmokingDays==0) {
                    String s = String.format("%d시간 %d분 %d초",nonSmokingHours, nonSmokingMins, nonSmokingSecs);
                    timeView.setText(s);
                } else {
                    String s = String.format("%d일 %d시간 %d분 %d초", nonSmokingDays, nonSmokingHours, nonSmokingMins, nonSmokingSecs);
                    timeView.setText(s);
                }

                // 절약금액
                currentSaved = (int)
                          ((float)packPrice/20 * numOfCigar * nonSmokingDays                    // 한 개비 가격 * 1일 흡연량(개비) * 금연날짜
                        + ((float)packPrice/20 * (float)numOfCigar/24) * nonSmokingHours);      // 한 개비 가격 * 시간당 흡연량(개비) * 금연시간
                currentSaved = (currentSaved/100)*100;
                savedMoneyView.setText("담배값 "+NumberFormat.getNumberInstance(Locale.US).format(currentSaved)+" 원 절약중");

                // 건강 상태 (12단계 등급)
                if (nonSmokingTime > 15*TIME_YEAR) {
                    healthGradeView.setText("1등급");
                }else if (nonSmokingTime > 10*TIME_YEAR) {
                    healthGradeView.setText("2등급");
                }else if (nonSmokingTime > 5*TIME_YEAR) {
                    healthGradeView.setText("3등급");
                }else if (nonSmokingTime > TIME_YEAR) {
                    healthGradeView.setText("4등급");
                }else if (nonSmokingTime > 180 * TIME_DAY) {
                    healthGradeView.setText("5등급");
                }else if (nonSmokingTime > 14 * TIME_DAY) {
                    healthGradeView.setText("6등급");
                }else if (nonSmokingTime > 72 * TIME_HOUR) {
                    healthGradeView.setText("7등급");
                }else if (nonSmokingTime > 48 * TIME_HOUR) {
                    healthGradeView.setText("8등급");
                }else if (nonSmokingTime > 24 * TIME_HOUR) {
                    healthGradeView.setText("9등급");
                }else if (nonSmokingTime > 8 * TIME_HOUR) {
                    healthGradeView.setText("10등급");
                }else if (nonSmokingTime > 2 * TIME_HOUR) {
                    healthGradeView.setText("11등급");
                }else if (nonSmokingTime > 20 * TIME_MIN) {
                    healthGradeView.setText("12등급");
                }else {
                    healthGradeView.setText("13등급");
                }
            } else {
                timeView.setText("아직 금연을 시작하지 않으셨습니다.");
                savedMoneyView.setText("금연 시작하시고 담배값을 절약하세요!");
                healthGradeView.setText("13등급");
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
        isLogined = UserManager.getInstance().getLoginState();
        setViewLogined();
        String motto = PropertyManager.getInstance().getBasisMotto();
        mottoView.setText(motto);
        mHandler.removeCallbacks(updateRunnable);
        mHandler.post(updateRunnable);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_progress_fragment, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_startTime_reset :
                // Dialog 띄운 후 현재시간으로 리셋
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setIcon(R.drawable.icon_logo_black);
                builder.setTitle("금연 중 담배를 피셨습니까?");
                builder.setMessage("금연 시작 시간을 현재 시간부터로 변경합니다.");
                builder.setNeutralButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(), "지금부터 금연합시다!", Toast.LENGTH_SHORT).show();
                        startTime = System.currentTimeMillis();
                        PropertyManager.getInstance().setBasisStartTime(startTime);
                    }
                });
                builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dlg = builder.create();
                dlg.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}