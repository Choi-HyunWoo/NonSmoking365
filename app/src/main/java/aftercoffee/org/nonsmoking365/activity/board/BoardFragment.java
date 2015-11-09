package aftercoffee.org.nonsmoking365.activity.board;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import aftercoffee.org.nonsmoking365.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BoardFragment extends Fragment {

    ListView listView;
    BoardItemAdapter mAdapter;

    public BoardFragment() {
        // Required empty public constructor
        this.setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_board, container, false);

        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle("금연 정보");

        listView = (ListView) view.findViewById(R.id.list_board);
        mAdapter = new BoardItemAdapter();
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((BoardActivity)getActivity()).pushBoardContentsFragment();
            }
        });

        // multiple item list
        mAdapter.add(R.drawable.sample, "흡연의 위험성 글 1번", "warning contents test11111111111111111111111111111111111111111111111111111111111111111");
        mAdapter.add(R.drawable.sample, "금연팁 글 2번", "warning contents test22222222222222222222222222222222222222222222222222222222222222222222222222");
        mAdapter.add(R.drawable.sample, "광고 글 3번", "warning contents test33333333333333333333333333333333333333333333333333333333333333333333333");


        return view;
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
