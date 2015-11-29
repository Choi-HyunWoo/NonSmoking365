package aftercoffee.org.nonsmoking365.activity.preview;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by HYUNWOO on 2015-11-29.
 */
public class PreviewPagerAdapter extends FragmentPagerAdapter {

    public static final int ITEM_COUNT = 3;

    public PreviewPagerAdapter (FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new TutorialFragment();
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
