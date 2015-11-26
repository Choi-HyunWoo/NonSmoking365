package aftercoffee.org.nonsmoking365.activity.board.boardlist;


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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import aftercoffee.org.nonsmoking365.activity.board.BoardActivity;
import aftercoffee.org.nonsmoking365.data.Board;
import aftercoffee.org.nonsmoking365.data.BoardDocs;
import aftercoffee.org.nonsmoking365.data.LikesResult;
import aftercoffee.org.nonsmoking365.manager.NetworkManager;
import aftercoffee.org.nonsmoking365.manager.UserManager;
import aftercoffee.org.nonsmoking365.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BoardFragment extends Fragment implements BoardItemAdapter.OnAdapterWarningClickListener, BoardItemAdapter.OnAdapterTipsClickListener {

    public static final int BOARD_PAGE_DISPLAY = 5;

    boolean isLogined;
    String user_id;
    String selectedDocID;

    ListView listView;
    PullToRefreshListView refreshView;
    boolean isUpdate = false;
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
                                    for (BoardDocs d : result.docsList) {
                                        if (d.category.equals("warning")) {
                                            BoardWarningItem b = new BoardWarningItem();
                                            b._id = d._id;
                                            b.title = d.title;
                                            b.contents = d.content;
                                            b.likes = d.like_ids.size();
                                            b.likeOn = false;
                                            for (String id : d.like_ids) {
                                                if (user_id.equals(id))
                                                    b.likeOn = true;        // 좋아요 ON
                                            }
                                            b.titleImg = R.drawable.sample;
                                            mAdapter.add(b);
                                        } else if (d.category.equals("tip")) {
                                            BoardTipsItem b = new BoardTipsItem();
                                            b._id = d._id;
                                            b.title = d.title;
                                            b.contents = d.content;
                                            b.likes = d.like_ids.size();
                                            for (String id : d.like_ids) {
                                                if (user_id.equals(id))
                                                    b.likeOn = true;        // 좋아요 ON
                                            }
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
                                Log.d("NetworkERROR/", "BoardGET_last_refresh"+code);
                                isUpdate = false;
                            }
                        });
                    }
                }
            }
        });

        listView = refreshView.getRefreshableView();
        mAdapter = new BoardItemAdapter();
        mAdapter.setOnAdapterWarningClickListener(this);
        mAdapter.setOnAdapterTipsClickListener(this);
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
                    for (BoardDocs d : result.docsList) {
                        if (d.category.equals("warning")) {
                            BoardWarningItem b = new BoardWarningItem();
                            b._id = d._id;
                            b.title = d.title;
                            b.contents = d.content;
                            b.likes = d.like_ids.size();
                            for (String id : d.like_ids) {
                                if (user_id.equals(id))
                                    b.likeOn = true;        // 좋아요 ON
                            }
                            b.titleImg = R.drawable.sample;
                            mAdapter.add(b);
                        } else if (d.category.equals("tip")) {
                            BoardTipsItem b = new BoardTipsItem();
                            b._id = d._id;
                            b.title = d.title;
                            b.contents = d.content;
                            b.likes = d.like_ids.size();
                            for (String id : d.like_ids) {
                                if (user_id.equals(id))
                                    b.likeOn = true;        // 좋아요 ON
                            }
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
                Log.d("NetworkERROR/", "BoardGET"+code);
            }
        });
    }

    @Override
    public void onAdapterWarningLikeClick(BoardItemAdapter adapter, final BoardWarningItem item, View view) {
        final ImageView likeImage = (ImageView)view.findViewById(R.id.image_like);
        final Button likeBtn = (Button)view.findViewById(R.id.btn_like);
        final int position = mAdapter.items.indexOf(item);
        NetworkManager.getInstance().postBoardLike(getActivity(), selectedDocID, user_id, new NetworkManager.OnResultListener<LikesResult>() {
            @Override
            public void onSuccess(LikesResult result) {
                likeBtn.setText("좋아요 "+result.like_ids.size());
                for (String id : result.like_ids) {
                    if (user_id.equals(id)) {
                        // 좋아요 OFF > ON
                        likeImage.setImageResource(R.drawable.icon_like_active);
                        item.likeOn = true;
                        ((BoardWarningItem)mAdapter.items.get(position)).likeOn = true;
                        ((BoardWarningItem)mAdapter.items.get(position)).likes = result.like_ids.size();
                        break;
                    } else {
                        likeImage.setImageResource(R.drawable.icon_like);
                        item.likeOn = false;
                        ((BoardWarningItem)mAdapter.items.get(position)).likeOn = false;
                        ((BoardWarningItem)mAdapter.items.get(position)).likes = result.like_ids.size();
                    }
                }
            }
            @Override
            public void onFail(int code) {
                Log.d("NetworkERROR/", "BoardLikePOST"+code);
            }
        });
    }
    @Override
    public void onAdapterTipsLikeClick(BoardItemAdapter adapter, final BoardTipsItem item, View view) {
        final ImageView likeImage = (ImageView)view.findViewById(R.id.image_like);
        final Button likeBtn = (Button)view.findViewById(R.id.btn_like);
        final int position = mAdapter.items.indexOf(item);
        NetworkManager.getInstance().postBoardLike(getActivity(), selectedDocID, user_id, new NetworkManager.OnResultListener<LikesResult>() {
            @Override
            public void onSuccess(LikesResult result) {
                likeBtn.setText("좋아요 "+result.like_ids.size());
                for (String id : result.like_ids) {
                    if (user_id.equals(id)) {
                        // 좋아요 OFF > ON
                        likeImage.setImageResource(R.drawable.icon_like_active);
                        item.likeOn = true;
                        ((BoardTipsItem)mAdapter.items.get(position)).likeOn = true;
                        ((BoardTipsItem)mAdapter.items.get(position)).likes = result.like_ids.size();
                        break;
                    } else {
                        // 좋아요 ON > OFF
                        likeImage.setImageResource(R.drawable.icon_like);
                        item.likeOn = false;
                        ((BoardTipsItem)mAdapter.items.get(position)).likeOn = false;
                        ((BoardTipsItem)mAdapter.items.get(position)).likes = result.like_ids.size();
                    }
                }
            }
            @Override
            public void onFail(int code) {
                Log.d("NetworkERROR/", "BoardLikePOST"+code);
            }
        });
    }

    @Override
    public void onAdapterWarningCommentClick(BoardItemAdapter adapter, View view) {
        Toast.makeText(getActivity(), "댓글 Warning", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onAdapterTipsCommentClick(BoardItemAdapter adapter, View view) {
        Toast.makeText(getActivity(), "댓글 Tip", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAdapterWarningShareClick(BoardItemAdapter adapter, View view) {
        Toast.makeText(getActivity(), "공유하기 Warning", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onAdapterTipsShareClick(BoardItemAdapter adapter, View view) {
        Toast.makeText(getActivity(), "공유하기 Tip", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 로그인 정보 가져오기
        isLogined = UserManager.getInstance().getLoginState();
        user_id = UserManager.getInstance().getUser_id();
    }

    @Override
    public void onResume() {
        super.onResume();
        // 로그인 정보 가져오기
        isLogined = UserManager.getInstance().getLoginState();
        user_id = UserManager.getInstance().getUser_id();
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
