package fifty.fiftyhouse.com.fifty.viewPager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

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

public class PhotoViewPager extends ViewPager {

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
