package aftercoffee.org.nonsmoking365.activity.main;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import aftercoffee.org.nonsmoking365.PropertyManager;

/**
 * Created by Tacademy on 2015-11-06.
 */
public class CountGridAdapter extends BaseAdapter {
    public static final int COUNT_ITEMS_START_POSITION = 0;
    public static final int COUNT_ITEMS_MAX_POSITION = 29;
    ArrayList<CountItem> items = new ArrayList<>();
    Long countStartTime;

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

    public void nonSmokingSuccess(int position) {
        if (position == COUNT_ITEMS_START_POSITION) {
            countStartTime = System.currentTimeMillis();
            PropertyManager.getInstance().setCountStartTime(countStartTime);
        }
        PropertyManager.getInstance().setCountItemMode(position,CountItem.MODE_O);
        initCount();
    }

    public void nonSmokingFailed (int position) {
        if (position == COUNT_ITEMS_START_POSITION) {
            countStartTime = System.currentTimeMillis();
            PropertyManager.getInstance().setCountStartTime(countStartTime);
        }
        PropertyManager.getInstance().setCountItemMode(position,CountItem.MODE_X);
        initCount();
    }

    public void nonSmokingCountReset() {
        countStartTime = 0L;
        PropertyManager.getInstance().setCountStartTime(countStartTime);
        PropertyManager.getInstance().setCountItemReset();
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
