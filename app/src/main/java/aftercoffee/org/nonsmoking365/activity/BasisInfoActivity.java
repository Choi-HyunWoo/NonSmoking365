package aftercoffee.org.nonsmoking365.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import aftercoffee.org.nonsmoking365.PropertyManager;
import aftercoffee.org.nonsmoking365.R;

public class BasisInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basis_info);

        Button btn = (Button) findViewById(R.id.btn_basisInfoCheck);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PropertyManager.getInstance().setBasisInfoCheck(true);
                Intent intent = new Intent(BasisInfoActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
