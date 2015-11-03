package aftercoffee.org.nonsmoking365.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import aftercoffee.org.nonsmoking365.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class OptionsFragment extends Fragment {


    public OptionsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_options, container, false);


        return v;
    }

    public void onClick (View v) {
        switch (v.getId()) {
            case R.id.btn_login : {

            }
            case R.id.btn_basisInfo : {

            }
            case R.id.btn_alarm : {

            }
            case R.id.btn_notice : {

            }
            case R.id.btn_question : {

            }
            case R.id.btn_withdraw : {

            }
            case R.id.btn_versionInfo : {

            }
            default: {

            }
        }
    }


}
