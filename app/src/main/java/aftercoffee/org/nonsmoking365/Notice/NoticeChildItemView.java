package aftercoffee.org.nonsmoking365.Notice;

import android.content.Context;
import android.widget.FrameLayout;
import android.widget.TextView;

import aftercoffee.org.nonsmoking365.R;

/**
 * Created by Tacademy on 2015-11-11.
 */
public class NoticeChildItemView extends FrameLayout {
    public NoticeChildItemView(Context context) {
        super(context);
        init();
    }

    TextView contentsView;
    private void init() {
        inflate(getContext(), R.layout.view_notice_child_item, this);
        contentsView = (TextView)findViewById(R.id.text_contentsView);
    }

    public void setChildItem(NoticeChildItem item) {
        contentsView.setText(item.contents);
    }
}
