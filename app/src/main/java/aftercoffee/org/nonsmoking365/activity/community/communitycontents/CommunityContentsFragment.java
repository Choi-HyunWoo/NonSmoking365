package aftercoffee.org.nonsmoking365.activity.community.communitycontents;


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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;

import aftercoffee.org.nonsmoking365.R;
import aftercoffee.org.nonsmoking365.activity.board.boardcontents.CommentItem;
import aftercoffee.org.nonsmoking365.activity.board.boardcontents.CommentItemView;
import aftercoffee.org.nonsmoking365.activity.board.boardcontents.ContentsAdapter;
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
public class CommunityContentsFragment extends Fragment implements ContentsAdapter.OnAdapterDeleteListener, CommunityContentsItemView.OnCommunityContentsBtnClickListener {

    boolean isLogined;
    String user_id;

    private static final String ARG_DOCID = "selected_docID";
    private String docID;       // Board List 에서 선택된 글의 ID

    public static CommunityContentsFragment newInstance(String selectedDocID) {
        CommunityContentsFragment fragment = new CommunityContentsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_DOCID, selectedDocID);
        fragment.setArguments(args);
        return fragment;
    }

    DisplayImageOptions options;

    public CommunityContentsFragment() {
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
    //CommunityContentsAdapter mAdapter;
    ContentsAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            docID = getArguments().getString(ARG_DOCID);
        }
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle("글 내용");
        isLogined = UserManager.getInstance().getLoginState();
        user_id = UserManager.getInstance().getUser_id();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_community_contents, container, false);
        listView =(ListView)view.findViewById(R.id.list_comment);
        commentView = (EditText)view.findViewById(R.id.edit_comment);
        commentSendBtn = (Button)view.findViewById(R.id.btn_comment_send);

        NetworkManager.getInstance().getCommunityContentAndComments(getContext(), docID, new NetworkManager.OnResultListener<CommunityDocs>() {
            @Override
            public void onSuccess(CommunityDocs result) {
                // 글 내용 담기
                CommunityContentsItemView contentView = new CommunityContentsItemView(getContext(), docID);
                CommunityContentsItem contents = new CommunityContentsItem();         // 글 내용 (헤더)
                // 유저 정보
                contents.userNickname = result.user_id.nick;
                if (result.user_id.image_ids.size() != 0) {
                    contents.userProfileImageURL = result.user_id.image_ids.get(0).uri;
                }
                // 글 정보
                contents.title = result.title;
                contents.content = result.content;
                contents.created = result.created;
                if (result.image_ids.size() != 0)
                    contents.contentImageURL = result.image_ids.get(0).uri;
                // 좋아요, 댓글 수 정보
                contents.commentsCount = result.commentsList.size();
                contents.likesCount = result.like_ids.size();
                contents.likeOn = false;
                if (isLogined) {
                    for (String id : result.like_ids) {
                        if (user_id.equals(id)) {
                            contents.likeOn = true;
                            break;
                        }
                    }
                }
                contentView.setContentItem(contents);
                contentView.setOnCommunityContentsBtnClickListener(CommunityContentsFragment.this);

                // ListView settings
                // 본문
                listView.addHeaderView(contentView, null, false);
                mAdapter = new ContentsAdapter();
                mAdapter.setOnAdapterDeleteListener(CommunityContentsFragment.this);
                listView.setAdapter(mAdapter);
                // 댓글
                if (result.commentsList.size() != 0) {
                    for (int i = 0; i < result.commentsList.size(); i++) {
                        // 댓글
                        CommentItem comment = new CommentItem();
                        comment.docID = result._id;
                        comment._id = result.commentsList.get(i)._id;
                        if (result.commentsList.get(i).user_id.image_ids.size() != 0) {
                            comment.userProfileImgURL = result.commentsList.get(i).user_id.image_ids.get(0).uri;
                        }
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
                Log.d("Network_Error/", "CommunityContentGET " + code);
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
                        NetworkManager.getInstance().postCommunityComment(getContext(), docID, user_id, content, new NetworkManager.OnResultListener<CommunityDocs>() {
                            @Override
                            public void onSuccess(CommunityDocs result) {
                                Toast.makeText(getContext(), "댓글이 등록되었습니다", Toast.LENGTH_SHORT).show();
                                mAdapter.clear();
                                if (result.commentsList.size() != 0) {
                                    for (int i = 0; i < result.commentsList.size(); i++) {
                                        // 댓글 추가
                                        CommentItem comment = new CommentItem();
                                        comment.docID = result._id;                         // 글 _id
                                        comment._id = result.commentsList.get(i)._id;       // 댓글 _id
                                        // 유저정보
                                        comment.user_id = result.commentsList.get(i).user_id._id;
                                        if (result.commentsList.get(i).user_id.image_ids.size() != 0) {
                                            comment.userProfileImgURL = result.commentsList.get(i).user_id.image_ids.get(0).uri;
                                        }
                                        comment.nickname = result.commentsList.get(i).user_id.nick;
                                        comment.content = result.commentsList.get(i).content;
                                        comment.date = result.commentsList.get(i).created;
                                        mAdapter.addCommentItem(comment);
                                    }
                                }
                                // Keyboard 내리기
                                imm = (InputMethodManager)getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                                // 작성된 댓글로 list 이동
                                listView.smoothScrollToPosition(mAdapter.getCount());
                                commentView.setText("");
                            }

                            @Override
                            public void onFail(int code) {
                                Log.d("Network_Error/", "CommunityCommentPOST " + code);
                            }
                        });
                    }
                }
                // 비 로그인시 처리
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setIcon(R.drawable.icon_logo_black);
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

    // 좋아요
    @Override
    public void onCommunityContentsLikeClick(CommunityContentsItemView view, final CommunityContentsItem item) {
        if (!isLogined) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setIcon(R.drawable.icon_logo_black);
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
            final Button likeBtn = (Button) view.findViewById(R.id.btn_like);
            final ImageView likeImage = (ImageView) view.findViewById(R.id.image_like);
            NetworkManager.getInstance().postCommunityLike(getActivity(), docID, user_id, new NetworkManager.OnResultListener<LikesResult>() {
                @Override
                public void onSuccess(LikesResult result) {
                    likeBtn.setText("좋아요 " + result.like_ids.size());
                    // 좋아요 ON > OFF (result.like_ids.size가 0인경우 for루프를 돌지 않으므로 Default를 OFF로 설정하겠음)
                    likeImage.setImageResource(R.drawable.icon_like_off);
                    item.likeOn = false;
                    item.likesCount = result.like_ids.size();
                    // 좋아요 OFF > ON 확인
                    for (String id : result.like_ids) {
                        if (user_id.equals(id)) {
                            likeImage.setImageResource(R.drawable.icon_like_on);
                            item.likeOn = true;
                            item.likesCount = result.like_ids.size();
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

    // 공유하기
    @Override
    public void onCommunityContentsShareClick(CommunityContentsItemView view, CommunityContentsItem item) {
        Toast.makeText(getActivity(), "구현중입니다.", Toast.LENGTH_SHORT).show();
    }

    // 댓글 삭제
    @Override
    public void onAdapterDelete(ContentsAdapter adapter, final View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setIcon(R.drawable.icon_logo_black);
        builder.setTitle("댓글 삭제");
        builder.setMessage("댓글을 삭제하시겠습니까?");
        builder.setNeutralButton("삭제", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String comment_id = ((CommentItemView)view)._id;
                NetworkManager.getInstance().deleteCommunityCommentDelete(getContext(), docID, comment_id, new NetworkManager.OnResultListener<CommunityDocs>() {
                    @Override
                    public void onSuccess(CommunityDocs result) {
                        // Adapter item을 갱신
                        Toast.makeText(getActivity(), "댓글이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                        mAdapter.clear();
                        if (result.commentsList.size() != 0) {
                            for (int i = 0; i < result.commentsList.size(); i++) {
                                // 댓글 추가
                                CommentItem comment = new CommentItem();
                                comment.docID = result._id;                         // 글 _id
                                comment._id = result.commentsList.get(i)._id;       // 댓글 _id
                                // 유저정보
                                comment.user_id = result.commentsList.get(i).user_id._id;
                                if (result.commentsList.get(i).user_id.image_ids.size() != 0) {
                                    comment.userProfileImgURL = result.commentsList.get(i).user_id.image_ids.get(0).uri;
                                }
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
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dlg = builder.create();
        dlg.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        isLogined = UserManager.getInstance().getLoginState();
        user_id = UserManager.getInstance().getUser_id();
        CommunityActivity.fab.setVisibility(View.GONE);
    }

    @Override
    public void onStop() {
        super.onStop();
        CommunityActivity.fab.setVisibility(View.VISIBLE);
    }

    InputMethodManager imm;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home :
                // Keyboard 내리기
                imm = (InputMethodManager)getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                // Fragment POP
                ((CommunityActivity)getActivity()).popFragment();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
