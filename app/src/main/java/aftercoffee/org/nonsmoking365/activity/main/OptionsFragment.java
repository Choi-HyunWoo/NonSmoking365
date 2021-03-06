package aftercoffee.org.nonsmoking365.activity.main;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;

import aftercoffee.org.nonsmoking365.activity.userinfo.UserInfoActivity;
import aftercoffee.org.nonsmoking365.manager.NetworkManager;
import aftercoffee.org.nonsmoking365.manager.UserManager;
import aftercoffee.org.nonsmoking365.R;
import aftercoffee.org.nonsmoking365.activity.AlarmActivity;
import aftercoffee.org.nonsmoking365.activity.BasisInfoActivity;
import aftercoffee.org.nonsmoking365.activity.login.LoginActivity;
import aftercoffee.org.nonsmoking365.activity.notice.NoticeActivity;
import aftercoffee.org.nonsmoking365.activity.QuestionActivity;
import aftercoffee.org.nonsmoking365.activity.VersionInfoActivity;
import aftercoffee.org.nonsmoking365.activity.WithdrawActivity;
import aftercoffee.org.nonsmoking365.utilities.Utilities;


/**
 * A simple {@link Fragment} subclass.
 */
public class OptionsFragment extends Fragment implements View.OnClickListener {

    boolean isLogined;

    public OptionsFragment() {
        // Required empty public constructor
    }

    ImageView userProfileImageView;
    TextView userNicknameView;
    Button loginBtn, userInfoBtn, questionBtn, withdrawBtn;
    LinearLayout loginBtnForm, userInfoBtnForm, questionBtnForm, withdrawBtnForm, emptyBottomForm;

    DisplayImageOptions options;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_options, container, false);
        isLogined = UserManager.getInstance().getLoginState();

        // View Initialize
        userProfileImageView = (ImageView)v.findViewById(R.id.image_userProfileImage);
        userNicknameView = (TextView)v.findViewById(R.id.text_userNickname);
        loginBtnForm = (LinearLayout)v.findViewById(R.id.loginBtnForm);
        userInfoBtnForm = (LinearLayout)v.findViewById(R.id.userInfoBtnForm);
        questionBtnForm = (LinearLayout)v.findViewById(R.id.questionBtnForm);
        withdrawBtnForm = (LinearLayout)v.findViewById(R.id.withdrawBtnForm);
        emptyBottomForm = (LinearLayout)v.findViewById(R.id.emptyBottomForm);
        // Imageloader options setting
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

        // 프로필 이미지
        userProfileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isLogined) {
                    // 비로그인 상태일 시, 로그인 화면으로 이동
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    // 로그인 상태일 시, 프로필 이미지 변경
                    Intent intent = new Intent(getActivity(), UserInfoActivity.class);
                    startActivity(intent);
                }

            }
        });
        userNicknameView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isLogined) {
                    // 비로그인 상태일 시, 로그인 화면으로 이동
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    // 로그인 상태일 시, 닉네임 변경
                    Intent intent = new Intent(getActivity(), UserInfoActivity.class);
                    startActivity(intent);
                }
            }
        });

        // Etc option buttons setting
        loginBtn = (Button)v.findViewById(R.id.btn_login);
        loginBtn.setOnClickListener(this);
        userInfoBtn = (Button)v.findViewById(R.id.btn_userInfo);
        userInfoBtn.setOnClickListener(this);
        Button btn;
        btn = (Button)v.findViewById(R.id.btn_basisInfo);
        btn.setOnClickListener(this);
        btn = (Button)v.findViewById(R.id.btn_alarm);
        btn.setOnClickListener(this);
        btn = (Button)v.findViewById(R.id.btn_notice);
        btn.setOnClickListener(this);
        questionBtn = (Button)v.findViewById(R.id.btn_question);
        questionBtn.setOnClickListener(this);
        withdrawBtn = (Button)v.findViewById(R.id.btn_withdraw);
        withdrawBtn.setOnClickListener(this);
        btn = (Button)v.findViewById(R.id.btn_versionInfo);
        btn.setOnClickListener(this);


        setViewLogined();
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        isLogined = UserManager.getInstance().getLoginState();
        setViewLogined();
    }


    // 로그인 시 변화될 부분들
    // create, resume 시에 부르자..
    private void setViewLogined() {
        if (isLogined) {
            // 로그인 상태
            String profileImgUrl = UserManager.getInstance().getUserProfileImageURL();
            if (TextUtils.isEmpty(profileImgUrl)) {
                // 프로필 이미지가 없는 경우
                ImageLoader.getInstance().displayImage("drawable://"+R.drawable.icon_profile_default, userProfileImageView, options);
            } else {
                // 프로필 이미지가 있는 경우
                ImageLoader.getInstance().displayImage(profileImgUrl, userProfileImageView, options);
            }
            userNicknameView.setText(UserManager.getInstance().getUserNickname());
            loginBtn.setText("로그아웃");
            userInfoBtnForm.setVisibility(View.VISIBLE);
            questionBtnForm.setVisibility(View.VISIBLE);
            withdrawBtnForm.setVisibility(View.VISIBLE);
            emptyBottomForm.setVisibility(View.GONE);
        }
        else {
            // 로그아웃 상태
            ImageLoader.getInstance().displayImage("drawable://"+R.drawable.icon_profile_default, userProfileImageView, options);
            userNicknameView.setText("로그인 해주세요");
            loginBtn.setText("로그인");
            userInfoBtnForm.setVisibility(View.GONE);
            questionBtnForm.setVisibility(View.GONE);
            withdrawBtnForm.setVisibility(View.GONE);
            emptyBottomForm.setVisibility(View.VISIBLE);
        }
    }

    // Buttons Listener
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                // 로그인 상태일 때 (로그아웃 버튼)
                if (isLogined) {
                    // 로그아웃 처리
                    NetworkManager.getInstance().logout(getContext(), new NetworkManager.OnResultListener<String>() {
                        @Override
                        public void onSuccess(String result) {
                            Toast.makeText(getActivity(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                            UserManager.getInstance().logoutClear();
                            isLogined = false;      // 변수 갱신후
                            setViewLogined();       // 뷰 갱신
                        }
                        @Override
                        public void onFail(int code) {
                            Toast.makeText(getActivity(), "연결 실패"+code, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                // 로그아웃 상태일 때 (로그인 버튼)
                else {
                    // 로그인 화면으로
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;
            case R.id.btn_userInfo:
                Intent intent = new Intent(getActivity(), UserInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_basisInfo:
                intent = new Intent(getActivity(), BasisInfoActivity.class);
                intent.putExtra(BasisInfoActivity.START_MODE, BasisInfoActivity.MODE_MODIFY);
                startActivity(intent);
                break;
            case R.id.btn_alarm:
                startActivity(new Intent(getActivity(), AlarmActivity.class));
                break;
            case R.id.btn_notice:
                startActivity(new Intent(getActivity(), NoticeActivity.class));
                break;
            case R.id.btn_question:
                startActivity(new Intent(getActivity(), QuestionActivity.class));
                break;
            case R.id.btn_withdraw:
                startActivity(new Intent(getActivity(), WithdrawActivity.class));
                break;
            case R.id.btn_versionInfo:
                startActivity(new Intent(getActivity(), VersionInfoActivity.class));
                break;
            default:
                break;
        }
    }
}
