package aftercoffee.org.nonsmoking365.activity.basisinfo;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import aftercoffee.org.nonsmoking365.activity.main.MainActivity;
import aftercoffee.org.nonsmoking365.PropertyManager;
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
    String startDate;
    String numOfCigar;
    String packPrice;
    String gender;                              // male , female
    String birthDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basis_info);
        /** START MODE SETTING */
        Intent data = getIntent();
        startMode = data.getIntExtra(START_MODE, 0);

        // View init
        mottoView = (EditText)findViewById(R.id.edit_motto);
        startDateBtn = (Button)findViewById(R.id.btn_startDate);
        numOfCigarView = (EditText)findViewById(R.id.edit_numOfCigar);
        packPriceView = (EditText)findViewById(R.id.edit_packPrice);
        birthDateBtn = (Button)findViewById(R.id.btn_birthDate);
        genderView = (RadioGroup)findViewById(R.id.radiogroup_gender);

        // "기초정보 수정" 일경우 임시 변수에 데이터를 저장하고 , 화면에 표시
        if (startMode == MODE_MODIFY) {
            motto = PropertyManager.getInstance().getBasisMotto();
            startDate = PropertyManager.getInstance().getBasisStartDate();
            numOfCigar = PropertyManager.getInstance().getBasisNumOfCigar();
            packPrice = PropertyManager.getInstance().getBasisPackPrice();
            gender = PropertyManager.getInstance().getBasisGender();
            birthDate = PropertyManager.getInstance().getBasisBirthDate();

            mottoView.setText(motto);
            startDateBtn.setText(startDate);
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
        // 시작 날짜 선택
        startDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dlg = new DatePickerDialog(BasisInfoActivity.this, startDateSetListener, year, month, day);
                dlg.show();
            }
        });
        // 생년 월일 선택
        birthDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dlg = new DatePickerDialog(BasisInfoActivity.this, birthDateSetListener, year, month, day);
                if (Build.VERSION.SDK_INT >= 11) {
                    dlg.getDatePicker().setMaxDate(System.currentTimeMillis());
                }
                /**
                 * (On API levels under 11 where getDatePicker() and setCalendarViewShown() are not available it does not matter - there's no CalendarView in the dialog anyway.)
                 */
                dlg.show();
            }
        });

        /// Finish Button
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
                } else if (TextUtils.isEmpty(startDate)) {
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
                    PropertyManager.getInstance().setBasisStartDate(startDate);
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


    /* DatePickerDialog의 결과값을 받을때 호출 */
    private DatePickerDialog.OnDateSetListener startDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            // TODO Auto-generated method stub
            String msg = String.format("%d-%d-%d", year, monthOfYear+1, dayOfMonth);
            startDate = msg;
            startDateBtn.setText(msg);
        }
    };
    private DatePickerDialog.OnDateSetListener birthDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            // TODO Auto-generated method stub
            String msg = String.format("%d-%d-%d", year,monthOfYear+1, dayOfMonth);
            birthDate = msg;
            birthDateBtn.setText(msg);
        }
    };
}
