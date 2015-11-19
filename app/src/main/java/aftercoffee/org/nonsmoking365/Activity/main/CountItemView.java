package aftercoffee.org.nonsmoking365.Activity.main;

import android.content.Context;
import android.widget.Button;
import android.widget.FrameLayout;

import aftercoffee.org.nonsmoking365.R;

/**
 * Created by Tacademy on 2015-11-06.
 */
public class CountItemView extends FrameLayout {
    public CountItemView(Context context) {
        super(context);
        init();
    }


    Button btn;
    private void init() {
        inflate(getContext(), R.layout.view_count_item, this);
        btn = (Button)findViewById(R.id.btn_count);
    }

    public void setCountItem(CountItem item) {
        switch (item.itemMode) {
            case CountItem.MODE_ON :
                btn.setBackgroundResource(R.drawable.btn_count_today);
                break;
            case CountItem.MODE_OFF :
                btn.setBackgroundResource(R.drawable.btn_count);
                break;
            case CountItem.MODE_O :
                btn.setBackgroundResource(R.drawable.icon_count_succes);
                break;
            case CountItem.MODE_X :
                btn.setBackgroundResource(R.drawable.icon_count_fail);
                break;
            default :
                break;
        }
    }


}
