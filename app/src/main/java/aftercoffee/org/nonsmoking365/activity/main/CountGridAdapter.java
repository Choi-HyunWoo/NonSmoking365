package aftercoffee.org.nonsmoking365.activity.main;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * Created by Tacademy on 2015-11-06.
 */
public class CountGridAdapter extends BaseAdapter {
    ArrayList<CountItem> items = new ArrayList<>();

    public void add (int itemMode) {
        CountItem c = new CountItem();
        c.itemMode = itemMode;
        items.add(c);
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
    public View getView(int position, View convertView, ViewGroup parent) {
        CountItemView view;
        if (convertView != null) {
            view = (CountItemView) convertView;
        } else {
            view = new CountItemView(parent.getContext());
        }
        view.setCountItem(items.get(position));
        return view;
    }
}
