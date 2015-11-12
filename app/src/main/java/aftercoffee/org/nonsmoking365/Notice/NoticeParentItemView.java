package aftercoffee.org.nonsmoking365.Notice;

import android.content.Context;
import android.widget.FrameLayout;
import android.widget.TextView;

import aftercoffee.org.nonsmoking365.R;

/**
 * Created by Tacademy on 2015-11-11.
 */
public class NoticeParentItemView extends FrameLayout {
    public NoticeParentItemView(Context context) {
        super(context);
        init();
    }

    TextView dateView, titleView;

    private void init() {
        inflate(getContext(), R.layout.view_notice_parent_item, this);
        dateView = (TextView)findViewById(R.id.text_dateView);
        titleView = (TextView)findViewById(R.id.text_titleView);
    }

    public void setParentItem (NoticeParentItem item) {
        dateView.setText(item.date);
        titleView.setText(item.title);
    }
}
