package aftercoffee.org.nonsmoking365.activity.basisinfo;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

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

    /* Current time */
    int year, month, day, hour, minute;
    GregorianCalendar calendar;                 // 날짜 연산을 위한 calendar

    /* Basis info */
    String motto;
    String startDate;
    String numOfCigar;
    String packPrice;
    String gender;
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
        // genderView !!

        // "기초정보 수정" 일경우 저장되있던 데이터를 화면에 표시한다.
        if (startMode == MODE_MODIFY) {
            mottoView.setText(PropertyManager.getInstance().getBasisMotto());
            startDateBtn.setText(PropertyManager.getInstance().getBasisStartDate());
            numOfCigarView.setText(PropertyManager.getInstance().getBasisNumOfCigar());
            packPriceView.setText(PropertyManager.getInstance().getBasisPackPrice());
            birthDateBtn.setText(PropertyManager.getInstance().getBasisBirthDate());
        }

        // 현재 날짜 및 시간을 받아온다.
        calendar = new GregorianCalendar();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);               // 0~11
        day= calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        // 날짜 선택
        startDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(BasisInfoActivity.this, startDateSetListener, year, month, day).show();
            }
        });
        birthDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(BasisInfoActivity.this, birthDateSetListener, year, month, day).show();
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
                // gender ;

                // 1. 작성되었는지 체크
                if (TextUtils.isEmpty(motto)) {
                    Toast.makeText(BasisInfoActivity.this, "금연 목표를 입력해 주세요", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(startDate)) {
                    Toast.makeText(BasisInfoActivity.this, "금연 시작일을 지정해 주세요", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(numOfCigar)) {
                    Toast.makeText(BasisInfoActivity.this, "1일 흡연량을 입력해 주세요", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(packPrice)) {
                    Toast.makeText(BasisInfoActivity.this, "담배 가격을 입력해 주세요", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(birthDate)) {
                    Toast.makeText(BasisInfoActivity.this, "생년월일을 입력해 주세요", Toast.LENGTH_SHORT).show();
                } else {        // 모두 제대로 작성되었다면

                    // 2. SharedPreferences에 저장
                    PropertyManager.getInstance().setBasisMotto(motto);
                    PropertyManager.getInstance().setBasisStartDate(startDate);
                    PropertyManager.getInstance().setBasisNumOfCigar(numOfCigar);
                    PropertyManager.getInstance().setBasisPackPrice(packPrice);
                    PropertyManager.getInstance().setBasisBirthDate(birthDate);

                    if (startMode == MODE_INIT) {                                                   // 최초의 입력이라면
                        PropertyManager.getInstance().setBasisInfoCheck(true);                      // splash 이후에도 기초정보창이 안뜨도록 변경
                        Intent intent = new Intent(BasisInfoActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
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
            String msg = String.format("%d-%d-%d", year,monthOfYear+1, dayOfMonth);
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
