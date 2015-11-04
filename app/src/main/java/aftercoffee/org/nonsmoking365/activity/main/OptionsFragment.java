package aftercoffee.org.nonsmoking365.activity.main;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import aftercoffee.org.nonsmoking365.R;
import aftercoffee.org.nonsmoking365.activity.AlarmActivity;
import aftercoffee.org.nonsmoking365.activity.BasisInfoActivity;
import aftercoffee.org.nonsmoking365.activity.login.LoginActivity;
import aftercoffee.org.nonsmoking365.activity.NoticeActivity;
import aftercoffee.org.nonsmoking365.activity.QuestionActivity;
import aftercoffee.org.nonsmoking365.activity.VersionInfoActivity;
import aftercoffee.org.nonsmoking365.activity.WithdrawActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class OptionsFragment extends Fragment implements View.OnClickListener {


    public OptionsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_options, container, false);

        Button btn;
        btn = (Button)v.findViewById(R.id.btn_login);
        btn.setOnClickListener(this);

        btn = (Button)v.findViewById(R.id.btn_basisInfo);
        btn.setOnClickListener(this);

        btn = (Button)v.findViewById(R.id.btn_alarm);
        btn.setOnClickListener(this);

        btn = (Button)v.findViewById(R.id.btn_notice);
        btn.setOnClickListener(this);

        btn = (Button)v.findViewById(R.id.btn_question);
        btn.setOnClickListener(this);

        btn = (Button)v.findViewById(R.id.btn_withdraw);
        btn.setOnClickListener(this);

        btn = (Button)v.findViewById(R.id.btn_versionInfo);
        btn.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                startActivity(new Intent(getActivity(), LoginActivity.class));
                break;
            case R.id.btn_basisInfo:
                startActivity(new Intent(getActivity(), BasisInfoActivity.class));
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
