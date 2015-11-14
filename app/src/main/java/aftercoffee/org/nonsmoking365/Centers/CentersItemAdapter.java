package aftercoffee.org.nonsmoking365.Centers;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HYUNWOO on 2015-11-14.
 */
public class CentersItemAdapter extends BaseAdapter {

    List<POI> items = new ArrayList<POI>();

    public void add(POI item) {
        items.add(item);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        CentersItemView view;
        if (convertView != null) {
            view = (CentersItemView) convertView;
        } else {
            view = new CentersItemView(parent.getContext());
        }
        view.setCentersItem(items.get(position));
        view.mapIconView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                POI item = (POI)getItem(position);
                ((CentersActivity)parent.getContext()).pushMapFragment(item.getLatitude(), item.getLongitude());
            }
        });
        view.dialIconView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("tel:"+((POI)getItem(position)).telNo);
                Intent intent = new Intent(Intent.ACTION_DIAL, uri);
                parent.getContext().startActivity(intent);
            }
        });
        return view;
    }
}
