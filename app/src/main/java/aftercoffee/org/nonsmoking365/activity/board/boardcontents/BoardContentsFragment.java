package aftercoffee.org.nonsmoking365.activity.board.boardcontents;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;

import aftercoffee.org.nonsmoking365.activity.board.BoardActivity;
import aftercoffee.org.nonsmoking365.activity.login.LoginActivity;
import aftercoffee.org.nonsmoking365.data.BoardDocs;
import aftercoffee.org.nonsmoking365.manager.NetworkManager;
import aftercoffee.org.nonsmoking365.manager.UserManager;
import aftercoffee.org.nonsmoking365.R;

public class BoardContentsFragment extends Fragment implements BoardContentsAdapter.OnAdapterDeleteListener {

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

    ListView listView;                  // 글내용 (HeaderView) + 댓글 (List) + 댓글작성란 (FooterView)
    EditText commentView;
    Button commentSendBtn;
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
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_board_contents, container, false);
        listView = (ListView)view.findViewById(R.id.list_comment);
        commentView = (EditText)view.findViewById(R.id.edit_comment);
        commentSendBtn = (Button)view.findViewById(R.id.btn_comment_send);

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
                // 본문
                listView.addHeaderView(contentView, null, false);
                mAdapter = new BoardContentsAdapter();
                mAdapter.setOnAdapterDeleteListener(BoardContentsFragment.this);
                listView.setAdapter(mAdapter);
                // 댓글
                if (result.commentsList.size() != 0) {
                    for (int i = 0; i < result.commentsList.size(); i++) {
                        // 댓글
                        BoardCommentItem comment = new BoardCommentItem();
                        comment.docID = result._id;
                        comment._id = result.commentsList.get(i)._id;
                        if (result.commentsList.get(i).user_id.image_ids.size() != 0) {
                            comment.profileImgURL = result.commentsList.get(i).user_id.image_ids.get(0).uri;
                        }
                        comment.user_id = result.commentsList.get(i).user_id._id;
                        comment.nickname = result.commentsList.get(i).user_id.nick;
                        comment.content = result.commentsList.get(i).content;
                        comment.date = result.commentsList.get(i).created;
                        mAdapter.addCommentItem(comment);
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
                Log.d("Network_Error/", "BoardContentGET "+code);
            }
        });

        // 댓글 작성란 (클릭시 맨 아래 댓글이 보이도록)
        commentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listView.smoothScrollToPosition(mAdapter.getCount());
            }
        });
        // 댓글 등록
        commentSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 로그인인 경우에만 댓글 달기 가능
                if (isLogined) {
                    String content = commentView.getText().toString();
                    if (!TextUtils.isEmpty(content)) {
                        String user_id = UserManager.getInstance().getUser_id();
                        NetworkManager.getInstance().postBoardComment(getContext(), docID, user_id, content, new NetworkManager.OnResultListener<BoardDocs>() {
                            @Override
                            public void onSuccess(BoardDocs result) {
                                Toast.makeText(getContext(), "댓글이 등록되었습니다", Toast.LENGTH_SHORT).show();
                                mAdapter.clear();
                                if (result.commentsList.size() != 0) {
                                    for (int i = 0; i < result.commentsList.size(); i++) {
                                        // 댓글 추가
                                        BoardCommentItem comment = new BoardCommentItem();
                                        // 프로필사진 <<
                                        comment.docID = result._id;
                                        comment._id = result.commentsList.get(i)._id;
                                        comment.user_id = result.commentsList.get(i).user_id._id;
                                        comment.nickname = result.commentsList.get(i).user_id.nick;
                                        comment.content = result.commentsList.get(i).content;
                                        comment.date = result.commentsList.get(i).created;
                                        mAdapter.addCommentItem(comment);
                                    }
                                }
                                listView.smoothScrollToPosition(mAdapter.getCount());
                                commentView.setText("");
                            }
                            @Override
                            public void onFail(int code) {
                                Log.d("Network_Error/", "CommentPOST "+code);
                            }
                        });
                    }
                }
                // 비 로그인시 처리
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("로그인");
                    builder.setMessage("댓글 작성은 회원만 가능합니다\n로그인 페이지로 이동하시겠습니까?");
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
                }
            }
        });

        return view;
    }

    // 댓글 삭제
    @Override
    public void onAdapterDelete(BoardContentsAdapter adapter, View view) {
        final String comment_id = ((BoardCommentItemView)view)._id;
        NetworkManager.getInstance().deleteBoardCommentDelete(getContext(), docID, comment_id, new NetworkManager.OnResultListener<BoardDocs>() {
            @Override
            public void onSuccess(BoardDocs result) {
                // Adapter item을 갱신
                Toast.makeText(getActivity(), "댓글이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                mAdapter.clear();
                if (result.commentsList.size() != 0) {
                    for (int i = 0; i < result.commentsList.size(); i++) {
                        // 댓글 추가
                        BoardCommentItem comment = new BoardCommentItem();
                        // 프로필사진 <<
                        comment.docID = result._id;
                        comment._id = result.commentsList.get(i)._id;
                        comment.user_id = result.commentsList.get(i).user_id._id;
                        comment.nickname = result.commentsList.get(i).user_id.nick;
                        comment.content = result.commentsList.get(i).content;
                        comment.date = result.commentsList.get(i).created;
                        mAdapter.addCommentItem(comment);
                    }
                }
            }

            @Override
            public void onFail(int code) {
                Log.d("Network_Error/", "CommentDELETE " + code);
            }
        });
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