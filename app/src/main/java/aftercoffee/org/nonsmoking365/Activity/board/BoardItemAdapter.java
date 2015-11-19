package aftercoffee.org.nonsmoking365.Activity.board;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tacademy on 2015-11-03.
 */
public class BoardItemAdapter extends BaseAdapter {
    List<BoardItem> items  = new ArrayList<BoardItem>();

    public static final int VIEW_TYPE_COUNT = 3;
    public static final int TYPE_INDEX_WARNING = 1;
    public static final int TYPE_INDEX_TIPS = 2;
    public static final int TYPE_INDEX_AD = 3;


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
        switch (getItemViewType(position)) {
            case TYPE_INDEX_WARNING : {
                BoardWarningItem b = (BoardWarningItem) items.get(position);
                return b._id;
            }
            case TYPE_INDEX_TIPS : {
                BoardTipsItem b = (BoardTipsItem) items.get(position);
                return b._id;
            }
            case TYPE_INDEX_AD : {
                BoardAdItem b = (BoardAdItem) items.get(position);
                return b._id;
            }
            default:
                return null;
        }
    }

    public void add(BoardItem item) {
        items.add(item);
        notifyDataSetChanged();
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        BoardItem d = items.get(position);
        if (d instanceof BoardWarningItem) {
            return TYPE_INDEX_WARNING;
        } else if (d instanceof BoardTipsItem) {
            return TYPE_INDEX_TIPS;
        } else {
            return TYPE_INDEX_AD;
        }
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public BoardItem getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        switch (getItemViewType(position)) {
            case TYPE_INDEX_WARNING: {
                BoardWarningItemView view;
                if (convertView != null && convertView instanceof BoardWarningItemView) {
                    view = (BoardWarningItemView) convertView;
                } else {
                    view = new BoardWarningItemView(parent.getContext());
                }
                view.setBoardItem((BoardWarningItem) items.get(position));
                return view;
            }
            case TYPE_INDEX_TIPS: {
                BoardTipsItemView view;
                if (convertView != null && convertView instanceof BoardTipsItemView) {
                    view = (BoardTipsItemView) convertView;
                } else {
                    view = new BoardTipsItemView(parent.getContext());
                }
                view.setBoardItem((BoardTipsItem) items.get(position));
                return view;
            }
            case TYPE_INDEX_AD : {
                BoardAdItemView view;
                if (convertView != null && convertView instanceof BoardAdItemView) {
                    view = (BoardAdItemView) convertView;
                } else {
                    view = new BoardAdItemView(parent.getContext());
                }
                view.setBoardItem((BoardAdItem) items.get(position));
                return view;
            }


        }
        return null;
    }
}
