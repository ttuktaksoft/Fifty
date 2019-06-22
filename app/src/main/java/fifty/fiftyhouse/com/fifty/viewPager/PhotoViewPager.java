package fifty.fiftyhouse.com.fifty.viewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.chrisbanes.photoview.PhotoView;

import fifty.fiftyhouse.com.fifty.CommonData;
import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.activty.UserProfileActivity;
import fifty.fiftyhouse.com.fifty.adapter.MainAdapter;
import fifty.fiftyhouse.com.fifty.util.RecyclerItemClickListener;

/*String ImgSrc ;
@Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_photo_view);
        Intent intent = getIntent(); //getIntent()로 받을준비
        ImgSrc  = intent.getStringExtra("ImgSrc");

        PhotoView photoView = findViewById(R.id.photoView);
        //     photoView.setImageResource(R.drawable.image4);

        CommonFunc.getInstance().DrawImageByGlide(getApplicationContext(), photoView, ImgSrc, false);

        }*/

public class PhotoViewPager extends android.support.v4.view.ViewPager {

    String TAG = "PhotoViewPager";
    public PhotoViewPager(Context context, AttributeSet attrs) {

        super(context, attrs);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        try {
            return super.onTouchEvent(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        try{
            return super.onInterceptTouchEvent(event);

        }catch(Exception e){

            e.printStackTrace();

        }

        return false;
    }
}
