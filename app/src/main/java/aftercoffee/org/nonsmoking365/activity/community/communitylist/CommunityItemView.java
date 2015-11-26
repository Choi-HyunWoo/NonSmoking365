package aftercoffee.org.nonsmoking365.activity.community.communitylist;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import aftercoffee.org.nonsmoking365.R;

/**
 * Created by Tacademy on 2015-11-24.
 */
public class CommunityItemView extends FrameLayout {

    CommunityItem item;

    LinearLayout like;
    LinearLayout comment;
    ImageView userProfileImageView;
    TextView userNicknameView;
    TextView titleView;
    TextView createdView;
    ImageView likeImage;
    Button likeBtn;
    Button commentBtn;

    DisplayImageOptions options;

    public interface OnCommunityBtnClickListener {
        public void onCommunityLikeClick(CommunityItemView view, CommunityItem item);
        public void onCommunityCommentClick(CommunityItemView view, CommunityItem item);
    }
    OnCommunityBtnClickListener mListener;
    public void setOnCommunityBtnClickListener(OnCommunityBtnClickListener listener) {
        mListener = listener;
    }

    public CommunityItemView(Context context) {
        super(context);
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.icon_image_add)
                .showImageForEmptyUri(R.drawable.icon_cigarette)
                .showImageOnFail(R.drawable.icon_cigarette)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .considerExifParams(true)
                //.displayer(new SimpleBitmapDisplayer())
                .build();
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.view_community_item, this);
        like = (LinearLayout)findViewById(R.id.like);
        comment = (LinearLayout)findViewById(R.id.comment);
        userProfileImageView = (ImageView)findViewById(R.id.image_userProfileImg);
        userNicknameView = (TextView)findViewById(R.id.text_userNickname);
        titleView = (TextView)findViewById(R.id.text_title);
        createdView = (TextView)findViewById(R.id.text_created);
        // 좋아요 버튼
        likeBtn = (Button)findViewById(R.id.btn_like);
        likeImage = (ImageView)findViewById(R.id.image_like);
        // 댓글 버튼
        commentBtn = (Button)findViewById(R.id.btn_comment);

        like.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCommunityLikeClick(CommunityItemView.this, item);
            }
        });

        comment.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCommunityCommentClick(CommunityItemView.this, item);
            }
        });

    }

    public void setCommunityItem(CommunityItem item) {
        this.item = item;
        ImageLoader.getInstance().displayImage(item.userProfileImg, userProfileImageView, options);
        userNicknameView.setText(item.userNickname);
        titleView.setText(item.title);
        createdView.setText(item.created);
        likeBtn.setText("좋아요 "+item.likesCount);
        commentBtn.setText("댓글 "+item.commentsCount);
        if (item.likeOn) {
            likeImage.setImageResource(R.drawable.icon_like_on);
        } else {
            likeImage.setImageResource(R.drawable.icon_like_off);
        }
    }
}
