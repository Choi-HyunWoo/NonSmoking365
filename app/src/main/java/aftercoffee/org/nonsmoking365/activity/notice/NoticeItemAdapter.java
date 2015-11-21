package aftercoffee.org.nonsmoking365.activity.notice;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tacademy on 2015-11-11.
 */
public class NoticeItemAdapter extends BaseExpandableListAdapter {

    List<NoticeParentItem> items = new ArrayList<NoticeParentItem>();

    public void add (String date, String title, String contents, String imageUrl) {
        NoticeParentItem p = new NoticeParentItem();
        p.date = date;
        p.title = title;
        items.add(p);

        NoticeChildItem item = new NoticeChildItem();
        item.contents = contents;
        item.imageUrl = imageUrl;
        p.childItem = item;

        notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return items.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return items.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return items.get(groupPosition).childItem;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return (long)groupPosition << 32 | 0xFFFFFFFF;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return (long)groupPosition << 32 | childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        NoticeParentItemView view;
        if (convertView != null) {
            view = (NoticeParentItemView)convertView;
        } else {
            view = new NoticeParentItemView(parent.getContext());
        }
        view.setParentItem(items.get(groupPosition));
        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        NoticeChildItemView view;
        if (convertView != null) {
            view = (NoticeChildItemView)convertView;
        } else {
            view = new NoticeChildItemView(parent.getContext());
        }
        view.setChildItem(items.get(groupPosition).childItem);
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
