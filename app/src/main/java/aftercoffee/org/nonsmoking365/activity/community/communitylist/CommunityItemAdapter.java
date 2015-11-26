package aftercoffee.org.nonsmoking365.activity.community.communitylist;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tacademy on 2015-11-24.
 */
public class CommunityItemAdapter extends BaseAdapter implements CommunityItemView.OnCommunityBtnClickListener {
    List<CommunityItem> items = new ArrayList<CommunityItem>();

    public interface OnAdapterBtnClickListener {
        public void onAdapterLikeClick(CommunityItemView view, CommunityItem item);
        public void onAdapterCommentClick(CommunityItemView view, CommunityItem item);
    }
    OnAdapterBtnClickListener mListener;
    public void setOnAdapterBtnClickListener(OnAdapterBtnClickListener listener) {
        mListener = listener;
    }

    @Override
    public void onCommunityLikeClick(CommunityItemView view, CommunityItem item) {
        mListener.onAdapterLikeClick(view, item);
    }
    @Override
    public void onCommunityCommentClick(CommunityItemView view, CommunityItem item) {
        mListener.onAdapterCommentClick(view, item);
    }

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
        view.setOnCommunityBtnClickListener(this);
        return view;
    }
}
