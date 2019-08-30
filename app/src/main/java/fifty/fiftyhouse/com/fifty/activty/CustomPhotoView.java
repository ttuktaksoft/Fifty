package fifty.fiftyhouse.com.fifty.activty;

import android.content.Context;
import android.content.Intent;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.chrisbanes.photoview.PhotoView;

import java.util.ArrayList;
import java.util.Map;

import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DialogFunc;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.util.OnSingleClickListener;

public class CustomPhotoView extends AppCompatActivity {

    String ImgSrc ;
    TextView tv_Custom_Photo_Title;
    ImageView iv_Custom_Photo_Back, iv_Custom_Photo_Down;
    ViewPager vp_Custom_Photo_View;
    Context mContext;

    public static int PHOTO_VIEW_TYPE_MY_PROFILE = 1;
    public static int PHOTO_VIEW_TYPE_MY_PROFILE_LIST = 2;
    public static int PHOTO_VIEW_TYPE_USER_PROFILE = 3;
    public static int PHOTO_VIEW_TYPE_USER_PROFILE_LIST = 4;
    public static int PHOTO_VIEW_TYPE_ONE = 5;
    public static int PHOTO_VIEW_TYPE_DATAS = 6;

    ArrayList<String> mPhotoSrcList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_photo_view);

        mContext = getApplicationContext();

        tv_Custom_Photo_Title = findViewById(R.id.tv_Custom_Photo_Title);
        iv_Custom_Photo_Back = findViewById(R.id.iv_Custom_Photo_Back);
        iv_Custom_Photo_Down = findViewById(R.id.iv_Custom_Photo_Down);
        vp_Custom_Photo_View = findViewById(R.id.vp_Custom_Photo_View);

        Intent intent = getIntent(); //getIntent()로 받을준비
        int type = intent.getIntExtra("Type", 1);

        if(intent.hasExtra("ImgSrc"))
            ImgSrc  = intent.getStringExtra("ImgSrc");

        ArrayList<String> ImgList = new ArrayList<>();
        if(intent.hasExtra("datas"))
            ImgList = intent.getStringArrayListExtra("datas");

        if(type == PHOTO_VIEW_TYPE_MY_PROFILE)
        {
            iv_Custom_Photo_Down.setVisibility(View.GONE);
            mPhotoSrcList.add(TKManager.getInstance().MyData.GetUserImg(Integer.toString(0)));
            tv_Custom_Photo_Title.setText(TKManager.getInstance().MyData.GetUserNickName());
        }
        else if(type == PHOTO_VIEW_TYPE_MY_PROFILE_LIST)
        {
            iv_Custom_Photo_Down.setVisibility(View.GONE);
            Map<String, String> map = TKManager.getInstance().MyData.GetUserImg();
            for(int i = 0 ; i < map.size() ; ++i)
            {
                mPhotoSrcList.add(TKManager.getInstance().MyData.GetUserImg(Integer.toString(i)));
            }

            tv_Custom_Photo_Title.setText(TKManager.getInstance().MyData.GetUserNickName());
        }
        else if(type == PHOTO_VIEW_TYPE_USER_PROFILE)
        {
            iv_Custom_Photo_Down.setVisibility(View.GONE);
            mPhotoSrcList.add(TKManager.getInstance().TargetUserData.GetUserImg(Integer.toString(0)));
            tv_Custom_Photo_Title.setText(TKManager.getInstance().TargetUserData.GetUserNickName());
        }
        else if(type == PHOTO_VIEW_TYPE_USER_PROFILE_LIST)
        {
            iv_Custom_Photo_Down.setVisibility(View.GONE);
            Map<String, String> map = TKManager.getInstance().TargetUserData.GetUserImg();
            for(int i = 0 ; i < map.size() ; ++i)
            {
                mPhotoSrcList.add(TKManager.getInstance().TargetUserData.GetUserImg(Integer.toString(i)));
            }

            tv_Custom_Photo_Title.setText(TKManager.getInstance().TargetUserData.GetUserNickName());
        }
        else if(type == PHOTO_VIEW_TYPE_ONE)
        {
            iv_Custom_Photo_Down.setVisibility(View.VISIBLE);
            mPhotoSrcList.add(ImgSrc);
            tv_Custom_Photo_Title.setText("");
        }
        else
        {
            iv_Custom_Photo_Down.setVisibility(View.VISIBLE);
            mPhotoSrcList.addAll(ImgList);
            tv_Custom_Photo_Title.setText("");
        }


        iv_Custom_Photo_Back.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                finish();
            }
        });

        iv_Custom_Photo_Down.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {

                final DialogFunc.MsgPopupListener listenerYes = new DialogFunc.MsgPopupListener() {
                    @Override
                    public void Listener() {
                        CommonFunc.getInstance().SaveImageByGlide(mContext, mPhotoSrcList.get(vp_Custom_Photo_View.getCurrentItem()));
                        DialogFunc.getInstance().ShowToast(mContext,  CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MSG_SAVE_PIC_SUCCESS), true);
                    }
                };

                DialogFunc.getInstance().ShowMsgPopup(CustomPhotoView.this, listenerYes, null, CommonFunc.getInstance().getStr(CustomPhotoView.this.getResources(), R.string.MSG_SAVE_PIC),
                        CommonFunc.getInstance().getStr(CustomPhotoView.this.getResources(), R.string.MSG_SAVE), CommonFunc.getInstance().getStr(CustomPhotoView.this.getResources(), R.string.MSG_CANCEL));

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
