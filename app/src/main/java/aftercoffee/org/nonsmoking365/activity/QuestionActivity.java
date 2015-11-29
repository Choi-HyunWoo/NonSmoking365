package aftercoffee.org.nonsmoking365.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import aftercoffee.org.nonsmoking365.manager.NetworkManager;
import aftercoffee.org.nonsmoking365.R;
import aftercoffee.org.nonsmoking365.manager.UserManager;

public class QuestionActivity extends AppCompatActivity {

    boolean isLogined;
    String userEmail;
    String imageFilePath;

    EditText titleView;
    EditText contentView;

    String title;
    String content;

    // RESULT CODE
    public static final int RESULT_LOAD_IMAGE = 1;

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

        isLogined = UserManager.getInstance().getLoginState();
        if (!isLogined) {
            Toast.makeText(QuestionActivity.this, "문의하기는 회원만 가능합니다.", Toast.LENGTH_SHORT).show();
            finish();
        }

        // 제목 변경을 원하면 (제목이 클릭되면) 제목 빈칸으로
        titleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleView.setText("");
            }
        });

        // 이미지 업로드
        ImageView imageAddBtn = (ImageView)findViewById(R.id.image_add);
        imageAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, RESULT_LOAD_IMAGE);
            }
        });

        // 완료 버튼
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(QuestionActivity.this);
                    builder.setMessage("문의 글을 등록하시겠습니까?");
                    builder.setTitle("문의하기");
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            NetworkManager.getInstance().postQuestionData(QuestionActivity.this, title, content, imageFilePath, new NetworkManager.OnResultListener<String>() {
                                @Override
                                public void onSuccess(String result) {
                                    Toast.makeText(QuestionActivity.this, "문의 글이 등록되었습니다.", Toast.LENGTH_SHORT).show();
                                    finish();
                                }

                                @Override
                                public void onFail(int code) {
                                    Log.d("QuestionActivity ", "network error/" + code);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            imageFilePath = cursor.getString(columnIndex);
            cursor.close();
            ImageView imageView = (ImageView) findViewById(R.id.image_add);
            imageView.setImageBitmap(BitmapFactory.decodeFile(imageFilePath));
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
