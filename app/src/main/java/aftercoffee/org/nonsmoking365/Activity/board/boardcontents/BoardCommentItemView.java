package aftercoffee.org.nonsmoking365.Activity.board.boardcontents;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import aftercoffee.org.nonsmoking365.Data.BoardDocs;
import aftercoffee.org.nonsmoking365.Manager.NetworkManager;
import aftercoffee.org.nonsmoking365.Manager.UserManager;
import aftercoffee.org.nonsmoking365.R;

/**
 * Created by Tacademy on 2015-11-19.
 */
public class BoardCommentItemView extends FrameLayout {

    Context context;

    public BoardCommentItemView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    String docID;       // 소속 글 _id
    String _id;         // 댓글의 _id
    String user_id;     // 작성자 _id

    ImageView profileImageView;
    TextView userNicknameView;
    TextView contentView;
    TextView dateView;
    TextView deleteView;


    public void init() {
        inflate(getContext(), R.layout.view_comment_item, this);

        profileImageView = (ImageView) findViewById(R.id.image_profileImg);
        userNicknameView = (TextView) findViewById(R.id.text_userNickname);
        contentView = (TextView) findViewById(R.id.text_content);
        dateView = (TextView) findViewById(R.id.text_date);
        deleteView = (TextView)findViewById(R.id.text_delete);          // clickListener

        deleteView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                NetworkManager.getInstance().postBoardCommentDelete(context, docID, _id, new NetworkManager.OnResultListener<BoardDocs>() {
                    @Override
                    public void onSuccess(BoardDocs result) {

                    }
                    @Override
                    public void onFail(int code) {

                    }
                });
            }
        });
    }

    public void setCommentItem(BoardCommentItem item) {
        docID = item.docID;
        _id = item._id;
        user_id = item.user_id;
        profileImageView.setBackgroundResource(R.drawable.icon_profile_default);
        userNicknameView.setText(item.nickname);
        contentView.setText(item.content);
        dateView.setText(item.date);
        if(user_id.equals(UserManager.getInstance().getUser_id())) {
            deleteView.setVisibility(VISIBLE);
        } else {
            deleteView.setVisibility(GONE);
        }
    }
}