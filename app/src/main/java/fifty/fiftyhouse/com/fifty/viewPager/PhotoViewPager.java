package fifty.fiftyhouse.com.fifty.viewPager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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

public class PhotoViewPager extends Fragment {

    PhotoView pv_Photo_View;

    View v_FragmentView = null;
    String mImgSrc = "";
    boolean mMyProfilePhoto = false;

    public PhotoViewPager() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v_FragmentView = inflater.inflate(R.layout.viewpager_photo_view, container, false);

        pv_Photo_View = v_FragmentView.findViewById(R.id.pv_Photo_View);
        CommonFunc.getInstance().DrawImageByGlide(getContext(), pv_Photo_View, mImgSrc, false);


        return v_FragmentView;
    }

    public void setMyProfilePhoto(boolean enable)
    {
        mMyProfilePhoto = enable;
    }

    public void setImgSrc(String src)
    {
        mImgSrc = src;
    }
}
