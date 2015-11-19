package aftercoffee.org.nonsmoking365.Activity.board;

import android.content.Context;
import android.widget.FrameLayout;
import android.widget.ImageView;

import aftercoffee.org.nonsmoking365.R;

/**
 * Created by Tacademy on 2015-11-09.
 */
public class BoardAdItemView extends FrameLayout {
    public BoardAdItemView(Context context) {
        super(context);
        init();
    }

    ImageView adView;

    private void init() {
        inflate(getContext(), R.layout.view_board_ad_item, this);

        adView = (ImageView)findViewById(R.id.image_ad);
    }

    public void setBoardItem(BoardAdItem b) {
        adView.setBackgroundResource(b.adImg);
    }

}
