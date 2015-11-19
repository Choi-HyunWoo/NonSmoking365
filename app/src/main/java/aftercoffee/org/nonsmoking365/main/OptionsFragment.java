package aftercoffee.org.nonsmoking365.main;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import aftercoffee.org.nonsmoking365.Manager.NetworkManager;
import aftercoffee.org.nonsmoking365.Manager.PropertyManager;
import aftercoffee.org.nonsmoking365.Manager.UserManager;
import aftercoffee.org.nonsmoking365.R;
import aftercoffee.org.nonsmoking365.AlarmActivity;
import aftercoffee.org.nonsmoking365.BasisInfoActivity;
import aftercoffee.org.nonsmoking365.login.LoginActivity;
import aftercoffee.org.nonsmoking365.Notice.NoticeActivity;
import aftercoffee.org.nonsmoking365.Question.QuestionActivity;
import aftercoffee.org.nonsmoking365.VersionInfoActivity;
import aftercoffee.org.nonsmoking365.WithdrawActivity;


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
    Button loginBtn, questionBtn, withdrawBtn;
    LinearLayout loginBtnForm, questionBtnForm, withdrawBtnForm, emptyBottomForm;

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
                .displayer(new SimpleBitmapDisplayer())         // RoundedBitmapDisplayer()로
                .build();


        userProfileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 프로필 사진 변경

            }
        });
        userNicknameView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 닉네임 변경
            }
        });

        // Etc option buttons setting
        loginBtn = (Button)v.findViewById(R.id.btn_login);
        loginBtn.setOnClickListener(this);
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
            // ImageLoader.getInstance().displayImage( /* 서버에서 받아온 imageurl */, userProfileImageView, options);
            userNicknameView.setText(UserManager.getInstance().getUserNickname());
            loginBtn.setText("로그아웃");
            questionBtnForm.setVisibility(View.VISIBLE);
            withdrawBtnForm.setVisibility(View.VISIBLE);
            emptyBottomForm.setVisibility(View.GONE);
        }
        else {
            // 로그아웃 상태
            ImageLoader.getInstance().displayImage("drawable://"+R.drawable.icon_profile_default, userProfileImageView, options);
            userNicknameView.setText("로그인 해주세요");
            loginBtn.setText("로그인");
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
                            Toast.makeText(getActivity(), "로그아웃 되었습니다." + result, Toast.LENGTH_SHORT).show();
                            PropertyManager.getInstance().setAutoLogin(false);      // 자동 로그인 끄기
                            UserManager.getInstance().setLoginState(false);         // 로그인 상태 false
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
            case R.id.btn_basisInfo:
                Intent intent = new Intent(getActivity(), BasisInfoActivity.class);
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
