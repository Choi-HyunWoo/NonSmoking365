package aftercoffee.org.nonsmoking365.Activity.community;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import aftercoffee.org.nonsmoking365.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CommunityContentsFragment extends Fragment {


    public CommunityContentsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_community_contents, container, false);
    }


}
