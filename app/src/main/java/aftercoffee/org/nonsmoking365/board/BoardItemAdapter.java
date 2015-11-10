package aftercoffee.org.nonsmoking365.board;

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

    private static final int VIEW_TYPE_COUNT = 3;
    private static final int TYPE_INDEX_WARNING = 1;
    private static final int TYPE_INDEX_TIPS = 2;
    private static final int TYPE_INDEX_AD = 3;

    public void add(BoardItem item) {
        items.add(item);
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
