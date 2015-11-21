package aftercoffee.org.nonsmoking365.tt.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import aftercoffee.org.nonsmoking365.tt.activity.main.MainActivity;
import aftercoffee.org.nonsmoking365.tt.manager.PropertyManager;
import aftercoffee.org.nonsmoking365.R;

public class BasisInfoActivity extends AppCompatActivity {

    public static final String START_MODE = "start_mode";
    public static final int MODE_INIT = 1;
    public static final int MODE_MODIFY = 2;
    int startMode;

    EditText mottoView;
    EditText numOfCigarView;
    EditText packPriceView;
    Button startDateBtn;
    Button birthDateBtn;
    RadioGroup genderView;

    /* Current time */
    int year, month, day, hour, minute;
    GregorianCalendar calendar;                 // 날짜 연산을 위한 calendar

    /* Basis info */
    String motto;
    long startTime;
    String startTimeToString;
    String numOfCigar;
    String packPrice;
    String gender;                              // male , female
    String birthDate;

    InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basis_info);
        /** START MODE SETTING
         * 사용자가 App을 최초로 켰을 때 Basis info창이 뜬 경우 >> START_MODE = MODE_INIT
         * 사용자가 설정 화면에서 기초정보 수정으로 접근한 경우 >> START_MODE = MODE_MODIFY
         * */
        // getIntent로 이전 화면이 Preview라면 (MODE_INIT이 전달됬다면) , 기초정보 최초의 입력 모드일것이다.
        Intent data = getIntent();
        startMode = data.getIntExtra(START_MODE, 0);
        /** (ISSUE) 기초정보 최초 입력 시, 입력을 안하고 App을 껏다 켠 경우 >> 기초정보를 안받아버림,
         *  SharedPreference에서 BasisInfo가 최초로 입력되었는지 확인 후 startMode를 INIT상태로 변경
         */
        if (!PropertyManager.getInstance().getBasisInfoCheck()) {
            startMode = MODE_INIT;
        }

        // Action Bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setElevation(0);
        if (startMode == MODE_INIT) {
            actionBar.setTitle("기초정보 입력");
            actionBar.setDisplayHomeAsUpEnabled(false);
        } else if (startMode == MODE_MODIFY) {
            actionBar.setTitle("기초정보 수정");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // View initialize
        mottoView = (EditText)findViewById(R.id.edit_motto);
        startDateBtn = (Button)findViewById(R.id.btn_startDate);
        numOfCigarView = (EditText)findViewById(R.id.edit_numOfCigar);
        packPriceView = (EditText)findViewById(R.id.edit_packPrice);
        birthDateBtn = (Button)findViewById(R.id.btn_birthDate);
        genderView = (RadioGroup)findViewById(R.id.radiogroup_gender);

        // MODE_MODIFY 일 경우 저장해둔 기초정보 데이터를 화면에 표시
        if (startMode == MODE_MODIFY) {
            motto = PropertyManager.getInstance().getBasisMotto();
            startTime = PropertyManager.getInstance().getBasisStartTime();
            startTimeToString = timeToString(startTime);
            numOfCigar = PropertyManager.getInstance().getBasisNumOfCigar();
            packPrice = PropertyManager.getInstance().getBasisPackPrice();
            gender = PropertyManager.getInstance().getBasisGender();
            birthDate = PropertyManager.getInstance().getBasisBirthDate();

            mottoView.setText(motto);
            startDateBtn.setText(startTimeToString);
            numOfCigarView.setText(numOfCigar);
            packPriceView.setText(packPrice);
            if (gender.equals("male")) {
                genderView.check(R.id.radioBtn_male);
            } else {
                genderView.check(R.id.radioBtn_female);
            }
            birthDateBtn.setText(birthDate);
        }

        // 성별 선택 Listener
        genderView.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioBtn_male :
                        gender = "male";
                        break;
                    case R.id.radioBtn_female :
                        gender = "female";
                        break;
                    default:
                        break;
                }
            }
        });

        // DatePicker를 현재 날짜로 지정하기 위해 Calendar에서 날짜 및 시간을 받아온다.
        calendar = new GregorianCalendar();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);               // 0~11
        day= calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);

        // Keyboard 컨트롤
        imm = (InputMethodManager) getSystemService(BasisInfoActivity.this.INPUT_METHOD_SERVICE);

        // 시작 날짜 선택
        startDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imm.hideSoftInputFromWindow(BasisInfoActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                DatePickerDialog dlg = new DatePickerDialog(BasisInfoActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar c = Calendar.getInstance();
                        c.set(year, monthOfYear, dayOfMonth, 0, 0, 0);
                        c.set(Calendar.MILLISECOND, 0);
                        startTime = c.getTimeInMillis();
                        startTimeToString = timeToString(startTime);
                        startDateBtn.setText(startTimeToString);
                    }
                }, year, month, day);
                dlg.show();
            }
        });

        // 생년 월일 선택
        birthDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imm.hideSoftInputFromWindow(BasisInfoActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                DatePickerDialog dlg = new DatePickerDialog(BasisInfoActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String msg = String.format("%d-%d-%d", year,monthOfYear+1, dayOfMonth);
                        birthDate = msg;
                        birthDateBtn.setText(msg);
                    }
                }, year, month, day);
                if (Build.VERSION.SDK_INT >= 11) {
                    /**
                     * (On API levels under 11 where getDatePicker() and setCalendarViewShown() are not available it does not matter - there's no CalendarView in the dialog anyway.)
                     */
                    dlg.getDatePicker().setCalendarViewShown(false);
                    dlg.getDatePicker().setMaxDate(System.currentTimeMillis());
                }
                dlg.show();
            }
        });

        // 완료 버튼
        // > 기초정보 최초의 입력 시 splash 이후에 기초정보창이 안뜨도록 SharedPreference의 BasisInfoCheck를 true로 변경
        // > 최초의 입력이 아니라면 기초정보만 수정후 SharedPreference에 내용 저장
        Button btn = (Button) findViewById(R.id.btn_finish);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 변수에 작성내용을 임시 저장
                motto = mottoView.getText().toString();
                numOfCigar = numOfCigarView.getText().toString();
                packPrice = packPriceView.getText().toString();

                // 1. 입력된 부분이 빠진곳이 있는지 체크
                if (TextUtils.isEmpty(motto)) {
                    Toast.makeText(BasisInfoActivity.this, "금연 목표를 입력해 주세요", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(startTimeToString)) {
                    Toast.makeText(BasisInfoActivity.this, "금연 시작일을 지정해 주세요", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(numOfCigar)) {
                    Toast.makeText(BasisInfoActivity.this, "1일 흡연량을 입력해 주세요", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(packPrice)) {
                    Toast.makeText(BasisInfoActivity.this, "담배 가격을 입력해 주세요", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(gender)) {
                    Toast.makeText(BasisInfoActivity.this, "성별을 입력해 주세요", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(birthDate)) {
                    Toast.makeText(BasisInfoActivity.this, "생년월일을 입력해 주세요", Toast.LENGTH_SHORT).show();
                }
                else {        // 모두 제대로 작성되었다면
                    // 2. 입력사항을 SharedPreferences에 저장
                    PropertyManager.getInstance().setBasisMotto(motto);
                    PropertyManager.getInstance().setBasisStartTime(startTime);
                    PropertyManager.getInstance().setBasisNumOfCigar(numOfCigar);
                    PropertyManager.getInstance().setBasisPackPrice(packPrice);
                    PropertyManager.getInstance().setBasisGender(gender);
                    PropertyManager.getInstance().setBasisBirthDate(birthDate);

                    if (startMode == MODE_INIT) {                                                   // 기초정보 최초의 입력이라면
                        PropertyManager.getInstance().setBasisInfoCheck(true);                      // splash 이후에도 기초정보창이 안뜨도록 변경
                        PropertyManager.getInstance().setCountItemReset();                          // 또한 금연 카운트 정보를 초기화
                        Intent intent = new Intent(BasisInfoActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {                                                                        // 기초정보 수정이라면
                        Toast.makeText(BasisInfoActivity.this, "기초정보가 수정되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                    finish();
                }

            }
        });
    }

    private String timeToString (long time) {
        Calendar calendar = Calendar.getInstance(Locale.KOREA);
        calendar.setTimeInMillis(time);
        String timeToString = String.format("%d-%d-%d", calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)+1, calendar.get(Calendar.DAY_OF_MONTH));
        return timeToString;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

