package aftercoffee.org.nonsmoking365.activity.community.communitylist;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import aftercoffee.org.nonsmoking365.R;
import aftercoffee.org.nonsmoking365.activity.board.boardlist.BoardItemAdapter;
import aftercoffee.org.nonsmoking365.activity.community.CommunityActivity;
import aftercoffee.org.nonsmoking365.data.Community;
import aftercoffee.org.nonsmoking365.data.CommunityDocs;
import aftercoffee.org.nonsmoking365.manager.NetworkManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class CommunityBoardFragment extends Fragment {

    public static final int COMMUNITY_PAGE_DISPLAY = 10;

    boolean isLogined;
    String selectedDocID;

    ListView listView;
    PullToRefreshListView refreshView;
    boolean isUpdate = false;
    CommunityItemAdapter mAdapter;

    public CommunityBoardFragment() {
        // Required empty public constructor
        this.setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_community_board, container, false);

        /**
         * PullToRefreshView
         */
        refreshView = (PullToRefreshListView)view.findViewById(R.id.list_community);
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
                    int startPage = startIndex / COMMUNITY_PAGE_DISPLAY;
                    if (startIndex != -1) {
                        isUpdate = true;
                        NetworkManager.getInstance().getCommunityData(getActivity(), COMMUNITY_PAGE_DISPLAY, startPage, new NetworkManager.OnResultListener<Community>() {
                            @Override
                            public void onSuccess(Community result) {
                                if (result != null) {
                                    for (CommunityDocs c : result.docsList) {
                                        CommunityItem item = new CommunityItem();
                                        item._id = c._id;
                                        item.title = c.title;
                                        item.contents = c.content;
                                        item.userNickname = c.user_id.nick;
                                        if (c.user_id.image_ids.size()!=0)
                                            item.userProfileImg = c.user_id.image_ids.get(0).uri;
                                        mAdapter.add(item);
                                    }
                                }
                                isUpdate = false;
                            }

                            @Override
                            public void onFail(int code) {
                                Log.d("Network ERROR", "communityGET" + code);
                                isUpdate = false;
                            }
                        });
                    }
                }
            }
        });

        listView = refreshView.getRefreshableView();
        mAdapter = new CommunityItemAdapter();
        listView.setAdapter(mAdapter);
        getBoard();     // 최초 가져오기

        // 게시글 목록에서 선택 시, 해당 게시글로 이동
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedDocID = mAdapter.getDocID(position-1);
                ((CommunityActivity)getActivity()).pushCommunityContentsFragment(selectedDocID);
            }
        });
        return view;
    }

    // 최초 가져오기 / 위에서 아래로 드래그 시 Refresh
    private void getBoard() {
        NetworkManager.getInstance().getCommunityData(getActivity(), COMMUNITY_PAGE_DISPLAY, 0, new NetworkManager.OnResultListener<Community>() {
            @Override
            public void onSuccess(Community result) {
                mAdapter.setTotalCount(result.count);
                mAdapter.clear();
                if (result != null) {
                    for (CommunityDocs c : result.docsList) {
                        CommunityItem item = new CommunityItem();
                        item._id = c._id;
                        item.title = c.title;
                        item.contents = c.content;
                        item.userNickname = c.user_id.nick;
                        item.created = c.created;
                        if (c.user_id.image_ids.size()!=0)
                            item.userProfileImg = c.user_id.image_ids.get(0).uri;
                        mAdapter.add(item);
                    }
                }
                refreshView.onRefreshComplete();            // Refresh 완료
            }

            @Override
            public void onFail(int code) {
                Log.d("Network ERROR", "communityGET" + code);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getBoard();
    }
}
