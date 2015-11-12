package aftercoffee.org.nonsmoking365.login;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import aftercoffee.org.nonsmoking365.AccessTermsActivity;
import aftercoffee.org.nonsmoking365.Manager.NetworkManager;
import aftercoffee.org.nonsmoking365.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignupFragment extends Fragment {

    public static final int REQUEST_ACCESS_TERMS = 0;

    EditText nicknameView, emailView, passwordView, passwordCheckView;
    CheckBox accessTermsCheckBox;
    Button btn;

    String nickname;
    String email;
    String password;
    String passwordCheck;

    public SignupFragment() {
        // Required empty public constructor
        this.setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_signup, container, false);
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle("회원가입");
        nicknameView = (EditText)v.findViewById(R.id.edit_nicknameView);
        emailView = (EditText)v.findViewById(R.id.edit_emailView);
        passwordView = (EditText)v.findViewById(R.id.edit_passwordView);
        passwordCheckView = (EditText)v.findViewById(R.id.edit_passwordCheckView);


        // 이용약관 확인
        accessTermsCheckBox = (CheckBox) v.findViewById(R.id.checkBox_accesstermsCheck);
        accessTermsCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accessTermsCheckBox.setChecked(false);
                Intent intent = new Intent(getActivity(), AccessTermsActivity.class);
                intent.putExtra("s", "s");
                startActivityForResult(intent, REQUEST_ACCESS_TERMS);
            }
        });

        // 회원가입 완료 버튼
        btn = (Button)v.findViewById(R.id.btn_finish);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nickname = nicknameView.getText().toString();
                email = emailView.getText().toString();
                password = passwordView.getText().toString();
                passwordCheck = passwordCheckView.getText().toString();

                if (TextUtils.isEmpty(nickname)) {
                    Toast.makeText(getActivity(), "닉네임을 입력해 주세요", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getActivity(), "E-mail을 입력해 주세요", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getActivity(), "비밀번호를 입력해 주세요", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(passwordCheck)) {
                    Toast.makeText(getActivity(), "비밀번호 확인에 입력된 비밀번호가 다릅니다.", Toast.LENGTH_SHORT).show();
                } else if (!checkEmail(email)) {
                    Toast.makeText(getActivity(), "E-mail 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show();
                } else if (!accessTermsCheckBox.isChecked()) {
                    Toast.makeText(getActivity(), "약관에 동의해 주세요", Toast.LENGTH_SHORT).show();
                } else {
                    NetworkManager.getInstance().signUp(getContext(), email, password, nickname, new NetworkManager.OnResultListener<String>() {
                        @Override
                        public void onSuccess(String result) {
                            Toast.makeText(getActivity(), "회원 가입을 환영합니다", Toast.LENGTH_SHORT).show();
                            getActivity().finish();
                        }

                        @Override
                        public void onFail(int code) {
                            Toast.makeText(getActivity(), "이미 존재하는 E-mail 입니다.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        return v;
    }

    private boolean checkEmail(String email){
        String regex = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);
        boolean isNormal = m.matches();
        return isNormal;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(REQUEST_ACCESS_TERMS, resultCode, data);
        if (requestCode == REQUEST_ACCESS_TERMS && resultCode == Activity.RESULT_OK) {
            String result = data.getStringExtra(AccessTermsActivity.RESULT_MESSAGE);
            if (result.equals(AccessTermsActivity.MESSAGE_AGREE)) {
                accessTermsCheckBox.setChecked(true);
            } else {
                accessTermsCheckBox.setChecked(false);
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                ((LoginActivity)getActivity()).popSignUpFragment();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
