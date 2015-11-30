package aftercoffee.org.nonsmoking365.activity.board.boardlist;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import aftercoffee.org.nonsmoking365.R;
import aftercoffee.org.nonsmoking365.data.Image;
import aftercoffee.org.nonsmoking365.manager.UserManager;

/**
 * Created by Tacademy on 2015-11-09.
 */
public class BoardWarningItemView extends FrameLayout {

    BoardWarningItem item;

    String docID;
    String user_id;

    public BoardWarningItemView(Context context, String docID) {
        super(context);
        this.docID = docID;
        this.user_id = UserManager.getInstance().getUser_id();
        init();
    }

    LinearLayout like;
    LinearLayout comment;
    LinearLayout share;
    ImageView titleImageView;
    TextView titleTextView;
    TextView contentsTextView;
    Button likeBtn;
    ImageView likeImage;
    Button commentsBtn;
    ImageView commentImage;
    Button shareBtn;

    /**
     * Board의 글 목록 ITEM
     */

    // ITEM의 click listener를 interface로 정의.
    public interface OnWarningBtnClickListener {
        public void onWarningLikeBtnClick(View view, BoardWarningItem item);
        public void onWarningCommentBtnClick(View view, BoardWarningItem item);
        public void onWarningShareBtnClick(View view);
    }
    public OnWarningBtnClickListener mListener;
    public void setOnWarningBtnClickListener(OnWarningBtnClickListener listener) {
        mListener = listener;
    }

    public void init() {
        inflate(getContext(), R.layout.view_board_warning_item, this);

        like = (LinearLayout)findViewById(R.id.like);
        comment = (LinearLayout)findViewById(R.id.comment);
        share = (LinearLayout)findViewById(R.id.share);
        titleImageView = (ImageView)findViewById(R.id.image_title);
        titleTextView = (TextView)findViewById(R.id.text_title);
        contentsTextView = (TextView)findViewById(R.id.text_contents);

        likeBtn = (Button)findViewById(R.id.btn_like);
        likeImage = (ImageView)findViewById(R.id.image_like);
        commentsBtn = (Button)findViewById(R.id.btn_comment);
        commentImage = (ImageView)findViewById(R.id.image_comment);
        shareBtn = (Button)findViewById(R.id.btn_share);

        // 좋아요 버튼
        like.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onWarningLikeBtnClick(BoardWarningItemView.this, item);
            }
        });
        // 댓글 버튼
        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onWarningCommentBtnClick(BoardWarningItemView.this, item);
            }
        });
        // 공유하기 버튼
        share.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onWarningShareBtnClick(BoardWarningItemView.this);
            }
        });
    }

    public void setBoardItem(BoardWarningItem item) {
        this.item = item;
        titleImageView.setBackgroundResource(item.titleImg);
        titleTextView.setText(item.title);
        contentsTextView.setText(item.contents);
        likeBtn.setText("좋아요 " + item.likesCount);
        commentsBtn.setText("댓글 "+item.commentsCount);
        if (item.likeOn) {
            likeImage.setImageResource(R.drawable.icon_like_on);
        } else {
            likeImage.setImageResource(R.drawable.icon_like_off);
        }
        if (item.commentsCount != 0) {
            commentImage.setImageResource(R.drawable.icon_comment_active);
        } else {
            commentImage.setImageResource(R.drawable.icon_comment);
        }
    }

}
