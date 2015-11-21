package aftercoffee.org.nonsmoking365.tt.activity.login;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import aftercoffee.org.nonsmoking365.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("로그인");
        actionBar.setElevation(0);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container, new LoginFragment()).commit();
        }
    }

    public void pushSignUpFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new SignupFragment()).addToBackStack(null).commit();
    }

    public void popSignUpFragment() {
        getSupportFragmentManager().popBackStack();
    }

}
