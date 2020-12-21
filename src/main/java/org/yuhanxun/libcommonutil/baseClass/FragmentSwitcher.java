package org.yuhanxun.libcommonutil.baseClass;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * Created by yuhanxun on 15-4-27.
 */
public class FragmentSwitcher {
    private Fragment mCurrentFragment;

    public Fragment getmCurrentFragment() {
        return mCurrentFragment;
    }

    private Fragment mToFragment;
    FragmentManager mFragmentManager;
    FragmentFactory fragmentFactory;
    public FragmentSwitcher(FragmentManager mFragmentManager, FragmentFactory fragmentFactory) {
        this.mFragmentManager = mFragmentManager;
        this.fragmentFactory = fragmentFactory;
    }

    public interface FragmentFactory {
        Fragment getFragmentByTag(String tag, String arg);
    }
    public void switchTabPage(String fragmentTag, int containerID, boolean forceRefrush, String arg) {
        mToFragment = mFragmentManager.findFragmentByTag(fragmentTag);

        if (mToFragment != null && forceRefrush) {
//            if (mToFragment.isVisible()) {
                FragmentTransaction ft = mFragmentManager.beginTransaction();
                ft.remove(mToFragment);
                ft.commit();
                mToFragment = null;
//            }
        }
        if (mToFragment == null) {
            if(fragmentFactory == null) {
                throw new NullPointerException("please set fragmentfactory");
            }
            mToFragment = fragmentFactory.getFragmentByTag(fragmentTag, arg);
            if (mToFragment == null) {
                throw new IllegalArgumentException("Get NullPoint from FragmentFactory");
            }
            FragmentTransaction ft = mFragmentManager.beginTransaction();
            ft.add(containerID, mToFragment, fragmentTag);
            if (mCurrentFragment != null) {
                ft.hide(mCurrentFragment);
                mCurrentFragment.setUserVisibleHint(false);
            }
            try {
                ft.commit();
            }catch (java.lang.IllegalStateException e) {
                e.printStackTrace();
                return;
            }
            mCurrentFragment = mToFragment;
            mCurrentFragment.setUserVisibleHint(true);
        } else {
            if (mCurrentFragment == mToFragment) {
                return;
            }
            if (!mToFragment.isAdded()) {
                FragmentTransaction ft = mFragmentManager.beginTransaction();
                ft.add(containerID, mToFragment);
                if (mCurrentFragment != null) {
                    ft.hide(mCurrentFragment);
                    mCurrentFragment.setUserVisibleHint(false);
                }
                ft.commit();
                mCurrentFragment = mToFragment;
                mCurrentFragment.setUserVisibleHint(true);
            } else {
                FragmentTransaction ft = mFragmentManager.beginTransaction();
                if (mCurrentFragment != null) {
                    ft.hide(mCurrentFragment);
                    mCurrentFragment.setUserVisibleHint(false);
                }
                ft.show(mToFragment);
                ft.commit();
                mCurrentFragment = mToFragment;
                mCurrentFragment.setUserVisibleHint(true);
            }
        }
    }
}
