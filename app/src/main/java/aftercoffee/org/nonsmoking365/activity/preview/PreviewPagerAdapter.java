package aftercoffee.org.nonsmoking365.activity.preview;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import aftercoffee.org.nonsmoking365.R;

/**
 * Created by HYUNWOO on 2015-11-29.
 */
public class PreviewPagerAdapter extends FragmentPagerAdapter {

    public static final int ITEM_COUNT = 3;

    public PreviewPagerAdapter (FragmentManager fm) {
        super(fm);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view;
        switch (position) {
            case 0 :
                view = LayoutInflater.from(container.getContext()).inflate(R.layout.view_tutorial_01, container, false);
                break;
            case 1 :
                view = LayoutInflater.from(container.getContext()).inflate(R.layout.view_tutorial_02, container, false);
                break;
            case 2 :
                view = LayoutInflater.from(container.getContext()).inflate(R.layout.view_tutorial_03, container, false);
                break;
            default:
                view = LayoutInflater.from(container.getContext()).inflate(R.layout.view_tutorial_01, container, false);
                break;
        }
        container.addView(view);
        return view;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new PreviewpageFragment();
        Bundle b = new Bundle();
        b.putInt("position", position);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public int getCount() {
        return ITEM_COUNT;
    }
}
