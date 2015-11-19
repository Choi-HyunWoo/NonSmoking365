package aftercoffee.org.nonsmoking365.Activity.board;

import android.content.Context;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import aftercoffee.org.nonsmoking365.R;

/**
 * Created by Tacademy on 2015-11-19.
 */
public class BoardCommentItemView extends FrameLayout {

    public BoardCommentItemView(Context context) {
        super(context);
        init();
    }

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

    }

    public void setCommentItem(BoardCommentItem item) {
        profileImageView.setBackgroundResource(R.drawable.icon_profile_default);
        userNicknameView.setText(item.nickname);
        contentView.setText(item.content);
        dateView.setText(item.date);
    }
}