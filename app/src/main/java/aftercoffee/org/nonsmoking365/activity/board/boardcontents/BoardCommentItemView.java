package aftercoffee.org.nonsmoking365.activity.board.boardcontents;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import aftercoffee.org.nonsmoking365.data.BoardDocs;
import aftercoffee.org.nonsmoking365.manager.NetworkManager;
import aftercoffee.org.nonsmoking365.manager.UserManager;
import aftercoffee.org.nonsmoking365.R;

/**
 * Created by Tacademy on 2015-11-19.
 */
public class BoardCommentItemView extends FrameLayout {

    Context context;
    DisplayImageOptions options;

    public BoardCommentItemView(Context context) {
        super(context);
        this.context = context;
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.icon_image_add)
                .showImageForEmptyUri(R.drawable.icon_cigarette)
                .showImageOnFail(R.drawable.icon_cigarette)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .considerExifParams(true)
                .displayer(new SimpleBitmapDisplayer())         // RoundedBitmapDisplayer()로
                .build();
        init();
    }

    String docID;       // 소속 글 _id
    String _id;         // 댓글의 _id
    String user_id;     // 작성자 _id
    String profileImgURL;

    ImageView profileImageView;
    TextView userNicknameView;
    TextView contentView;
    TextView dateView;
    TextView deleteView;

    public interface OnDeleteClickListener {
        public void onDeleteClick(View view);
    }
    OnDeleteClickListener mListener;
    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        mListener = listener;
    }


    public void init() {
        inflate(getContext(), R.layout.view_comment_item, this);

        profileImageView = (ImageView) findViewById(R.id.image_profileImg);
        userNicknameView = (TextView) findViewById(R.id.text_userNickname);
        contentView = (TextView) findViewById(R.id.text_content);
        dateView = (TextView) findViewById(R.id.text_date);
        deleteView = (TextView)findViewById(R.id.text_delete);          // clickListener

        // 삭제 버튼
        deleteView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onDeleteClick(BoardCommentItemView.this);
                    // Listener를 통해 이 뷰를 소유한 객체에서 구현부를 작성하도록 하겠다. (Has-a Interface)
                }
            }
        });
    }

    public void setCommentItem(BoardCommentItem item) {
        docID = item.docID;
        _id = item._id;
        user_id = item.user_id;
        profileImgURL = item.profileImgURL;
        ImageLoader.getInstance().displayImage(profileImgURL, profileImageView, options);
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