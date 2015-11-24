package aftercoffee.org.nonsmoking365.activity.community.communitylist;

import android.content.Context;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import aftercoffee.org.nonsmoking365.R;

/**
 * Created by Tacademy on 2015-11-24.
 */
public class CommunityItemView extends FrameLayout {

    ImageView userProfileImageView;
    TextView userNicknameView;
    TextView titleView;
    Button likeBtn;
    Button commentBtn;

    DisplayImageOptions options;

    public CommunityItemView(Context context) {
        super(context);
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.icon_image_add)
                .showImageForEmptyUri(R.drawable.icon_cigarette)
                .showImageOnFail(R.drawable.icon_cigarette)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .considerExifParams(true)
                //.displayer(new SimpleBitmapDisplayer())         // RoundedBitmapDisplayer()로
                .build();
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.view_community_item, this);
        userProfileImageView = (ImageView)findViewById(R.id.image_userProfileImage);
        userNicknameView = (TextView)findViewById(R.id.text_userNickname);
        titleView = (TextView)findViewById(R.id.text_title);
        likeBtn = (Button)findViewById(R.id.btn_like);
        commentBtn = (Button)findViewById(R.id.btn_comment);


    }
    public void setCommunityItem(CommunityItem item) {
        ImageLoader.getInstance().displayImage(item.userProfileImg, userProfileImageView, options);
        userNicknameView.setText(item.userNickname);
        titleView.setText(item.title);
    }
}
