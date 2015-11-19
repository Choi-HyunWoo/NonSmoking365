package aftercoffee.org.nonsmoking365.board;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import aftercoffee.org.nonsmoking365.Manager.NetworkManager;
import aftercoffee.org.nonsmoking365.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BoardFragment extends Fragment {

    ListView listView;
    PullToRefreshListView refreshView;
    BoardItemAdapter mAdapter;
    boolean isUpdate = false;
    String selectedDocID;

    public BoardFragment() {
        // Required empty public constructor
        this.setHasOptionsMenu(true);
    }

    public static final int BOARD_PAGE_DISPLAY = 5;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_board, container, false);
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle("금연 정보");

        /**
         * PullToRefreshView
         */
        refreshView = (PullToRefreshListView)view.findViewById(R.id.list_board);
        refreshView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                getBoard();
            }
        });

        // Scroll이 마지막 위치일때 호출
        refreshView.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                if (!isUpdate) {
                    int startIndex = mAdapter.getStartIndex();
                    int startPage = startIndex / BOARD_PAGE_DISPLAY;
                    if (startIndex != -1) {
                        isUpdate = true;
                        NetworkManager.getInstance().getBoardData(getContext(), BOARD_PAGE_DISPLAY, startPage, new NetworkManager.OnResultListener<Board>() {
                            @Override
                            public void onSuccess(Board result) {
                                if (result != null) {
                                    for (Docs d : result.docsList) {
                                        if (d.category.equals("warning")) {
                                            BoardWarningItem b = new BoardWarningItem();
                                            b._id = d._id;
                                            b.title = d.title;
                                            b.contents = d.content;
                                            b.titleImg = R.drawable.sample;
                                            mAdapter.add(b);
                                        } else if (d.category.equals("tip")) {
                                            BoardTipsItem b = new BoardTipsItem();
                                            b._id = d._id;
                                            b.title = d.title;
                                            b.contents = d.content;
                                            b.titleImg = R.drawable.sample;
                                            mAdapter.add(b);
                                        } else {
                                            BoardAdItem b = new BoardAdItem();
                                            b._id = d._id;
                                            b.adImg = R.drawable.sample;
                                            mAdapter.add(b);
                                        }
                                    }
                                }
                                isUpdate = false;
                            }

                            @Override
                            public void onFail(int code) {
                                Log.d("BoardFragment Last ", "network error/" + code);
                                isUpdate = false;
                            }
                        });
                    }
                }
            }
        });

        listView = refreshView.getRefreshableView();
        mAdapter = new BoardItemAdapter();
        listView.setAdapter(mAdapter);
        getBoard();     // 최초 가져오기

        // 게시글 목록에서 선택 시, 해당 게시글로 이동
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedDocID = mAdapter.getDocID(position-1);
                ((BoardActivity)getActivity()).pushBoardContentsFragment(selectedDocID);
            }
        });

        return view;
    }

    private void getBoard() {
        NetworkManager.getInstance().getBoardData(getContext(), BOARD_PAGE_DISPLAY, 0, new NetworkManager.OnResultListener<Board>() {
            @Override
            public void onSuccess(Board result) {
                mAdapter.setTotalCount(result.count);
                mAdapter.clear();
                if (result != null) {
                    for (Docs d : result.docsList) {
                        if (d.category.equals("warning")) {
                            BoardWarningItem b = new BoardWarningItem();
                            b._id = d._id;
                            b.title = d.title;
                            b.contents = d.content;
                            b.titleImg = R.drawable.sample;
                            mAdapter.add(b);
                        } else if (d.category.equals("tip")) {
                            BoardTipsItem b = new BoardTipsItem();
                            b._id = d._id;
                            b.title = d.title;
                            b.contents = d.content;
                            b.titleImg = R.drawable.sample;
                            mAdapter.add(b);
                        } else {
                            BoardAdItem b = new BoardAdItem();
                            b._id = d._id;
                            b.adImg = R.drawable.sample;
                            mAdapter.add(b);
                        }
                    }
                }
                refreshView.onRefreshComplete();            // Refresh 완료

            }

            @Override
            public void onFail(int code) {
                Log.d("BoardFragment getboard ", "network error/" + code);
            }
        });
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
