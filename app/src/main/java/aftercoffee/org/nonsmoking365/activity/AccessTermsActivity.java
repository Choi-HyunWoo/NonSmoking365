package aftercoffee.org.nonsmoking365.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import aftercoffee.org.nonsmoking365.R;

public class AccessTermsActivity extends AppCompatActivity {

    // Result extra
    public static final String RESULT_MESSAGE = "result_message";
    public static final String MESSAGE_AGREE = "agree";
    public static final String MESSAGE_DISAGREE = "disagree";

    Button agreeBtn, disagreeBtn, okBtn;

    public static final String EXTRA_FROMWHERE = "fromWhere";
    public static final int FROM_SIGNUP = 1;
    public static final int FROM_VERSIONINFO = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access_terms);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("이용 약관");
        actionBar.setElevation(0);

        // View initialize
        agreeBtn = (Button)findViewById(R.id.btn_agree);
        disagreeBtn = (Button)findViewById(R.id.btn_disagree);
        okBtn = (Button)findViewById(R.id.btn_ok);

        agreeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = new Intent();
                data.putExtra(RESULT_MESSAGE, MESSAGE_AGREE);
                setResult(Activity.RESULT_OK, data);
                finish();
            }
        });
        disagreeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = new Intent();
                data.putExtra(RESULT_MESSAGE, MESSAGE_DISAGREE);
                setResult(Activity.RESULT_OK, data);
                finish();
            }
        });
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        Intent intent = getIntent();
        int fromwhere = intent.getIntExtra(EXTRA_FROMWHERE, 0);
        if (fromwhere == FROM_SIGNUP) {
            okBtn.setVisibility(View.GONE);
            agreeBtn.setVisibility(View.VISIBLE);
            disagreeBtn.setVisibility(View.VISIBLE);
        } else {
            okBtn.setVisibility(View.VISIBLE);
            agreeBtn.setVisibility(View.GONE);
            disagreeBtn.setVisibility(View.GONE);
        }

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
