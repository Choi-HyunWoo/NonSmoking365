package aftercoffee.org.nonsmoking365.main;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import aftercoffee.org.nonsmoking365.Manager.PropertyManager;

/**
 * Created by Tacademy on 2015-11-06.
 */
public class CountGridAdapter extends BaseAdapter {
    public static final int COUNT_ITEMS_START_POSITION = 0;
    public static final int COUNT_ITEMS_MAX_POSITION = 29;
    ArrayList<CountItem> items = new ArrayList<>();

    // get saved CountItemMode and add to list
    public void initCount() {
        items.clear();
        for (int i=0; i<30; i++) {
            CountItem c = new CountItem();
            c.itemMode = PropertyManager.getInstance().getCountItemMode(i);
            items.add(c);
        }
        notifyDataSetChanged();
    }

    public void setModeO(int position) {
        PropertyManager.getInstance().setCountItemMode(position, CountItem.MODE_O);
        initCount();
    }

    public void setModeX(int position) {
        PropertyManager.getInstance().setCountItemMode(position, CountItem.MODE_X);
        initCount();
    }

    public void setModeOn(int position) {
        PropertyManager.getInstance().setCountItemMode(position, CountItem.MODE_ON);
        initCount();
    }

    public boolean isModeOff(int position) {
        if (PropertyManager.getInstance().getCountItemMode(position) == CountItem.MODE_OFF)
            return true;
        else
            return false;
    }

    public void nonSmokingCountReset() {
        PropertyManager.getInstance().setCountItemMode(0, CountItem.MODE_ON);
        for (int i=1; i<30; i++) {
            PropertyManager.getInstance().setCountItemMode(i, CountItem.MODE_OFF);
        }
        initCount();
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
