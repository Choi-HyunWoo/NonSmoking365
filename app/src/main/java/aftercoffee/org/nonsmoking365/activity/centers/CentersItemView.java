package aftercoffee.org.nonsmoking365.activity.centers;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import aftercoffee.org.nonsmoking365.data.POI;
import aftercoffee.org.nonsmoking365.R;

/**
 * Created by HYUNWOO on 2015-11-14.
 */
public class CentersItemView extends FrameLayout{
    public CentersItemView(Context context) {
        super(context);
        init();
    }

    TextView centersNameView;
    TextView distanceView;
    ImageView mapIconView;
    ImageView dialIconView;

    private void init() {
        inflate(getContext(), R.layout.view_centers_item, this);

        centersNameView = (TextView)findViewById(R.id.text_centerName);
        distanceView = (TextView)findViewById(R.id.text_distance);
        mapIconView = (ImageView)findViewById(R.id.image_mapIcon);
        dialIconView = (ImageView)findViewById(R.id.image_dialIcon);

        mapIconView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 화면이동
            }
        });

    }
    public void setCentersItem(final POI item) {
        centersNameView.setText(item.name);
        distanceView.setText(item.distance + "km");
    }
}
