package aftercoffee.org.nonsmoking365.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import aftercoffee.org.nonsmoking365.R;
import aftercoffee.org.nonsmoking365.activity.BoardActivity;
import aftercoffee.org.nonsmoking365.activity.CentersActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class StateFragment extends Fragment {


    public StateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_state, container, false);
        // Inflate the layout for this fragment

        Button btn = (Button)view.findViewById(R.id.btn_warning);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BoardActivity.class);
                intent.putExtra(BoardActivity.PARAM_BOARD_TYPE, BoardActivity.TYPE_WARNING);
                startActivity(intent);
            }
        });

        btn = (Button)view.findViewById(R.id.btn_tips);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BoardActivity.class);
                intent.putExtra(BoardActivity.PARAM_BOARD_TYPE, BoardActivity.TYPE_TIPS);
                startActivity(intent);
            }
        });

        btn = (Button)view.findViewById(R.id.btn_centers);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CentersActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }


}
