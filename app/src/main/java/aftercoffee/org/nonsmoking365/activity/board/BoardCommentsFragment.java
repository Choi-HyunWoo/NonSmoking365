package aftercoffee.org.nonsmoking365.activity.board;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import aftercoffee.org.nonsmoking365.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BoardCommentsFragment extends Fragment {


    public BoardCommentsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_board_comments, container, false);
    }


}
