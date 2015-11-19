package aftercoffee.org.nonsmoking365.Activity.board.boardcontents;

import android.content.Context;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import aftercoffee.org.nonsmoking365.R;

/**
 * Created by Tacademy on 2015-11-19.
 */
public class BoardContentsItemView extends FrameLayout {

    public BoardContentsItemView(Context context) {
        super(context);
        init();
    }

    ImageView contentImageView;
    TextView titleView;
    TextView contentView;
    Button likeBtn;
    Button shareBtn;

    public void init() {
        inflate(getContext(), R.layout.view_board_contents, this);

        titleView = (TextView)findViewById(R.id.text_title);
        contentView = (TextView)findViewById(R.id.text_content);
        contentImageView = (ImageView)findViewById(R.id.image_content);
        likeBtn = (Button)findViewById(R.id.btn_like);
        shareBtn = (Button)findViewById(R.id.btn_share);
    }

    public void setContentItem(BoardContentsItem item) {
        titleView.setText(item.title);
        contentView.setText(item.content);
        // contentImageView;
        likeBtn.setText("좋아요 "+item.likes);
    }
}