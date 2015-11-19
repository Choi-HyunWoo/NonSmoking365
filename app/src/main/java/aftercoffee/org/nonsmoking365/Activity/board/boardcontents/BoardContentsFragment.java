package aftercoffee.org.nonsmoking365.Activity.board.boardcontents;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import aftercoffee.org.nonsmoking365.Activity.board.BoardActivity;
import aftercoffee.org.nonsmoking365.Data.BoardDocs;
import aftercoffee.org.nonsmoking365.Data.LikesResult;
import aftercoffee.org.nonsmoking365.Manager.NetworkManager;
import aftercoffee.org.nonsmoking365.Manager.UserManager;
import aftercoffee.org.nonsmoking365.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BoardContentsFragment extends Fragment {

    boolean isLogined;

    /**
     * 글 선택 시, 글 내용을 가져오는 화면
     * 선택된 글의 ID를 fragment parameter로 가져와서 글 Data를 서버에서 받아올 것.

     * <Layout>
     * 글 내용 , 댓글 리스트 , 댓글 적는 란이 있다.
     * (글 내용은 댓글 리스트의 HeaderView로 붙일 것)
     * 글 내용 [HeaderView] ; 글 title, 글 content, 카테고리, 좋아요 Button, 공유하기 Button
     * 댓글 리스트 [ListView]
     * 댓글 적는 란 [EditText], 등록 버튼 [Button]
     */

    // The fragment initialization parameter
    // 선택된 글의 ID
    private static final String ARG_DOCID = "selected_docID";
    private String docID;       // Board List 에서 선택된 글의 ID

    public static BoardContentsFragment newInstance(String selectedDocID) {
        BoardContentsFragment fragment = new BoardContentsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_DOCID, selectedDocID);
        fragment.setArguments(args);
        return fragment;
    }

    DisplayImageOptions options;

    public BoardContentsFragment() {
        // Required empty public constructor
        // Universal ImageLoader의 option을 초기화
        this.setHasOptionsMenu(true);
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.icon_image_add)
                .showImageForEmptyUri(R.drawable.icon_cigarette)
                .showImageOnFail(R.drawable.icon_cigarette)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .considerExifParams(true)
//                .displayer(new RoundedBitmapDisplayer(50))
                .build();
    }

    ListView listView;                  // 글내용 (헤더) + 댓글 (리스트)
    BoardContentsAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            docID = getArguments().getString(ARG_DOCID);
        }
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle("글 내용");
        isLogined = UserManager.getInstance().getLoginState();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_board_contents, container, false);
        listView = (ListView)view.findViewById(R.id.list_comment);

        // 글 내용과 댓글들 가져오기
        NetworkManager.getInstance().getBoardContentAndComments(getContext(), docID, new NetworkManager.OnResultListener<BoardDocs>() {
            @Override
            public void onSuccess(BoardDocs result) {
                // 글 내용 담기
                BoardContentsItemView contentView = new BoardContentsItemView(getContext(), docID);
                BoardContentsItem contents = new BoardContentsItem();         // 글 내용 (헤더)
                contents.title = result.title;
                contents.content = result.content;
                contents.likes = result.like_ids.size();
                if (result.image_ids.size() != 0)
                    contents.imageURL = result.image_ids.get(0).uri;
                contentView.setContentItem(contents);

                // ListView settings
                listView.addHeaderView(contentView, null, false);
                mAdapter = new BoardContentsAdapter();
                listView.setAdapter(mAdapter);

                // 댓글 추가하기
                if (result.commentsList.size() != 0) {
                    for (int i = 0; i < result.commentsList.size(); i++) {
                        // 댓글 추가
                        BoardCommentItem comment = new BoardCommentItem();
                        // 프로필사진 <<
                        comment.nickname = result.commentsList.get(i).user_id.nick;
                        comment.content = result.commentsList.get(i).content;
                        comment.date = result.commentsList.get(i).created;
                        mAdapter.addComment(comment);
                    }
                }
                /*
                // 이미지 갯수 늘어나면 동적으로 imageview 생성하여 추가해줄 것.
                if (result.image_ids.size() != 0)
                    ImageLoader.getInstance().displayImage(result.image_ids.get(0).uri, imageView, options);
                categoryView.setText(result.category);                  // 한글로 수정할것
                */
            }

            @Override
            public void onFail(int code) {
                Log.d("BoardContent Load ", "network error/" + code);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        isLogined = UserManager.getInstance().getLoginState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home :
                ((BoardActivity)getActivity()).popFragment();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}