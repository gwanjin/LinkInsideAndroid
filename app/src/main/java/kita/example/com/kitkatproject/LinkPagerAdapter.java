package kita.example.com.kitkatproject;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/**
 * Created by kita on 2017-01-10.
 */

public class LinkPagerAdapter extends FragmentPagerAdapter {
    private static int PAGE_NUMBER = 3;


    public LinkPagerAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                return FirstPageFragment.newInstance();
            case 1:
                return SecondPageFragment.newInstance();
            case 2:
                return ThirdPageFragment.newInstance();
            default:
                return null;
        }
    }


    @Override
    public int getCount() {
        return PAGE_NUMBER;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch(position){
            case 0:
                return "Matched List";
            case 1:
                return "Applied List";
            case 2:
                return "Register Calendar";
            default:
                return null;
        }
    }
}
