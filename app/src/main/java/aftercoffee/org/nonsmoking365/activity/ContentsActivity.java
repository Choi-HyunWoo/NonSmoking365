package aftercoffee.org.nonsmoking365.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import aftercoffee.org.nonsmoking365.R;

public class ContentsActivity extends AppCompatActivity {

    TextView titleVIew, contentsView;
    String title;                   // 글 제목
    String contents;                // 글 내용

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contents);

        titleVIew = (TextView)findViewById(R.id.text_title);
        contentsView = (TextView)findViewById(R.id.text_contents);

        Intent data = getIntent();
        title = data.getStringExtra("title");
        contents = data.getStringExtra("contents");

        titleVIew.setText(title);
        contentsView.setText(contents);

        Button btn = (Button) findViewById(R.id.btn_comments);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContentsActivity.this, CommentsActivity.class);
                intent.putExtra("title", title);
                startActivity(intent);
            }
        });
    }
}
