package aftercoffee.org.nonsmoking365.activity.main;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import aftercoffee.org.nonsmoking365.activity.CountResultActivity;
import aftercoffee.org.nonsmoking365.manager.PropertyManager;
import aftercoffee.org.nonsmoking365.manager.UserManager;
import aftercoffee.org.nonsmoking365.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CountFragment extends Fragment {

    boolean isLogined;

    LinearLayout layout;
    GridView countGridView;
    CountGridAdapter mAdapter;
    TextView countStartView;
    TextView countPositionView;
    TextView countRestView;

    public CountFragment() {
        // Required empty public constructor
        this.setHasOptionsMenu(true);
    }

    long startTime;     // 금연 카운팅 시작시간 변수

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_count, container, false);
        isLogined = UserManager.getInstance().getLoginState();

        // View initialize
        layout = (LinearLayout)view.findViewById(R.id.layout);
        countGridView = (GridView) view.findViewById(R.id.gridView);
        mAdapter = new CountGridAdapter();
        countGridView.setAdapter(mAdapter);
        countStartView = (TextView) view.findViewById(R.id.text_countStartDate);
        countPositionView = (TextView) view.findViewById(R.id.text_countPosition);
        countRestView = (TextView) view.findViewById(R.id.text_countRestTime);

        /** * * * COUNT Programming GUIDE * * * ***
         *
         *  Info)
         *  사용자가 금연 카운트 중이다 / 카운트 중이 아니다를 startTime으로 판별
         *  startTime은 카운트가 진행중이지 않을 경우 기본값 -1을 가지고, 카운트가 시작되면 startTime이 현재 시간으로 SP와 필드에 저장. 저장된 시작시간을 기준으로 카운트가 진행됨.
         *  사용자는 매일 0시(midnight)에 생성되는 "ON Button"을 클릭하여 오늘의 금연 성공/실패 여부를 선택할 수 있다,
         *  사용자가 금연 성공을 선택하면 "O Button"으로, 금연 실패를 선택하면 "X Button"으로 버튼의 Mode가 변경된다.
         *  ON 버튼이나 이미 선택한 O,X버튼이 아닌 나머지 버튼들은 "OFF 버튼"으로 닫혀 있어서 사용자가 클릭할 수 없다. (클릭 시 다음 카운트 시간 출력. "다음 카운트까지 ~~시간 남았습니다.")
         *
         *  App 구동시 초기화 해야 할 정보 (SharedPreference; PropertyManager를 통해 가져올 내용)
         *  - SP에 저장했던 카운트 시작 시간 (startTime)
         *  - SP에 저장했던 뷰 정보 ; ON , OFF, O, X (사용자가 O와 X중 무엇을 선택했는지 기억해야 하므로 이 정보도 담겨있어야함)
         *
         *  Handler에서 수행할 연산
         *  - 오늘이 Count 몇일차인지 CountPosition 구하기 (현재 시간 - 카운트 시작시간) (......GRIDVIEW index역할)
         *  - NextMidnight(다음날 자정 시간)을 구하고, NextMidnight까지 남은 시간을 구하기.
         *  - NextMidNight이 됬을 때 해당 CountPosition에 있는 버튼 뷰를 ON으로 변경하기.
         *
         *  버튼 선택시 수행할 연산
         *  - ON 버튼 : 진행중인 경우) Dialog를 띄우고 금연 성공/실패 여부를 묻는다.
         *                             성공 클릭 시 O Button으로 바뀜. (Adapter에게 알려서 ViewMode를 변경하고 SP에게 저장)
         *                             실패 클릭 시 X Button으로 바뀜. (Adapter에게 알려서 ViewMode를 변경하고 SP에게 저장)
         *                             ====================================================================================
         *         최초의 입력인 경우) 카운트 시작시간(startTime)을 현재 시간으로 (System.currentMillies) 설정하고, SP에 저장한다.
         *         마지막 버튼인 경우) 금연 카운트 결과 Activity를 불러온다. (자신이 Count한 결과가 캐릭터 Animation과 함께 출력. 상품 응모 버튼이 있다.)
         *                             Counting을 종료(startTime = -1)하고 시작 화면으로 돌아온다.
         *  - OFF 버튼 : 선택 불가능, 클릭 시 NextMidNight까지 남은 시간을 Toast로 출력
         *  - O 버튼 : 진행현황 화면으로 이동한다. (자신이 Counting하고있는 결과가 나온다. 평균XX, 캐릭터 수집 화면.,)
         *  - X 버튼 : 진행현황 화면으로 이동한다. ( ,, )
         *  - RESET 버튼 : SP에 저장해두었던 내용들이 리셋되고 뷰를 갱신한다.
         *                 startTime = -1     ...SP에 저장
         *                 GridView setting이 0번인덱스만 ON으로, 나머진 OFF로. Adapter에서 바꾸고 SP에도 저장
         *  - START 버튼 : 애니메이션을 넣어서 만들지?.. 미구현
         *
         *
         */

        /// APP 구동시 INIT (SP)
        startTime = getStartTime(); // startTime을 SP에서 가져옴
        if (startTime == -1) {
            mAdapter.resetCount();  // 카운트중이지 않다면 reset
        }
        mAdapter.initCount();       // List를 clear하고 SP에서 가져온 ITEM MODE들을 바탕으로 뷰 초기화.

        // Count Btn Click Listener
        countGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                CountItem c = (CountItem) mAdapter.getItem(position);
                switch (c.itemMode) {
                    case CountItem.MODE_ON :
                        // 카운팅
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("금연 카운트");
                        builder.setMessage((position + 1) + "일차 금연에 성공하셨습니까?");

                        builder.setPositiveButton("성공", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (getStartTime() == -1) {                     // 금연 카운트가 첫 시작이라면
                                    startTime = System.currentTimeMillis();     // 시작시간 설정후
                                    setStartTime(startTime);                    // SP에 저장
                                    mHandler.post(countRunnable);               // Handler 구동 시작
                                }
                                // 저장된 성공 횟수를 1 증가
                                PropertyManager.getInstance().setCountSuccess(PropertyManager.getInstance().getCountSuccess()+1);
                                mAdapter.setModeO(position);
                            }
                        });
                        builder.setNegativeButton("실패", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (getStartTime() == -1) {                     // 금연 카운트가 첫 시작이라면
                                    startTime = System.currentTimeMillis();     // 시작시간 설정후
                                    setStartTime(startTime);                    // SP에 저장
                                    mHandler.post(countRunnable);               // Handler 구동 시작
                                }
                                // 저장된 실패 횟수를 1 증가
                                PropertyManager.getInstance().setCountFailure(PropertyManager.getInstance().getCountFailure() + 1);
                                mAdapter.setModeX(position);
                            }
                        });
                        AlertDialog dlg = builder.create();
                        dlg.show();
                        break;
                    case CountItem.MODE_OFF :
                        // 카운트 남은시간 알림
                        if (startTime != -1) {
                            Toast.makeText(getActivity(), "다음 카운트까지 " + getRestTime()[0] + "시간 " + getRestTime()[1] + " 분 " + getRestTime()[2] + " 초 남았습니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "1일차 버튼부터 카운트를 진행하세요!", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case CountItem.MODE_O :
                        // 카운트 결과창으로
                        Intent intent = new Intent(getActivity(), CountResultActivity.class);
                        intent.putExtra("clickPosition", position);
                        intent.putExtra("clickDayInfo", CountResultActivity.IS_SUCCESS);
                        startActivity(intent);
                        break;
                    case CountItem.MODE_X :
                        // 카운트 결과창으로
                        intent = new Intent(getActivity(), CountResultActivity.class);
                        intent.putExtra("clickPosition", position);
                        intent.putExtra("clickDayInfo", CountResultActivity.IS_FAILURE);
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        });

        /*
        // SET ALL ON (TEST)
        Button btn = (Button)view.findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.setAllOn();
            }
        });
        */

        return view;
    }

    // 시작시간 설정
    private void setStartTime(long startTime) {
        PropertyManager.getInstance().setCountStartTime(startTime);
    }

    // 시작시간 가져오기
    private long getStartTime() {
        return PropertyManager.getInstance().getCountStartTime();
    }

    // 시작시간을 String으로 (long >> String)
    private String startTimeToString() {
        Date date = new Date(startTime);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String startTimeStr = sdf.format(date);
        return startTimeStr;
    }

    // 내일 자정 시간 계산 (long)
    private long getNextMidnight() {
        Calendar c = Calendar.getInstance(Locale.KOREA);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        c.add(Calendar.DAY_OF_YEAR, 1);
        long nextMidnight = c.getTimeInMillis();
        return nextMidnight;
    }

    // 내일 자정(다음 카운트)까지 남은 시간 계산
    // 현재 시간에서 다음 자정까지의 기간(countRestTerm)을 구하고, 시/분/초 단위로 쪼갠다.
    private int[] getRestTime() {           // return Hours, Minutes, Seconds
        int[] restTimeArray = new int[3];
        long countRestTime = getNextMidnight() - System.currentTimeMillis();
        restTimeArray[0] = (int) countRestTime / (60 * 60 * 1000);                                                  // restHours
        restTimeArray[1] = (int) countRestTime / (60 * 1000) - (restTimeArray[0] * 60);                             // restMinutes
        restTimeArray[2] = (int) countRestTime / 1000 - (restTimeArray[0] * 60 * 60) - (restTimeArray[1] * 60);     // restSeconds
        return restTimeArray;
    }

    // 카운트가 정지 상태일 때 TextView 출력
    private void setTextCountStop() {
        countRestView.setText("");
        countStartView.setText("금연 카운트를 시작해 주세요");
        countPositionView.setText("");
    }

    // 핸들러
    Handler mHandler = new Handler();
    private static final int TIME_INTERVAL = 1000;
    Runnable countRunnable = new Runnable() {
        @Override
        public void run() {
            // 오늘의 Count Position 계산
            // 시작시간부터 다음 자정까지의 기간(countingTerm)을 구하고, 일단위로 나눈다.
            long countingTerm = getNextMidnight() - startTime;
            int todayCountPos = (int) countingTerm / (24 * 60 * 60 * 1000);

            int restTimeArray[] = getRestTime();

            // View에 계산 결과를 출력
            countRestView.setText("다음 카운트까지 " + restTimeArray[0] + "시간 " + restTimeArray[1] + " 분 " + restTimeArray[2] + " 초 남았습니다.");
            countPositionView.setText("금연 카운트 " + (todayCountPos + 1) + "일차");
            countStartView.setText("금연 카운트 시작일 : " + startTimeToString());

            // 자정이 됬을 때, 다음 카운트 버튼을 ON Button으로 활성화.
            if (mAdapter.isModeOff(todayCountPos) && todayCountPos <= mAdapter.COUNT_ITEMS_MAX_POSITION) {
                mAdapter.setModeOn(todayCountPos);
            }

            mHandler.postDelayed(this, TIME_INTERVAL);
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        mHandler.removeCallbacks(countRunnable);
        if (startTime != -1) {
            mHandler.post(countRunnable);
        } else {
            setTextCountStop();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        isLogined = UserManager.getInstance().getLoginState();
        mHandler.removeCallbacks(countRunnable);
        if (startTime != -1) {
            mHandler.post(countRunnable);
        } else {
            setTextCountStop();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mHandler.removeCallbacks(countRunnable);
    }

    @Override
    public void onStop() {
        super.onStop();
        mHandler.removeCallbacks(countRunnable);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_count_fragment, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_count_reset :
                if (startTime == -1) {
                    Toast.makeText(getActivity(), "이 버튼은 카운트 초기화 버튼입니다.\n아직 금연 카운트가 시작되지 않았습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setIcon(R.drawable.icon_logo_black);
                    builder.setTitle("금연 카운트 초기화");
                    builder.setMessage("금연 카운트를 초기화하시겠습니까?\n현재까지의 카운트 정보가 모두 삭제됩니다.");
                    builder.setNeutralButton("초기화", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 금연 카운트 정지 (reset)
                            startTime = -1;                             // 시작시간을 -1로
                            setStartTime(startTime);                    // SP에 저장
                            mAdapter.resetCount();                      // SP의 내용을 reset하고, GridView에 보여지는 Item을 reset
                            PropertyManager.getInstance().setCountSuccess(0);
                            PropertyManager.getInstance().setCountFailure(0);

                            mHandler.removeCallbacks(countRunnable);    // Handler 정지

                            // NOTICE RESET, set TextView
                            Toast.makeText(getActivity(), "금연 카운트가 초기화되었습니다.", Toast.LENGTH_SHORT).show();

                            // 카운트 정지상태에 보여줄 text로 TextView 세팅!
                            setTextCountStop();
                        }
                    });
                    builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dlg = builder.create();
                    dlg.show();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}