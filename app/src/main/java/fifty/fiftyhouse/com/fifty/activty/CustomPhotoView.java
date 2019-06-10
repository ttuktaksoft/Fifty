package fifty.fiftyhouse.com.fifty.activty;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;

import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;

public class CustomPhotoView extends AppCompatActivity {

    String ImgSrc ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_photo_view);
        Intent intent = getIntent(); //getIntent()로 받을준비
        ImgSrc  = intent.getStringExtra("ImgSrc");

        PhotoView photoView = findViewById(R.id.photoView);
   //     photoView.setImageResource(R.drawable.image4);

        CommonFunc.getInstance().DrawImageByGlide(getApplicationContext(), photoView, ImgSrc, false);

    }
}
