package aftercoffee.org.nonsmoking365.activity.board;

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

    public void add(int imgResource, String title, String contents) {
        BoardItem b = null;
        for (BoardItem item : items) {
            if (item.title.equals(title)) {
                b = item;
                break;
            }
        }
        if (b == null) {
            b = new BoardItem();
            b.title = title;
            b.contents = contents;
            b.imgResource = imgResource;
            items.add(b);
        }
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
        BoardItemView view;
        if (convertView != null) {
            view = (BoardItemView) convertView;
        } else {
            view = new BoardItemView(parent.getContext());
        }
        // data setting
        view.setBoardItem(items.get(position));
        return view;
    }
}
