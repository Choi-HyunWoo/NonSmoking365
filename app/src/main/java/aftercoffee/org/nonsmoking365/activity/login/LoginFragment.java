package aftercoffee.org.nonsmoking365.activity.login;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import aftercoffee.org.nonsmoking365.NetworkManager;
import aftercoffee.org.nonsmoking365.PropertyManager;
import aftercoffee.org.nonsmoking365.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    Button btn;
    EditText emailView, passwordView;
    CheckBox autoLoginCheckView;
    TextView findView, signupView;


    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        findView = (TextView)v.findViewById(R.id.text_find);
        signupView = (TextView)v.findViewById(R.id.text_signUp);

        emailView = (EditText)v.findViewById(R.id.edit_email);
        passwordView = (EditText)v.findViewById(R.id.edit_password);

        autoLoginCheckView = (CheckBox)v.findViewById(R.id.checkBox_autologin);

        // Login Button clicked
        btn = (Button)v.findViewById(R.id.btn_login);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String id = emailView.getText().toString();
                final String password = passwordView.getText().toString();

                /** id, password 검사할 것 **/

                NetworkManager.getInstance().login(id, password, new NetworkManager.OnResultListener<String>() {
                    @Override
                    public void onSuccess(String result) {
                        if (result.equals("ok")) {
                            // 자동 로그인이 설정되있다면 SP에 저장
                            if (autoLoginCheckView.isChecked()) {
                                PropertyManager.getInstance().setId(id);
                                PropertyManager.getInstance().setPassword(password);
                            }
                            getActivity().finish();
                        } else {
                            // ...
                            Toast.makeText(getActivity(), "로그인 실패!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFail(int code) {
                        // ...
                        Toast.makeText(getActivity(), "CONNECTION FAIL", Toast.LENGTH_SHORT).show();
                    }
                });
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
        signupView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((LoginActivity)getActivity()).pushSingUpFragment();
            }
        });
        return v;
    }


}
