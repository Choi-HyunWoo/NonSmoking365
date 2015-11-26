package aftercoffee.org.nonsmoking365.activity.board.boardlist;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import aftercoffee.org.nonsmoking365.R;
import aftercoffee.org.nonsmoking365.data.LikesResult;
import aftercoffee.org.nonsmoking365.manager.NetworkManager;
import aftercoffee.org.nonsmoking365.manager.UserManager;

/**
 * Created by Tacademy on 2015-11-09.
 */
public class BoardTipsItemView extends FrameLayout {

    BoardTipsItem item;

    String docID;
    String user_id;

    public BoardTipsItemView(Context context, String docID) {
        super(context);
        this.docID = docID;
        this.user_id = UserManager.getInstance().getUser_id();
        init();
    }

    ImageView titleImageView;
    TextView titleTextView;
    TextView contentsTextView;
    Button likeBtn;
    ImageView likeImage;
    Button commentsBtn;
    Button shareBtn;

    /**
     * Board의 글 목록 ITEM
     */

    // ITEM의 click listener를 interface로 정의.
    public interface OnTipsBtnClickListener {
        public void onTipsLikeBtnClick(View view, BoardTipsItem item);
        public void onTipsCommentBtnClick(View view, BoardTipsItem item);
        public void onTipsShareBtnClick(View view);
    }
    public OnTipsBtnClickListener mListener;
    public void setOnTipsBtnClickListener(OnTipsBtnClickListener listener) {
        mListener = listener;
    }

    public void init() {
        inflate(getContext(), R.layout.view_board_tips_item, this);

        titleImageView = (ImageView)findViewById(R.id.image_title);
        titleTextView = (TextView)findViewById(R.id.text_title);
        contentsTextView = (TextView)findViewById(R.id.text_contents);

        likeBtn = (Button)findViewById(R.id.btn_like);
        likeImage = (ImageView)findViewById(R.id.image_like);
        commentsBtn = (Button)findViewById(R.id.btn_comment);
        shareBtn = (Button)findViewById(R.id.btn_share);

        // 좋아요 버튼
        likeBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onTipsLikeBtnClick(BoardTipsItemView.this, item);
            }
        });
        // 댓글 버튼
        commentsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onTipsCommentBtnClick(BoardTipsItemView.this, item);
            }
        });
        // 공유하기 버튼
        shareBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onTipsShareBtnClick(BoardTipsItemView.this);
            }
        });
    }

    public void setBoardItem(BoardTipsItem item) {
        this.item = item;
        titleImageView.setBackgroundResource(item.titleImg);
        titleTextView.setText(item.title);
        contentsTextView.setText(item.contents);
        if (item.likeOn) {
            likeBtn.setText("좋아요 " + item.likesCount+"ON");
            likeImage.setImageResource(R.drawable.icon_like_active);
        } else {
            likeBtn.setText("좋아요 " + item.likesCount+"OFF");
            likeImage.setImageResource(R.drawable.icon_like);
        }
    }
}
