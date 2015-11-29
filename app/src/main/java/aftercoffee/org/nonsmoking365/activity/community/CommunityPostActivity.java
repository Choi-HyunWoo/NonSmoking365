package aftercoffee.org.nonsmoking365.activity.community;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import aftercoffee.org.nonsmoking365.R;
import aftercoffee.org.nonsmoking365.data.CommunityDocs;
import aftercoffee.org.nonsmoking365.manager.NetworkManager;

public class CommunityPostActivity extends AppCompatActivity {

    EditText titleView;
    EditText contentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_post);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("글 작성");
        setSupportActionBar(toolbar);

        titleView = (EditText)findViewById(R.id.edit_title);
        contentView = (EditText)findViewById(R.id.edit_content);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String title = titleView.getText().toString();
                final String content = contentView.getText().toString();
                if (TextUtils.isEmpty(title)) {
                    Toast.makeText(CommunityPostActivity.this, "제목을 입력해 주세요.", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(content)) {
                    Toast.makeText(CommunityPostActivity.this, "내용을 입력해 주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CommunityPostActivity.this);
                    builder.setIcon(R.drawable.icon_logo_black);
                    builder.setTitle("글 작성");
                    builder.setMessage("글을 등록하시겠습니까?");
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            NetworkManager.getInstance().postCommunityContent(CommunityPostActivity.this, title, content, new NetworkManager.OnResultListener<CommunityDocs>() {
                                @Override
                                public void onSuccess(CommunityDocs result) {
                                    Toast.makeText(CommunityPostActivity.this, "글이 등록되었습니다.", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                                @Override
                                public void onFail(int code) {
                                    Log.d("Network ERROR", "communitypost" + code);
                                }
                            });
                        }
                    });
                    builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
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
            case android.R.id.home :
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
