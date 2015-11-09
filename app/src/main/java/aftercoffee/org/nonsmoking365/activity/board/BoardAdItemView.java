package aftercoffee.org.nonsmoking365.activity.board;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
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
    String url;

    private void init() {
        inflate(getContext(), R.layout.view_board_ad_item, this);

        adView = (ImageView)findViewById(R.id.image_ad);
        adView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                // startActivity(intent);
            }
        });
    }

    public void setBoardItem(BoardAdItem b) {
        adView.setBackgroundResource(b.adImg);
        url = b.url;
    }

}
