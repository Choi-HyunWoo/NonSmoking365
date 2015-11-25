package aftercoffee.org.nonsmoking365.activity.board.boardcontents;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tacademy on 2015-11-19.
 */
public class ContentsAdapter extends BaseAdapter implements CommentItemView.OnDeleteClickListener {

    List<CommentItem> items = new ArrayList<CommentItem>();

    public void addCommentItem(CommentItem item) {
        items.add(item);
        notifyDataSetChanged();
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    public void deleteCommentItem(String comment_id) {
        for (int i=0; i<items.size(); i++) {
            if (items.get(i)._id.equals(comment_id)) {
                items.remove(i);
                notifyDataSetChanged();
            }
        }
    }

    // Adapter > Fragment
    public interface OnAdapterDeleteListener {
        public void onAdapterDelete(ContentsAdapter adapter, View view);
    }
    OnAdapterDeleteListener mListener;
    public void setOnAdapterDeleteListener(OnAdapterDeleteListener listener) {
        mListener = listener;
    }

    // ItemView > Adapter
    @Override
    public void onDeleteClick(View view) {
        if (mListener != null) {
            mListener.onAdapterDelete(this, view);
        }
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
        CommentItemView view;
        if (convertView != null) {
            view = (CommentItemView) convertView;
        } else {
            view = new CommentItemView(parent.getContext());
        }
        view.setCommentItem(items.get(position));
        view.setOnDeleteClickListener(this);

        return view;
    }
}
