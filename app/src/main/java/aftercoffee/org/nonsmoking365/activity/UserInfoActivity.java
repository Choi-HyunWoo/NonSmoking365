package aftercoffee.org.nonsmoking365.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;

import aftercoffee.org.nonsmoking365.R;
import aftercoffee.org.nonsmoking365.manager.NetworkManager;
import aftercoffee.org.nonsmoking365.manager.UserManager;
import aftercoffee.org.nonsmoking365.utilities.Utilities;

public class UserInfoActivity extends AppCompatActivity {

    boolean isLogined;
    String user_id;
    String userNickname;
    String userProfileImageURL;
    String userGrade;

    DisplayImageOptions options;
    EditText userNicknameView;
    ImageView userProfileImageView;
    TextView userGradeView;
    Button finishBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setElevation(0);
        actionBar.setTitle("프로필 관리");
        isLogined = UserManager.getInstance().getLoginState();
        if (!isLogined) {
            Toast.makeText(UserInfoActivity.this, "로그인 해주세요", Toast.LENGTH_SHORT).show();
            finish();
        }
        user_id = UserManager.getInstance().getUser_id();
        userNickname = UserManager.getInstance().getUserNickname();
        userProfileImageURL = UserManager.getInstance().getUserProfileImageURL();
        userGrade = UserManager.getInstance().getUserGradeToString();

        // View initialize
        userProfileImageView = (ImageView)findViewById(R.id.image_userProfileImage);
        userNicknameView = (EditText)findViewById(R.id.edit_userNickname);
        userGradeView = (TextView)findViewById(R.id.text_userGrade);
        finishBtn = (Button)findViewById(R.id.btn_finish);

        // setting
        userNicknameView.setText(userNickname);
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.icon_image_add)
                .showImageForEmptyUri(R.drawable.icon_cigarette)
                .showImageOnFail(R.drawable.icon_cigarette)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .considerExifParams(true)
                .displayer(new BitmapDisplayer() {
                    @Override
                    public void display(Bitmap bitmap, ImageAware imageAware, LoadedFrom loadedFrom) {
                        Bitmap centerCroppedBitmap = Utilities.getCenterCroppedBitmap(bitmap);

                        RoundedBitmapDrawable circledDrawable = RoundedBitmapDrawableFactory.create(getResources(), centerCroppedBitmap);
                        circledDrawable.setCircular(true);
                        circledDrawable.setAntiAlias(true);
                        imageAware.setImageDrawable(circledDrawable);
                    }
                })
                .build();
        ImageLoader.getInstance().displayImage(userProfileImageURL, userProfileImageView, options);
        userGradeView.setText(userGrade);



        userProfileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                // startActivityForResult(Intent.createChooser(intent, "Select Picture"), requestCode);
            }
        });


        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String changedNickName = userNicknameView.getText().toString();
                if (TextUtils.isEmpty(changedNickName)) {
                    Toast.makeText(UserInfoActivity.this, "닉네임을 설정해 주세요", Toast.LENGTH_SHORT).show();
                } else {
                    // NetworkManager.getInstance().postUserInfo(UserInfoActivity.this,
                    // result OK면
                    finish();
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
