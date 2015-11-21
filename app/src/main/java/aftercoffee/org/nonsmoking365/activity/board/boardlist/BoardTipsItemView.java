package aftercoffee.org.nonsmoking365.activity.board.boardlist;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import aftercoffee.org.nonsmoking365.R;

/**
 * Created by Tacademy on 2015-11-09.
 */
public class BoardTipsItemView extends FrameLayout {
    public BoardTipsItemView(Context context) {
        super(context);
        init();
    }

    ImageView titleImageView;
    TextView titleTextView;
    TextView contentsTextView;
    Button likeBtn;
    Button commentsBtn;
    Button shareBtn;

    public void init() {
        inflate(getContext(), R.layout.view_board_tips_item, this);

        titleImageView = (ImageView)findViewById(R.id.image_title);
        titleTextView = (TextView)findViewById(R.id.text_title);
        contentsTextView = (TextView)findViewById(R.id.text_contents);

        likeBtn = (Button)findViewById(R.id.btn_like);
        commentsBtn = (Button)findViewById(R.id.btn_comments);
        shareBtn = (Button)findViewById(R.id.btn_share);

        commentsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // comment fragment로?
            }
        });
    }

    public void setBoardItem(BoardTipsItem item) {
        titleImageView.setBackgroundResource(item.titleImg);
        titleTextView.setText(item.title);
        contentsTextView.setText(item.contents);
    }
}
