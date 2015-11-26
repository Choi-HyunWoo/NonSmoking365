package aftercoffee.org.nonsmoking365.activity.community.communitycontents;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import aftercoffee.org.nonsmoking365.R;
import aftercoffee.org.nonsmoking365.activity.board.boardcontents.BoardContentsItem;
import aftercoffee.org.nonsmoking365.data.LikesResult;
import aftercoffee.org.nonsmoking365.manager.NetworkManager;
import aftercoffee.org.nonsmoking365.manager.UserManager;

/**
 * Created by HYUNWOO on 2015-11-25.
 */
public class CommunityContentsItemView extends FrameLayout {

    CommunityContentsItem item;
    Context context;
    String docID;
    String user_id;

    DisplayImageOptions options;

    public CommunityContentsItemView(Context context, String docID) {
        super(context);
        this.context = context;
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
    ImageView userProfileImageView;
    TextView userNicknameView;
    TextView titleView;
    TextView contentView;
    ImageView contentImageView;

    ImageView likeImage;
    Button likeBtn;
    Button shareBtn;

    public interface OnCommunityContentsBtnClickListener {
        public void onCommunityContentsLikeClick(CommunityContentsItemView view, CommunityContentsItem item);
        public void onCommunityContentsShareClick(CommunityContentsItemView view, CommunityContentsItem item);
    }

    OnCommunityContentsBtnClickListener mListener;

    public void setOnCommunityContentsBtnClickListener (OnCommunityContentsBtnClickListener listener) {
        mListener = listener;
    }

    public void init() {
        inflate(getContext(), R.layout.view_community_header, this);

        like = (LinearLayout)findViewById(R.id.like);
        share =(LinearLayout)findViewById(R.id.share);
        userProfileImageView = (ImageView)findViewById(R.id.image_userProfileImg);
        userNicknameView = (TextView)findViewById(R.id.text_userNickname);
        titleView = (TextView)findViewById(R.id.text_title);
        contentView = (TextView)findViewById(R.id.text_content);
        contentImageView = (ImageView)findViewById(R.id.image_content);

        likeImage = (ImageView)findViewById(R.id.image_like);
        likeBtn = (Button)findViewById(R.id.btn_like);
        shareBtn = (Button)findViewById(R.id.btn_share);

        like.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCommunityContentsLikeClick(CommunityContentsItemView.this, item);
            }
        });

        share.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCommunityContentsShareClick(CommunityContentsItemView.this, item);
            }
        });
    }


    public void setContentItem(CommunityContentsItem item) {
        this.item = item;
        ImageLoader.getInstance().displayImage(item.userProfileImageURL, userProfileImageView, options);
        userNicknameView.setText(item.userNickname);
        titleView.setText(item.title);
        contentView.setText(item.content);
        ImageLoader.getInstance().displayImage(item.contentImageURL, contentImageView, options);
        likeBtn.setText("좋아요 " + item.likesCount);
        if (item.likeOn) {
            likeImage.setImageResource(R.drawable.icon_like_on);
        } else {
            likeImage.setImageResource(R.drawable.icon_like_off);
        }
    }
}
