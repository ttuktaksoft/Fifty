package fifty.fiftyhouse.com.fifty.activty;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

    String ImgSrc ;
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
        ImgSrc  = intent.getStringExtra("ImgSrc");
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

        vp_Custom_Photo_View.setAdapter(new PhotoPagerAdapter());
        vp_Custom_Photo_View.setCurrentItem(0);

    }

    //     photoView.setImageResource(R.drawable.image4);
    private class PhotoPagerAdapter extends PagerAdapter {
        PhotoView pv_Photo_View;
        LayoutInflater mLayoutInflater;
        public PhotoPagerAdapter() {
            mLayoutInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return mPhotoSrcList.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View itemView = mLayoutInflater.inflate(R.layout.viewpager_photo_view, container,false);
            pv_Photo_View = itemView.findViewById(R.id.pv_Photo_View);
            CommonFunc.getInstance().DrawImageByGlide(mContext, pv_Photo_View, mPhotoSrcList.get(position), false);
            container.addView(itemView);
            return itemView;

        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((ConstraintLayout)object);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((ConstraintLayout)object);
        }
    }
}
