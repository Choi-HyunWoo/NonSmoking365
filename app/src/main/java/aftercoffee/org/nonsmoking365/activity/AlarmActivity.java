package aftercoffee.org.nonsmoking365.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import aftercoffee.org.nonsmoking365.R;
import aftercoffee.org.nonsmoking365.manager.PropertyManager;

public class AlarmActivity extends AppCompatActivity {

    RadioGroup alarmSettingView;
    LinearLayout timeSettingView;
    TimePicker timePicker;
    boolean isAlarmOn;
    Button okBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("알람 설정");
        actionBar.setElevation(0);

        alarmSettingView = (RadioGroup)findViewById(R.id.radioGroup);
        timeSettingView = (LinearLayout)findViewById(R.id.timeSettingView);
        timePicker = (TimePicker)findViewById(R.id.timePicker);
        okBtn = (Button)findViewById(R.id.btn_ok);

        isAlarmOn = PropertyManager.getInstance().getAlarmSetting();
        if (isAlarmOn) {
            alarmSettingView.check(R.id.radioBtn_alarmON);
            timeSettingView.setVisibility(View.VISIBLE);
        } else {
            alarmSettingView.check(R.id.radioBtn_alarmOFF);
            timeSettingView.setVisibility(View.GONE);
        }

        alarmSettingView.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioBtn_alarmON :
                        isAlarmOn = true;
                        timeSettingView.setVisibility(View.VISIBLE);
                        break;
                    case R.id.radioBtn_alarmOFF :
                        isAlarmOn = false;
                        timeSettingView.setVisibility(View.GONE);
                        break;
                    default:
                        break;
                }
            }
        });

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PropertyManager.getInstance().setAlarmSetting(isAlarmOn);
                int hour = 0;
                int min = 0;
                int currentApiVersion = android.os.Build.VERSION.SDK_INT;
                if (currentApiVersion > android.os.Build.VERSION_CODES.LOLLIPOP_MR1){
                    hour = timePicker.getHour();
                    min = timePicker.getMinute();
                } else {
                    hour = timePicker.getCurrentHour();
                    min = timePicker.getCurrentMinute();
                }
                Toast.makeText(AlarmActivity.this, hour+"시"+min+"분", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

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
