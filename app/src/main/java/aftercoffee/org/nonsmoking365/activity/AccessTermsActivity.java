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

    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access_terms);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("이용 약관");
        actionBar.setElevation(0);

        btn = (Button)findViewById(R.id.btn_agree);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = new Intent();
                data.putExtra(RESULT_MESSAGE, MESSAGE_AGREE);
                setResult(Activity.RESULT_OK, data);
                finish();
            }
        });
        btn = (Button)findViewById(R.id.btn_disagree);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = new Intent();
                data.putExtra(RESULT_MESSAGE, MESSAGE_DISAGREE);
                setResult(Activity.RESULT_OK, data);
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
