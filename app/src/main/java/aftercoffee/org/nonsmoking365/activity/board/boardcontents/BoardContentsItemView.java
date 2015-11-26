package aftercoffee.org.nonsmoking365.activity.board.boardcontents;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import aftercoffee.org.nonsmoking365.manager.UserManager;
import aftercoffee.org.nonsmoking365.R;

/**
 * Created by Tacademy on 2015-11-19.
 */
public class BoardContentsItemView extends FrameLayout {

    BoardContentsItem item;
    String docID;
    String user_id;

    DisplayImageOptions options;

    public BoardContentsItemView(Context context, String docID) {
        super(context);
        this.docID = docID;
        user_id = UserManager.getInstance().getUser_id();
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.icon_image_add)
                .showImageForEmptyUri(R.drawable.icon_cigarette)
                .showImageOnFail(R.drawable.icon_cigarette)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .considerExifParams(true)
//                .displayer(new RoundedBitmapDisplayer(50))
                .build();
        init();
    }

    LinearLayout like;
    LinearLayout share;
    ImageView contentImageView;
    TextView titleView;
    TextView contentView;
    Button likeBtn;
    ImageView likeImage;
    Button shareBtn;

    public interface OnBoardContentsBtnClickListener {
        public void onBoardContentLikeClick(View view, BoardContentsItem item);
        public void onBoardContentShareClick(View view, BoardContentsItem item);
    }
    OnBoardContentsBtnClickListener mListener;
    public void setOnBoardContentsBtnClickListener(OnBoardContentsBtnClickListener listener) {
        mListener = listener;
    }

    public void init() {
        inflate(getContext(), R.layout.view_board_header, this);

        like = (LinearLayout)findViewById(R.id.like);
        share= (LinearLayout)findViewById(R.id.share);
        titleView = (TextView)findViewById(R.id.text_title);
        contentView = (TextView)findViewById(R.id.text_content);
        contentImageView = (ImageView)findViewById(R.id.image_content);
        likeBtn = (Button)findViewById(R.id.btn_like);
        likeImage = (ImageView)findViewById(R.id.image_like);

        like.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onBoardContentLikeClick(BoardContentsItemView.this, item);
            }
        });

        share.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onBoardContentShareClick(BoardContentsItemView.this, item);
            }
        });
    }

    public void setContentItem(BoardContentsItem item) {
        this.item = item;
        titleView.setText(item.title);
        contentView.setText(item.content);
        ImageLoader.getInstance().displayImage(item.imageURL, contentImageView, options);
        likeBtn.setText("좋아요 "+item.likesCount);
        if (item.likeOn) {
            likeImage.setImageResource(R.drawable.icon_like_on);
        } else {
            likeImage.setImageResource(R.drawable.icon_like_off);
        }
    }
}