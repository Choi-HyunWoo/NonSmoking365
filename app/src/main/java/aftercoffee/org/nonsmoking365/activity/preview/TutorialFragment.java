package aftercoffee.org.nonsmoking365.activity.preview;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import aftercoffee.org.nonsmoking365.R;
import aftercoffee.org.nonsmoking365.activity.BasisInfoActivity;
import aftercoffee.org.nonsmoking365.manager.PropertyManager;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TutorialFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TutorialFragment extends Fragment {

    private static final String ARG_POSITION = "position";
    private int position;

    public TutorialFragment() {
        // Required empty public constructor
    }

    public static TutorialFragment newInstance(int position) {
        TutorialFragment fragment = new TutorialFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            position = getArguments().getInt(ARG_POSITION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view;
        switch (position) {
            case 0 :
                view = inflater.inflate(R.layout.view_tutorial_01, container, false);
                break;
            case 1 :
                view = inflater.inflate(R.layout.view_tutorial_02, container, false);
                break;
            case 2 :
                view = inflater.inflate(R.layout.view_tutorial_03, container, false);
                Button startBtn = (Button) view.findViewById(R.id.btn_start);
                startBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PropertyManager.getInstance().setPreviewCheck(true);
                        Intent intent = new Intent(getActivity(), BasisInfoActivity.class);
                        intent.putExtra(BasisInfoActivity.START_MODE, BasisInfoActivity.MODE_INIT);
                        startActivity(intent);
                        getActivity().finish();
                    }
                });
                break;
            default:
                view = inflater.inflate(R.layout.view_tutorial_01, container, false);
                break;
        }
        return view;
    }

}
