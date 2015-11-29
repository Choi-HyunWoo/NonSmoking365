package aftercoffee.org.nonsmoking365.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import aftercoffee.org.nonsmoking365.R;
import aftercoffee.org.nonsmoking365.manager.NetworkManager;
import aftercoffee.org.nonsmoking365.manager.UserManager;

public class WithdrawActivity extends AppCompatActivity {

    RadioGroup reasonGroup;
    EditText etcView;
    Button submitBtn;

    String reason;
    boolean isEtc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setElevation(0);
        actionBar.setTitle("회원 탈퇴");

        reasonGroup = (RadioGroup)findViewById(R.id.reason_group);
        etcView = (EditText)findViewById(R.id.edit_etc);
        etcView.setVisibility(View.INVISIBLE);
        submitBtn = (Button)findViewById(R.id.btn_submit);

        reasonGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_reason_success :
                        etcView.setVisibility(View.INVISIBLE);
                        reason = "금연 성공";
                        isEtc = false;
                        break;
                    case R.id.radio_reason_badservice :
                        etcView.setVisibility(View.INVISIBLE);
                        reason = "서비스 불만족";
                        isEtc = false;
                        break;
                    case R.id.radio_reason_etc :
                        etcView.setVisibility(View.VISIBLE);
                        isEtc = true;
                        break;
                }
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEtc) {
                    reason = etcView.getText().toString();
                }
                if (TextUtils.isEmpty(reason)) {
                    Toast.makeText(WithdrawActivity.this, "탈퇴 사유를 입력해 주세요", Toast.LENGTH_SHORT).show();
                } else {
                    NetworkManager.getInstance().postWithdraw(WithdrawActivity.this, reason, new NetworkManager.OnResultListener<String>() {
                        @Override
                        public void onSuccess(String result) {
                            Toast.makeText(WithdrawActivity.this, "회원탈퇴가 완료되었습니다.", Toast.LENGTH_SHORT).show();
                            UserManager.getInstance().logoutClear();
                        }
                        @Override
                        public void onFail(int code) {
                            Log.d("Network ERROR:", "withdraw" + code);
                        }
                    });

                }
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
