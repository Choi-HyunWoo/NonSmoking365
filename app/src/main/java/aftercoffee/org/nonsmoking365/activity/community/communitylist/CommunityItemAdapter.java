package aftercoffee.org.nonsmoking365.activity.community.communitylist;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tacademy on 2015-11-24.
 */
public class CommunityItemAdapter extends BaseAdapter {
    List<CommunityItem> items = new ArrayList<CommunityItem>();

    public void add(CommunityItem item) {
        items.add(item);
        notifyDataSetChanged();
    }
    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }
    int totalCount;
    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
    public int getTotalCount() {
        return totalCount;
    }
    public int getStartIndex() {
        if (items.size() < totalCount) {
            return items.size() + 1;
        }
        return -1;
    }
    public String getDocID(int position) {
        return items.get(position)._id;
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
        CommunityItemView view;
        if (convertView != null) {
            view = (CommunityItemView)convertView;
        } else {
            view = new CommunityItemView(parent.getContext());
        }
        view.setCommunityItem(items.get(position));
        return view;
    }
}
