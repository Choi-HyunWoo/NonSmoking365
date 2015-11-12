package aftercoffee.org.nonsmoking365.Question;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import aftercoffee.org.nonsmoking365.Manager.NetworkManager;
import aftercoffee.org.nonsmoking365.R;

public class QuestionActivity extends AppCompatActivity {

    EditText titleView;
    EditText contentView;

    String title;
    String content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("문의하기");
        actionBar.setElevation(0);

        titleView = (EditText)findViewById(R.id.edit_title);
        contentView = (EditText)findViewById(R.id.edit_content);

        // 제목 변경을 원하면 (제목이 클릭되면) 제목 빈칸으로
        titleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleView.setText("");
            }
        });

        Button btn = (Button)findViewById(R.id.btn_finish);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = titleView.getText().toString();
                content = contentView.getText().toString();
                if (TextUtils.isEmpty(title)) {
                    Toast.makeText(QuestionActivity.this, "제목을 입력해 주세요", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(content)) {
                    Toast.makeText(QuestionActivity.this, "문의 내용을 입력해 주세요", Toast.LENGTH_SHORT).show();
                } else {
                    /**
                     * 로그인 정보를 받아온다 (email)
                     */

                    AlertDialog.Builder builder = new AlertDialog.Builder(QuestionActivity.this);
                    builder.setMessage("문의 글을 등록하시겠습니까?");
                    builder.setTitle("문의하기");
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            NetworkManager.getInstance().postQuestionData(QuestionActivity.this, "id_dummy", title, content, new NetworkManager.OnResultListener<Question>() {
                                @Override
                                public void onSuccess(Question result) {
                                    Toast.makeText(QuestionActivity.this, "문의 글이 등록되었습니다.", Toast.LENGTH_SHORT).show();
                                    finish();
                                }

                                @Override
                                public void onFail(int code) {
                                    Toast.makeText(QuestionActivity.this, "Network connect failed", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                    AlertDialog dlg = builder.create();
                    dlg.show();

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
