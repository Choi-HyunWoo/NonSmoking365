package aftercoffee.org.nonsmoking365.activity.login;


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
import android.widget.TextView;
import android.widget.Toast;

import aftercoffee.org.nonsmoking365.data.Login;
import aftercoffee.org.nonsmoking365.manager.NetworkManager;
import aftercoffee.org.nonsmoking365.manager.PropertyManager;
import aftercoffee.org.nonsmoking365.manager.UserManager;
import aftercoffee.org.nonsmoking365.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    Button btn;
    EditText emailView, passwordView;
    CheckBox autoLoginCheckView;
    TextView findView;

    String email;
    String password;

    public LoginFragment() {
        // Required empty public constructor
        this.setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle("로그인");

        findView = (TextView)v.findViewById(R.id.text_find);
        emailView = (EditText)v.findViewById(R.id.edit_email);
        passwordView = (EditText)v.findViewById(R.id.edit_password);
        autoLoginCheckView = (CheckBox)v.findViewById(R.id.checkBox_autologin);

        emailView.setText(UserManager.getInstance().getUserEmail());

        // Login Button clicked
        btn = (Button)v.findViewById(R.id.btn_login);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = emailView.getText().toString();
                password = passwordView.getText().toString();
                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(getActivity(), "아이디와 비밀번호를 입력하세요", Toast.LENGTH_SHORT).show();
                } else {
                    NetworkManager.getInstance().login(getContext(), email, password, new NetworkManager.OnResultListener<Login>() {
                        @Override
                        public void onSuccess(Login result) {
                            if (result.status.equals("ok")) {
                                if (autoLoginCheckView.isChecked()) {
                                    PropertyManager.getInstance().setAutoLogin(true);
                                    PropertyManager.getInstance().setAutoLoginId(email);
                                    PropertyManager.getInstance().setAutoLoginPassword(password);
                                }
                                UserManager.getInstance().setLoginState(true);
                                UserManager.getInstance().setUser_id(result.user._id);
                                if (result.user.image_ids.size() != 0) {
                                    if (result.user.image_ids.get(0).equals("")) {
                                        UserManager.getInstance().setUserProfileImageURL("drawable://"+R.drawable.icon_profile_default);
                                    } else {
                                        UserManager.getInstance().setUserProfileImageURL(result.user.image_ids.get(0).uri);
                                    }
                                }
                                UserManager.getInstance().setUserEmail(email);
                                UserManager.getInstance().setUserPassword(password);
                                UserManager.getInstance().setUserNickname(result.user.nick);
                                Toast.makeText(getActivity(), "로그인 성공!", Toast.LENGTH_SHORT).show();
                                getActivity().finish();
                            } else {
                                Toast.makeText(getActivity(), "로그인 실패, 아이디와 비밀번호를 확인하세요", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFail(int code) {

                        }
                    });
                }
            }
        });


        // Facebook login clicked
        btn = (Button)v.findViewById(R.id.btn_facebook);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "구현중", Toast.LENGTH_SHORT).show();
            }
        });


        // Sign up clicked
        btn = (Button)v.findViewById(R.id.btn_signUp);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((LoginActivity)getActivity()).pushSignUpFragment();
            }
        });
        return v;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                getActivity().finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
