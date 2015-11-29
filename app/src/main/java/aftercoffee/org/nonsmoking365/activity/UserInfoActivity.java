package aftercoffee.org.nonsmoking365.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import aftercoffee.org.nonsmoking365.R;
import aftercoffee.org.nonsmoking365.data.User;
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

    public static final int RESULT_LOAD_IMAGE = 1;
    String imageFilePath;
    boolean isDefaultImage = false;

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
        userGrade = UserManager.getInstance().getUserGrade();

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
        if (TextUtils.isEmpty(userProfileImageURL)) {
            // 프로필 이미지가 없는 경우
            ImageLoader.getInstance().displayImage("drawable://"+R.drawable.icon_profile_default, userProfileImageView, options);
        } else {
            // 프로필 이미지가 있는 경우
            ImageLoader.getInstance().displayImage(userProfileImageURL, userProfileImageView, options);
        }
        userGradeView.setText(userGrade);

        final String[] items = new String[] {"기본 이미지로 선택", "갤러리에서 선택"};
        // 변경할 프로필 사진 선택
        userProfileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UserInfoActivity.this);
                builder.setTitle("프로필 사진 변경");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                // 기본 이미지로 설정
                                Bitmap bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.icon_profile_default);
                                File file = new File(getExternalCacheDir(), FILE_NAME);
                                try {
                                    FileOutputStream fos = new FileOutputStream(file);
                                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                                    fos.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                imageFilePath = getExternalCacheDir()+FILE_NAME;
                                String decodedUri = Uri.fromFile(new File(imageFilePath)).toString();
                                ImageLoader.getInstance().displayImage(decodedUri, userProfileImageView, options);
                                break;
                            case 1:
                                // 이미지 선택]
                                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(intent, RESULT_LOAD_IMAGE);
                                break;
                        }
                    }
                });
                AlertDialog dlg = builder.create();
                dlg.show();
            }
        });


        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String changedNickName = userNicknameView.getText().toString();
                if (TextUtils.isEmpty(changedNickName)) {
                    Toast.makeText(UserInfoActivity.this, "닉네임을 설정해 주세요", Toast.LENGTH_SHORT).show();
                } else {
                    NetworkManager.getInstance().modifyUser(UserInfoActivity.this, user_id, changedNickName, imageFilePath, new NetworkManager.OnResultResponseListener<User>() {
                        @Override
                        public void onSuccess(User result) {
                            UserManager.getInstance().setUserNickname(result.nick);
                            if (result.image_ids.size() != 0) {
                                UserManager.getInstance().setUserProfileImageURL(result.image_ids.get(0).uri);
                            }
                            Toast.makeText(UserInfoActivity.this, "회원정보가 수정되었습니다.", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        @Override
                        public void onFail(int code, String reponseString) {
                            if (reponseString.equals("the nick already exists")) {
                                Toast.makeText(UserInfoActivity.this, "이미 존재하는 닉네임입니다.", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.d("Network ERROR : ", "profile modify/"+code);
                            }
                        }
                    });
                }
            }
        });

    }

    // Bitmap to File
    public static String FILE_PATH = "/storage/emulated/0/NonSmoking365/profileImage/";
    public static String FILE_NAME = "/myprofile.jpg";
    public  void SaveBitmapToFileCache(Bitmap bitmap, String strFilePath, String filename) {
        File file = new File(this.getExternalFilesDir(Environment.DIRECTORY_PICTURES), filename);
        // If no folders
        if (!file.exists()) {
            file.mkdirs();
            // Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
        }
        File fileCacheItem = new File(strFilePath + filename);
        OutputStream out = null;
        try {
            fileCacheItem.createNewFile();
            out = new FileOutputStream(fileCacheItem);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
            String decodedUri = Uri.fromFile(new File(imageFilePath)).toString();
            ImageLoader.getInstance().displayImage(decodedUri, userProfileImageView, options);
        }
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
