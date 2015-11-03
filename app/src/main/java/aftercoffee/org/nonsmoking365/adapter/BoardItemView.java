package aftercoffee.org.nonsmoking365.adapter;

import android.content.Context;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import aftercoffee.org.nonsmoking365.R;

/**
 * Created by Tacademy on 2015-11-03.
 */
public class BoardItemView extends FrameLayout {
    public BoardItemView(Context context) {
        super(context);
        init();
    }

    ImageView titleImageView;
    TextView titleTextView;
    TextView contentsTextView;

    public void init() {
        inflate(getContext(), R.layout.view_board_item, this);

        titleImageView = (ImageView)findViewById(R.id.image_title);
        titleTextView = (TextView)findViewById(R.id.text_title);
        contentsTextView = (TextView)findViewById(R.id.text_contents);
    }

    public void setBoardItem(BoardItem item) {
        titleImageView.setImageResource(item.imgResource);
        titleTextView.setText(item.title);
        contentsTextView.setText(item.contents);
    }
}
