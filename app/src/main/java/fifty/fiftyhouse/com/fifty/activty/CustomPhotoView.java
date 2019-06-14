package fifty.fiftyhouse.com.fifty.activty;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.ArrayList;
import java.util.Map;

import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.viewPager.PhotoViewPager;

public class CustomPhotoView extends AppCompatActivity {

    View ui_Custom_Photo_TopBar;
    TextView tv_TopBar_Title;
    ImageView iv_TopBar_Back;
    ViewPager vp_Custom_Photo_View;
    Context mContext;

    public static int PHOTO_VIEW_TYPE_MY_PROFILE = 1;
    public static int PHOTO_VIEW_TYPE_MY_PROFILE_LIST = 2;
    public static int PHOTO_VIEW_TYPE_USER_PROFILE = 3;
    public static int PHOTO_VIEW_TYPE_USER_PROFILE_LIST = 4;

    ArrayList<String> mPhotoSrcList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_photo_view);

        mContext = getApplicationContext();
        ui_Custom_Photo_TopBar = findViewById(R.id.ui_Custom_Photo_TopBar);
        tv_TopBar_Title = ui_Custom_Photo_TopBar.findViewById(R.id.tv_TopBar_Title);
        iv_TopBar_Back = ui_Custom_Photo_TopBar.findViewById(R.id.iv_TopBar_Back);
        vp_Custom_Photo_View = findViewById(R.id.vp_Custom_Photo_View);

        Intent intent = getIntent(); //getIntent()로 받을준비
        int type = intent.getIntExtra("Type", 1);

        if(type == PHOTO_VIEW_TYPE_MY_PROFILE)
        {
            mPhotoSrcList.add(TKManager.getInstance().MyData.GetUserImg(Integer.toString(0)));
            tv_TopBar_Title.setText(TKManager.getInstance().MyData.GetUserNickName());
        }
        else if(type == PHOTO_VIEW_TYPE_MY_PROFILE_LIST)
        {
            Map<String, String> map = TKManager.getInstance().MyData.GetUserImg();
            for(int i = 0 ; i < map.size() ; ++i)
            {
                mPhotoSrcList.add(TKManager.getInstance().MyData.GetUserImg(Integer.toString(i)));
            }

            tv_TopBar_Title.setText(TKManager.getInstance().MyData.GetUserNickName());
        }
        else if(type == PHOTO_VIEW_TYPE_USER_PROFILE)
        {
            mPhotoSrcList.add(TKManager.getInstance().TargetUserData.GetUserImg(Integer.toString(0)));
            tv_TopBar_Title.setText(TKManager.getInstance().TargetUserData.GetUserNickName());
        }
        else if(type == PHOTO_VIEW_TYPE_USER_PROFILE_LIST)
        {
            Map<String, String> map = TKManager.getInstance().TargetUserData.GetUserImg();
            for(int i = 0 ; i < map.size() ; ++i)
            {
                mPhotoSrcList.add(TKManager.getInstance().TargetUserData.GetUserImg(Integer.toString(i)));
            }

            tv_TopBar_Title.setText(TKManager.getInstance().TargetUserData.GetUserNickName());
        }


        iv_TopBar_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        vp_Custom_Photo_View.setAdapter(new PhotoPagerAdapter(getSupportFragmentManager(), mPhotoSrcList.size()));
        vp_Custom_Photo_View.setCurrentItem(0);

    }

    private class PhotoPagerAdapter extends FragmentStatePagerAdapter {
        private  int tabCount;
        public PhotoPagerAdapter(FragmentManager fm, int tabCount) {
            super(fm);
            this.tabCount = tabCount;
        }

        @Override
        public Fragment getItem(int position) {
            PhotoViewPager PhotoPage = new PhotoViewPager();
            PhotoPage.setMyProfilePhoto(true);
            PhotoPage.setImgSrc(mPhotoSrcList.get(position));
            return PhotoPage;
        }

        @Override
        public int getCount() {
            return tabCount;
        }
    }
}
