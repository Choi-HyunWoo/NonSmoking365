package aftercoffee.org.nonsmoking365.board;


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
import android.widget.Toast;

import aftercoffee.org.nonsmoking365.Manager.NetworkManager;
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

        // Client item test
        /*
        // multiple item list
        BoardWarningItem warning = new BoardWarningItem();
        warning.title="흡연의 위험성 글입니다.";
        warning.contents="흡연은 위헙합니다. 금연하세요";
        warning.titleImg=R.drawable.sample;

        BoardTipsItem tips = new BoardTipsItem();
        tips.title="이것은 금연 팁 글입니다.";
        tips.contents="금연이 힘드실땐 운동하세요";
        tips.titleImg=R.drawable.sample;

        BoardAdItem ad = new BoardAdItem();
        ad.adImg=R.drawable.sample;
        */

        /*
        mAdapter.add(warning);
        mAdapter.add(tips);
        mAdapter.add(warning);
        //mAdapter.add(ad);
        mAdapter.add(tips);
        //mAdapter.add(ad);
        mAdapter.add(warning);
        mAdapter.add(tips);
        */

        // Network add BoardItem Test
        NetworkManager.getInstance().getBoardData(getContext(), new NetworkManager.OnResultListener<Board>() {
            @Override
            public void onSuccess(Board result) {
                for (Docs d : result.docsList) {
                    if (d.category.equals("warning")) {
                        BoardWarningItem b = new BoardWarningItem();
                        b.title = d.title;
                        b.contents = d.content;
                        b.titleImg = R.drawable.sample;
                        mAdapter.add(b);
                    } else if (d.category.equals("tip")) {
                        BoardTipsItem b = new BoardTipsItem();
                        b.title = d.title;
                        b.contents = d.content;
                        b.titleImg = R.drawable.sample;
                        mAdapter.add(b);
                    } else {
                        BoardAdItem b = new BoardAdItem();
                        b.adImg = R.drawable.sample;
                        mAdapter.add(b);
                    }
                }
            }

            @Override
            public void onFail(int code) {
                Toast.makeText(getContext(), "Network connect failed", Toast.LENGTH_SHORT).show();
            }
        });

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
