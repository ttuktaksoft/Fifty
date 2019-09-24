package fifty.fiftyhouse.com.fifty.activty;

import android.content.Context;
import androidx.annotation.NonNull;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.util.OnSingleClickListener;
import fifty.fiftyhouse.com.fifty.viewPager.FriendListViewPager;
import fifty.fiftyhouse.com.fifty.viewPager.FriendRequestListViewPager;

public class FriendListActivity extends AppCompatActivity {

    ViewPager vp_FriendList;

    View ui_FriendList_TopBar;
    TextView tv_TopBar_Title;
    ImageView iv_TopBar_Back;

    TabLayout tl_FriendList_TopTab;

    FriendListViewPager mFriendListViewPager = null;
    FriendRequestListViewPager mFriendRequestListViewPager = null;

    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

        mContext = getApplicationContext();

        ui_FriendList_TopBar = findViewById(R.id.ui_FriendList_TopBar);
        tv_TopBar_Title = ui_FriendList_TopBar.findViewById(R.id.tv_TopBar_Title);
        iv_TopBar_Back = ui_FriendList_TopBar.findViewById(R.id.iv_TopBar_Back);
        vp_FriendList = findViewById(R.id.vp_FriendList);
        tl_FriendList_TopTab = findViewById(R.id.tl_FriendList_TopTab);

        tv_TopBar_Title.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.TITLE_USER_LIST_FRIEND));
        iv_TopBar_Back.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                finish();
            }
        });


        tl_FriendList_TopTab.addTab(tl_FriendList_TopTab.newTab().setText(getResources().getString(R.string.MSG_TAB_FRIEND_MY)));
        tl_FriendList_TopTab.addTab(tl_FriendList_TopTab.newTab().setText(getResources().getString(R.string.MSG_TAB_FRIEND_REQUEST)));
        tl_FriendList_TopTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vp_FriendList.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        vp_FriendList.setOffscreenPageLimit(3);
        vp_FriendList.setAdapter(new TabPagerAdapter(getSupportFragmentManager(), 2));
        vp_FriendList.setCurrentItem(0);
        vp_FriendList.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tl_FriendList_TopTab));
    }

    private class TabPagerAdapter extends FragmentStatePagerAdapter {
        private  int tabCount;
        public TabPagerAdapter(FragmentManager fm, int tabCount) {
            super(fm);
            this.tabCount =tabCount;
        }

        @Override
        public Fragment getItem(int position) {
            switch(position){
                case 0:
                    mFriendListViewPager = new FriendListViewPager();
                    return mFriendListViewPager;
                case 1:
                    mFriendRequestListViewPager = new FriendRequestListViewPager();
                    return mFriendRequestListViewPager;
            }

            return null;
        }

        @Override
        public int getCount() {
            return tabCount;
        }
    }

    /*private class TabPagerAdapter extends PagerAdapter {
        private  int tabCount;
        // Context를 전달받아 mContext에 저장하는 생성자 추가.
        public TabPagerAdapter(Context context, int tabCount) {
            mContext = context ;
            this.tabCount = tabCount;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = null ;

            if (mContext != null) {
                switch(position){
                    case 0:
                        mFriendListViewPager = new FriendListViewPager();
                        view = mFriendListViewPager.v_FragmentView;
                        break;
                    case 1:
                        mFriendRequestListViewPager = new FriendRequestListViewPager();
                        view = mFriendRequestListViewPager.v_FragmentView;
                        break;
                }
            }

            // 뷰페이저에 추가.
            container.addView(view) ;

            return view ;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // 뷰페이저에서 삭제.
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            // 전체 페이지 수는 10개로 고정.
            return this.tabCount;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return (view == (View)object);
        }
    }*/
}
