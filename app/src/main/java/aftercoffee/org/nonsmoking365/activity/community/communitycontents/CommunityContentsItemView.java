package aftercoffee.org.nonsmoking365.activity.community.communitycontents;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
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

    ImageView userProfileImageView;
    TextView userNicknameView;
    TextView titleView;
    TextView contentView;
    ImageView contentImageView;
    Button likeBtn;
    Button shareBtn;

    public void init() {
        inflate(getContext(), R.layout.view_community_header, this);

        userProfileImageView = (ImageView)findViewById(R.id.image_userProfileImg);
        userNicknameView = (TextView)findViewById(R.id.text_userNickname);
        titleView = (TextView)findViewById(R.id.text_title);
        contentView = (TextView)findViewById(R.id.text_content);
        contentImageView = (ImageView)findViewById(R.id.image_content);

        likeBtn = (Button)findViewById(R.id.btn_like);
        shareBtn = (Button)findViewById(R.id.btn_share);

        likeBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                NetworkManager.getInstance().postCommunityLike(getContext(), docID, user_id, new NetworkManager.OnResultListener<LikesResult>() {
                    @Override
                    public void onSuccess(LikesResult result) {
                        if (result.likes > 999) {
                            likeBtn.setText("좋아요 999+");
                        } else {
                            likeBtn.setText("좋아요 " + result.likes);
                        }
                    }

                    @Override
                    public void onFail(int code) {
                        Log.d("BoardContentItemView:", "network error/" + code);
                    }
                });
            }
        });

        shareBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 게시글 공유하기 구현 (Facebook)
                Toast.makeText(context, "구현중입니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void setContentItem(CommunityContentsItem item) {
        ImageLoader.getInstance().displayImage(item.userProfileImageURL, userProfileImageView, options);
        userNicknameView.setText(item.userNickname);
        titleView.setText(item.title);
        contentView.setText(item.content);
        ImageLoader.getInstance().displayImage(item.contentImageURL, contentImageView, options);
        if (item.likes > 999) {
            likeBtn.setText("좋아요 999+");
        } else {
            likeBtn.setText("좋아요 "+item.likes);
        }
    }
}
