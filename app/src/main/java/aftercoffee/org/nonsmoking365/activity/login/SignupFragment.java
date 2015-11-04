package aftercoffee.org.nonsmoking365.activity.login;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import aftercoffee.org.nonsmoking365.activity.AccessTermsActivity;
import aftercoffee.org.nonsmoking365.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignupFragment extends Fragment {

    public static final int REQUEST_ACCESS_TERMS = 0;

    EditText nicknameView, emailView, passwordView, passwordCheckView;
    CheckBox accessTermsCheckBox;
    Button btn;

    public SignupFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_signup, container, false);

        accessTermsCheckBox = (CheckBox) v.findViewById(R.id.checkBox_accesstermsCheck);
        accessTermsCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accessTermsCheckBox.setChecked(false);
                Intent intent = new Intent(getActivity(), AccessTermsActivity.class);
                startActivityForResult(intent, REQUEST_ACCESS_TERMS);
            }
        });

        btn = (Button)v.findViewById(R.id.btn_finish);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "회원 가입을 환영합니다", Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
        });

        return v;
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



}
