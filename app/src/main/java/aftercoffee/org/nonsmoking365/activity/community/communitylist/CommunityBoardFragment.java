package aftercoffee.org.nonsmoking365.activity.community.communitylist;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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

import aftercoffee.org.nonsmoking365.R;
import aftercoffee.org.nonsmoking365.activity.community.CommunityActivity;
import aftercoffee.org.nonsmoking365.activity.login.LoginActivity;
import aftercoffee.org.nonsmoking365.data.Community;
import aftercoffee.org.nonsmoking365.data.CommunityDocs;
import aftercoffee.org.nonsmoking365.data.LikesResult;
import aftercoffee.org.nonsmoking365.manager.NetworkManager;
import aftercoffee.org.nonsmoking365.manager.UserManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class CommunityBoardFragment extends Fragment implements CommunityItemAdapter.OnAdapterBtnClickListener {

    public static final int COMMUNITY_PAGE_DISPLAY = 10;

    boolean isLogined;
    String user_id;
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle("금연 갤러리");
        isLogined = UserManager.getInstance().getLoginState();
        user_id = UserManager.getInstance().getUser_id();
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
                                        // 글 정보
                                        item._id = c._id;
                                        item.title = c.title;
                                        item.contents = c.content;
                                        // 유저 정보
                                        item.userNickname = c.user_id.nick;
                                        if (c.user_id.image_ids.size()!=0)
                                            item.userProfileImgURL = c.user_id.image_ids.get(0).uri;
                                        // 좋아요, 댓글 수
                                        item.likesCount = c.like_ids.size();
                                        item.commentsCount = c.commentsList.size();
                                        item.likeOn = false;
                                        if (isLogined) {
                                            for (String id : c.like_ids) {
                                                if (user_id.equals(id)) {
                                                    item.likeOn = true;         // 좋아요 ON
                                                }
                                            }
                                        }
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
        mAdapter.setOnAdapterBtnClickListener(this);
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
                        // 글 정보
                        item._id = c._id;
                        item.title = c.title;
                        item.contents = c.content;
                        // 유저 정보
                        item.userNickname = c.user_id.nick;
                        if (c.user_id.image_ids.size()!=0)
                            item.userProfileImgURL = c.user_id.image_ids.get(0).uri;
                        // 좋아요, 댓글 수
                        item.likesCount = c.like_ids.size();
                        item.commentsCount = c.commentsList.size();
                        item.likeOn = false;
                        if (isLogined) {
                            for (String id : c.like_ids) {
                                if (user_id.equals(id)) {
                                    item.likeOn = true;      // 좋아요 ON
                                }
                            }
                        }
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

    // 좋아요 버튼
    @Override
    public void onAdapterLikeClick(CommunityItemView view, final CommunityItem item) {
        if (!isLogined) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("로그인");
            builder.setMessage("좋아요는 회원만 가능합니다\n로그인 페이지로 이동하시겠습니까?");
            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
            });
            builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            AlertDialog dlg = builder.create();
            dlg.show();
    } else {
        final ImageView likeImage = (ImageView) view.findViewById(R.id.image_like);
        final Button likeBtn = (Button) view.findViewById(R.id.btn_like);
        final int position = mAdapter.items.indexOf(item);
        String docID = item._id;
        NetworkManager.getInstance().postCommunityLike(getActivity(), docID, user_id, new NetworkManager.OnResultListener<LikesResult>() {
            @Override
            public void onSuccess(LikesResult result) {
                likeBtn.setText("좋아요 " + result.like_ids.size());
                // 좋아요 ON > OFF (result.like_ids.size가 0인경우 for루프를 돌지 않으므로 Default를 OFF로 설정하겠음)
                likeImage.setImageResource(R.drawable.icon_like_off);
                item.likeOn = false;
                ((CommunityItem)mAdapter.getItem(position)).likeOn = false;
                ((CommunityItem)mAdapter.getItem(position)).likesCount = result.like_ids.size();
                // 좋아요 OFF > ON 인지 확인
                for (String id : result.like_ids) {
                    if (user_id.equals(id)) {
                        likeImage.setImageResource(R.drawable.icon_like_on);
                        item.likeOn = true;
                        ((CommunityItem)mAdapter.getItem(position)).likeOn = true;
                        ((CommunityItem)mAdapter.getItem(position)).likesCount = result.like_ids.size();
                        break;
                    }
                }
            }

            @Override
            public void onFail(int code) {
                Log.d("NetworkERROR/", "CommunityLikePOST" + code);
            }
        });
    }
    }
    // 댓글 버튼
    @Override
    public void onAdapterCommentClick(CommunityItemView view, CommunityItem item) {
        Toast.makeText(getActivity(), "댓글", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        getBoard();
        isLogined = UserManager.getInstance().getLoginState();
        user_id = UserManager.getInstance().getUser_id();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home :
                getActivity().finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
