package org.yuhanxun.libcommonutil.baseClass;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by yuhanxun on 15/9/17.
 */
public class FragViewPageAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments;

    public FragViewPageAdapter(FragmentManager fm) {
        super(fm);
    }

    public FragViewPageAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    /**
     * add the fragment to the special position
     *
     * @param location the position be added to.
     * @param fragment
     */
    public void addFragment(int location, Fragment fragment) {
        this.fragments.add(location, fragment);
        this.notifyDataSetChanged();
    }

    /**
     * add the fragment to the default position.the end of the list.
     *
     * @param fragment
     */
    public void addFragment(Fragment fragment) {
        this.fragments.add(fragment);
        this.notifyDataSetChanged();
    }
}