package aftercoffee.org.nonsmoking365.login;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import aftercoffee.org.nonsmoking365.Manager.NetworkManager;
import aftercoffee.org.nonsmoking365.Manager.PropertyManager;
import aftercoffee.org.nonsmoking365.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    Button btn;
    EditText emailView, passwordView;
    CheckBox autoLoginCheckView;
    TextView findView;


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
