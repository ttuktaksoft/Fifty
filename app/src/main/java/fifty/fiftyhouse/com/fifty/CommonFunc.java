package fifty.fiftyhouse.com.fifty;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import fifty.fiftyhouse.com.fifty.DataBase.ChatData;
import fifty.fiftyhouse.com.fifty.DataBase.ClubContextData;
import fifty.fiftyhouse.com.fifty.DataBase.UserData;
import fifty.fiftyhouse.com.fifty.Manager.FirebaseManager;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.activty.AuthActivity;
import fifty.fiftyhouse.com.fifty.activty.LoginActivity;
import fifty.fiftyhouse.com.fifty.activty.SignUpActivity;
import fifty.fiftyhouse.com.fifty.activty.UserProfileActivity;
import fifty.fiftyhouse.com.fifty.adapter.CustomGridListHolder;
import gun0912.tedbottompicker.TedBottomPicker;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static fifty.fiftyhouse.com.fifty.CommonData.MOBILE_STATE;
import static fifty.fiftyhouse.com.fifty.CommonData.NONE_STATE;
import static fifty.fiftyhouse.com.fifty.CommonData.WIFI_STATE;

public class CommonFunc {

    private static CommonFunc _Instance;
    public static CommonFunc getInstance() {
        if (_Instance == null)
            _Instance = new CommonFunc();

        return _Instance;
    }
    private CommonFunc() {
    }

    private int mWidth;
    private  int mHeight;
    public int MainUserListSlotCount = 3;

    public Activity mCurActivity;

    public void GoMainActivity(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        intent.setFlags(Intent. FLAG_ACTIVITY_SINGLE_TOP);
        activity.startActivity(intent);
        activity.finish();
    }


    public int getWidthByDevice()
    {
        return mWidth;
    }

    public void setWidthByDevice(int width)
    {
        mWidth = width;
    }

    public int getHeightByDevice()
    {
        return mHeight;
    }

    public void setHeightByDevice(int height)
    {
        mHeight = height;
    }

    public int convertPXtoDP(Resources res, int pixel)
    {
        float density = res.getDisplayMetrics().density;
        if(density == 1.0)
            density *= 4.0;
        else if(density == 1.5)
            density *= (8/3);
        else if(density == 2.0)
            density *= 2.0;
        return Math.round(pixel / density);
    }

    public int convertDPtoPX(Resources res, int dp)
    {
        int px = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, res.getDisplayMetrics());
        return px;
    }


    public String getStr(Resources res, int id)
    {
        if(res == null)
            return "ERROR";

        return res.getString(id);
    }

    public String[] ConvertTimeToHM(String time)
    {
        String[] rtValue = new String[2];

        String tempMsgDate = time;

        String tempDate = tempMsgDate.substring(0, 7);
        String tempTime = tempMsgDate.substring(8, 12);

        String tempHour = tempTime.substring(0, 2);
        int tempHourInteger = Integer.parseInt(tempHour);

        String tempMinute = tempTime.substring(2, 4);
        if(Integer.parseInt(tempHour) > 12) {
            tempHour = Integer.toString(tempHourInteger - 12);
            rtValue[0] = tempHour;
            rtValue[0] = tempMinute;
        }
        else
        {
            rtValue[0] = tempHour;
            rtValue[0] = tempMinute;
        }

        return  rtValue;
    }

    public String GetCurrentDate()
    {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String getTime = sdf.format(date);
        return getTime;
    }

    public String GetCurrentTime()
    {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String getTime = sdf.format(date);
        return getTime;
    }


    int i = 0;
    public void MoveActivity(Activity from, Class  to)
    {
        i++;

        if(i == 22)
        {
            CommonFunc.getInstance().mCurActivity = from;
            from.startActivity(new Intent(from, to));
            from.finish();
        }
    }


    public void MoveUserActivity(Activity from)
    {
        from.startActivity(new Intent(from, UserProfileActivity.class));
        from.finish();
    }

    public void DrawImageByGlide(Context context, ImageView view, int src, boolean circle)
    {
        if(circle)
        {
            Glide.with(context).load(src)
                    .circleCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(view);
        }
        else
            Glide.with(context).load(src)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(view);


    }

    public void DrawImageByGlide(Context context, ImageView view, String src, boolean circle)
    {
        if(circle)
        {
            Glide.with(context).load(src)
                    .circleCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(view);
        }
        else
            Glide.with(context).load(src)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(view);


    }

    public void DrawImageByGlide(Context context, ImageView view, Bitmap bmp, boolean circle)
    {


        if(circle)
        {
            Glide.with(context).load(bmp)
                    .circleCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.bg_empty_circle)
                    .into(view);
        }
        else
            Glide.with(context).load(bmp)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.bg_empty_square)
                    .into(view);
    }

    public void SaveImageByGlide(final Context context, String src)
    {
        try{

            Glide.with(context)
                    .asBitmap().load(src)
                    .apply(new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.ALL))
                    .listener(new RequestListener<Bitmap>(){

                                  @Override
                                  public boolean onLoadFailed(GlideException e, Object
                                          o, Target<Bitmap> target, boolean b){

                                      return false;
                                  }

                                  @Override
                                  public boolean onResourceReady(Bitmap bitmap, Object o, Target<Bitmap>
                                          target, DataSource dataSource, boolean b){

                                      saveImage(context, bitmap);   // save your bitmap

                                      return false;
                                  }
                              }
                    ).submit();


        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private String saveImage(final Context context, Bitmap image) {
        String savedImagePath = null;

        String imageFileName = "FIFTY_" + System.currentTimeMillis() + ".jpg";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                + "/FIFTY");
        boolean success = true;
        if (!storageDir.exists()) {
            success = storageDir.mkdirs();
        }
        if (success) {
            File imageFile = new File(storageDir, imageFileName);
            savedImagePath = imageFile.getAbsolutePath();
            try {
                OutputStream fOut = new FileOutputStream(imageFile);
                image.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                fOut.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Add the image to the system gallery
            galleryAddPic(context, savedImagePath);
        }
        return savedImagePath;
    }

    private void galleryAddPic(final Context context, String imagePath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(imagePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }

    public void saveVideo(Context context, String cacheFile)
    {
        int first = cacheFile.lastIndexOf("/");
        int end = cacheFile.lastIndexOf(".");
        String tempName = cacheFile.substring(first+1, end);
        String imageFileName = tempName + ".mp4";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES)
                + "/FIFTY");
        File saveFile = new File(storageDir, imageFileName);

        boolean success = true;
        if (!storageDir.exists()) {
            success = storageDir.mkdirs();
        }
        if (success) {
            try {
                File inFile = new File(cacheFile);

                InputStream in = new FileInputStream(inFile);
                OutputStream save = new FileOutputStream(saveFile);

                // Copy the bits from instream to outstream
                byte[] buf = new byte[1024];
                int len;

                while ((len = in.read(buf)) > 0) {
                    save.write(buf, 0, len);
                }

                in.close();
                save.flush();
                save.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(saveFile.getAbsoluteFile());
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }

    public boolean IsGalleryVideo(String name)
    {
        File files = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES)
                + "/FIFTY/" + name + ".mp4");
        if(files.exists()==true) {
            return true;
        } else {
            return false;
        }
    }

    public void setEditTextMaxSize(EditText et, int size)
    {
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(size);
        et.setFilters(FilterArray);
    }

    public static boolean isEmpty(Object s) {
        if (s == null) {
            return true;
        }
        if ((s instanceof String) && (((String)s).trim().length() == 0)) {
            return true;
        }
        if (s instanceof Map) {
            return ((Map<?, ?>)s).isEmpty();
        }
        if (s instanceof List) {
            return ((List<?>)s).isEmpty();
        }
        if (s instanceof Object[]) {
            return (((Object[])s).length == 0);
        }
        return false;
    }

    public boolean CheckStringNull(String str)
    {
        if(TextUtils.isEmpty(str))
        {
            return true;
        }
        else
            return false;
    }

    public interface PhotoSelectListener {
        void Listener(List<Uri> list);
    }

    private void GetPhotoInGallery(FragmentActivity activity, Context context, Fragment fragment, final PhotoSelectListener multiSelectListener, boolean oneSelectCrop) {

        if(multiSelectListener == null && context != null && fragment != null)
        {
            TedBottomPicker.with(activity)
                    .setPeekHeight(activity.getResources().getDisplayMetrics().heightPixels/2)
                    .showTitle(false)
                    .showVideoMedia()
                    .setPeekHeight(activity.getResources().getDisplayMetrics().heightPixels/2)
                    .show(uri -> {
                        CropImage.activity(uri)
                                .setActivityTitle(getStr(activity.getResources(), R.string.MSG_PHOTO_SELECT))
                                .setGuidelines(CropImageView.Guidelines.ON)
                                .setInitialCropWindowPaddingRatio(0)
                                .start(context, fragment);
                    });
        }
        else
        {
            TedBottomPicker.with(activity)
                    .setPeekHeight(activity.getResources().getDisplayMetrics().heightPixels/2)
                    .showTitle(false)
                    .setCompleteButtonText("확인")
                    .setEmptySelectionText("이미지가 없습니다")
                    .showMultiImage(uriList -> {
                        if (uriList.size() > 0)
                        {
                            if(uriList.size() == 1 && oneSelectCrop) {
                                if (context != null && fragment != null) {
                                    CropImage.activity(uriList.get(0))
                                            .setActivityTitle(getStr(context.getResources(), R.string.MSG_PHOTO_SELECT))
                                            .setGuidelines(CropImageView.Guidelines.ON)
                                            .setInitialCropWindowPaddingRatio(0)
                                            .start(context, fragment);
                                }
                                else
                                {
                                    CropImage.activity(uriList.get(0))
                                            .setActivityTitle(getStr(activity.getResources(), R.string.MSG_PHOTO_SELECT))
                                            .setGuidelines(CropImageView.Guidelines.ON)
                                            .setInitialCropWindowPaddingRatio(0)
                                            .start(activity);
                                }
                            }
                            else
                            {
                                if(multiSelectListener != null)
                                    multiSelectListener.Listener(uriList);
                            }

                        }
                    });
        }

    }

    public Uri getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private void GetPhotoVideoInGallery(FragmentActivity activity, final PhotoSelectListener selectListener) {

        TedBottomPicker.with(activity)
                .setPeekHeight(activity.getResources().getDisplayMetrics().heightPixels/2)
                .showTitle(false)
                .showVideoMedia()
                .setCompleteButtonText("확인")
                .setEmptySelectionText("동영상이 없습니다")
                .showMultiImage(uriList -> {
                    if(selectListener != null)
                        selectListener.Listener(uriList);
                });
    }

    public void SetCropImage(Context context, Uri uri, int addImgIndex, ImageView imageView, final FirebaseManager.CheckFirebaseComplete listener) {

        Bitmap originalBm = null;
        try {
            originalBm = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if(imageView != null)
            DrawImageByGlide(context, imageView, originalBm, true);

        final FirebaseManager.CheckFirebaseComplete uploadlistener = new FirebaseManager.CheckFirebaseComplete() {
            @Override
            public void CompleteListener() {
                if (listener!=null)
                    listener.CompleteListener();
            }

            @Override
            public void CompleteListener_Yes() {
            }

            @Override
            public void CompleteListener_No() {
            }
        };

        FirebaseManager.getInstance().UploadImg(TKManager.getInstance().MyData.GetUserIndex(), originalBm, addImgIndex, uploadlistener);
        if(addImgIndex == 0)
            FirebaseManager.getInstance().UploadThumbImg(TKManager.getInstance().MyData.GetUserIndex(), originalBm, uploadlistener);
    }

    public void SetImageInChatRoom(Context context, Uri uri,  String roomIndex, final FirebaseManager.CheckFirebaseComplete listener) {

        //ImageResize.resizeFile(file, file, 1280, camera);

/*        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap originalBm = BitmapFactory.decodeFile(file.getAbsolutePath(), options);*/
        //iv_SignUp_Profile.setImageBitmap(originalBm);

        Bitmap originalBm = null;
        try {
            originalBm = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        final FirebaseManager.CheckFirebaseComplete uploadlistener = new FirebaseManager.CheckFirebaseComplete() {
            @Override
            public void CompleteListener() {
                if (listener!=null)
                    listener.CompleteListener();
            }

            @Override
            public void CompleteListener_Yes() {
            }

            @Override
            public void CompleteListener_No() {
            }
        };

        FirebaseManager.getInstance().UploadImgInChatRoom(roomIndex, originalBm,  uploadlistener);
    }


    public void GetPermissionForGalleryCamera(final FragmentActivity activity, Context context, Fragment fragment, final PhotoSelectListener multiSelectListener, boolean oneSelectCrop) {
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                CommonFunc.getInstance().GetPhotoInGallery(activity, context, fragment, multiSelectListener, oneSelectCrop);
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
            }
        };

        TedPermission.with(activity)
                .setPermissionListener(permissionListener)
                .setRationaleMessage(activity.getResources().getString(R.string.permission_cammera))
                .setDeniedMessage(activity.getResources().getString(R.string.permission_request))
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();

    }

    public void GetPermissionForGalleryVideo(final FragmentActivity activity, final PhotoSelectListener selectListener) {
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                CommonFunc.getInstance().GetPhotoVideoInGallery(activity, selectListener);
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
            }
        };

        TedPermission.with(activity)
                .setPermissionListener(permissionListener)
                .setRationaleMessage(activity.getResources().getString(R.string.permission_cammera))
                .setDeniedMessage(activity.getResources().getString(R.string.permission_request))
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();

    }







    public interface CheckLocationComplete {
        void CompleteListener();

        void CompleteListener_Yes();

        void CompleteListener_No();
    }

    @SuppressLint("MissingPermission")
    public void GetUserLocation(final Activity activity, final CommonFunc.CheckLocationComplete listener)
    {
        DialogFunc.getInstance().ShowToast(activity, " 위치정보 받아 오는 중", true);

        final Geocoder geocoder = new Geocoder(activity);
        final LocationManager lm = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);

        List<String> temp = lm.getAllProviders();

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_LOW);
        criteria.setPowerRequirement(Criteria.POWER_LOW);

        // getting GPS status
        boolean isGPSEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);

        // getting network status
        boolean isNetworkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if(isNetworkEnabled == false)
        {
            DialogFunc.getInstance().ShowToast(activity, "위치 정보 옵션을 켜주세요 \n 정확한 위치 정보를 사용할 수 없습니다", true);

            TKManager.getInstance().MyData.SetUserDist_Lon(127.001699);
            TKManager.getInstance().MyData.SetUserDist_Lat(37.564214);
            TKManager.getInstance().MyData.SetUserDist_Area("대한민국");
            TKManager.getInstance().MyData.SetUserDist_Region(1.0);

            if(listener != null)
                listener.CompleteListener();
        }

        else
        {
            String temp1 = lm.getBestProvider(criteria, true);

            final Location lastLocation = lm
                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            double lastLat = 0;
            double lastLon= 0;

            if(lastLocation != null)
            {
                lastLat = lastLocation.getLatitude();
                lastLon = lastLocation.getLongitude();
            }
            else
            {
                lastLat = 37.564214;
                lastLon = 127.001699;
            }

            final double finalLastLon = lastLon;
            final double finalLastLat = lastLat;
            lm.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, new LocationListener(){
                @Override
                public void onLocationChanged(Location location) {


                    Log.d("@@@@ Location  ", "onLocationChanged" + location);

                    //위치 받아왔을 때 실행시킬 명령어 입력
                    String provider = location.getProvider();
                    double longitude = location.getLongitude();
                    double latitude = location.getLatitude();
                    double altitude = location.getAltitude();

                    List<Address> list = null;
                    try {
                        list = geocoder.getFromLocation(
                                latitude, // 위도
                                longitude, // 경도
                                1); // 얻어올 값의 개수

                        TKManager.getInstance().MyData.SetUserDist_Lon(longitude);
                        TKManager.getInstance().MyData.SetUserDist_Lat(latitude);

                    } catch (IOException e) {
                        e.printStackTrace();

                        if(lastLocation != null)
                        {
                            TKManager.getInstance().MyData.SetUserDist_Lon(finalLastLon);
                            TKManager.getInstance().MyData.SetUserDist_Lat(finalLastLat);
                            TKManager.getInstance().MyData.SetUserDist_Area("대한민국");
                            TKManager.getInstance().MyData.SetUserDist_Region(1.0);
                        }
                        else
                        {
                            TKManager.getInstance().MyData.SetUserDist_Lon(127.001699);
                            TKManager.getInstance().MyData.SetUserDist_Lat(37.564214);
                            TKManager.getInstance().MyData.SetUserDist_Area("대한민국");
                            TKManager.getInstance().MyData.SetUserDist_Region(1.0);
                        }


                        if(listener != null)
                            listener.CompleteListener();
                    }

                    if (list != null) {
                        if (list.size() == 0) {
                            if(lastLocation != null)
                            {
                                TKManager.getInstance().MyData.SetUserDist_Lon(finalLastLon);
                                TKManager.getInstance().MyData.SetUserDist_Lat(finalLastLat);
                                TKManager.getInstance().MyData.SetUserDist_Area("대한민국");
                                TKManager.getInstance().MyData.SetUserDist_Region(1.0);
                            }
                            else
                            {
                                TKManager.getInstance().MyData.SetUserDist_Lon(127.001699);
                                TKManager.getInstance().MyData.SetUserDist_Lat(37.564214);
                                TKManager.getInstance().MyData.SetUserDist_Area("대한민국");
                                TKManager.getInstance().MyData.SetUserDist_Region(1.0);
                            }
                        } else {
                            TKManager.getInstance().MyData.SetUserDist_Area(list.get(0).getAdminArea() + " " + list.get(0).getSubLocality() + " " + list.get(0).getThoroughfare());
                            TKManager.getInstance().MyData.SetUserDist_Region(1.0);

                        }

                        DialogFunc.getInstance().ShowToast(activity, TKManager.getInstance().MyData.GetUserDist_Area(), true);
                        if(listener != null)
                            listener.CompleteListener();
                    }
                    else
                    {

                        Log.d("@@@@ Location  ", "list null");
                        TKManager.getInstance().MyData.SetUserDist_Lon(127.001699);
                        TKManager.getInstance().MyData.SetUserDist_Lat(37.564214);
                        TKManager.getInstance().MyData.SetUserDist_Area("대한민국");
                        TKManager.getInstance().MyData.SetUserDist_Region(1.0);

                        if(listener != null)
                            listener.CompleteListener();
                    }

                }
                @Override
                public void onProviderDisabled(String provider) {
                    // TODO Auto-generated method stub
                    Log.d("@@@@ Location  ", "onProviderDisabled" + provider);
                }
                @Override
                public void onProviderEnabled(String provider) {
                    // TODO Auto-generated method stub
                    Log.d("@@@@ Location  ", "onProviderEnabled" + provider);
                }
                @Override
                public void onStatusChanged(String provider, int status,
                                            Bundle extras) {
                    // TODO Auto-generated method stub
                    Log.d("@@@@ Location  ", "onStatusChanged" + provider);
                }
            }, null);
        }



/*

        LocationListener gpsLocationListener = null;
        gpsLocationListener = new LocationListener() {

            public void onLocationChanged(Location location) {
                String provider = location.getProvider();
                double longitude = location.getLongitude();
                double latitude = location.getLatitude();
                double altitude = location.getAltitude();

                List<Address> list = null;
                try {
                    list = geocoder.getFromLocation(
                            latitude, // 위도
                            longitude, // 경도
                            10); // 얻어올 값의 개수

                    TKManager.getInstance().MyData.SetUserDist_Lon(longitude);
                    TKManager.getInstance().MyData.SetUserDist_Lat(latitude);

                } catch (IOException e) {
                    e.printStackTrace();


                    TKManager.getInstance().MyData.SetUserDist_Lon(127.001699);
                    TKManager.getInstance().MyData.SetUserDist_Lat(37.564214);
                    TKManager.getInstance().MyData.SetUserDist_Area("대한민국");
                    TKManager.getInstance().MyData.SetUserDist_Region(1.0);

                    if(listener != null)
                        listener.CompleteListener_No();
                }

                if (list != null) {
                    if (list.size() == 0) {
                        TKManager.getInstance().MyData.SetUserDist_Area("대한민국");
                        TKManager.getInstance().MyData.SetUserDist_Region(1.0);
                        if(listener != null)
                            listener.CompleteListener();

                    } else {
                        TKManager.getInstance().MyData.SetUserDist_Area(list.get(0).getAdminArea() + " " + list.get(0).getSubLocality() + " " + list.get(0).getThoroughfare());
                        TKManager.getInstance().MyData.SetUserDist_Region(1.0);
                        if(listener != null)
                            listener.CompleteListener();
                    }
                }
                else
                {

                    Log.d("@@@@ Location  ", "list null");
                    TKManager.getInstance().MyData.SetUserDist_Lon(127.001699);
                    TKManager.getInstance().MyData.SetUserDist_Lat(37.564214);
                    TKManager.getInstance().MyData.SetUserDist_Area("대한민국");
                    TKManager.getInstance().MyData.SetUserDist_Region(1.0);

                    if(listener != null)
                        listener.CompleteListener_No();
                }

            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log.d("@@@@ Location  ", "onStatusChanged" + provider + "___" + status);
            }

            public void onProviderEnabled(String provider) {
                Log.d("@@@@ Location  ", "onProviderEnabled" + provider);
            }

            public void onProviderDisabled(String provider) {
                Log.d("@@@@ Location  ", "onProviderDisabled" + provider);
  */
/*              if(listener != null)
                    listener.CompleteListener_No();*//*

            }
        };



        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                1000,
                1,
                gpsLocationListener);

        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                1000,
                1,
                gpsLocationListener);
*/
    }

    public void SortByFilter(ArrayList<String> UserList, ArrayList<String> ViewList, boolean descending)
    {
        ViewList.clear();

        ArrayList<String> tempDataList = new ArrayList<String>();
        tempDataList = UserList;
        Map<String, Long> tempDataMap = new LinkedHashMap<String, Long>();

        for(int i=0; i<tempDataList.size(); i++)
        {
            if(TKManager.getInstance().UserData_Simple.get(tempDataList.get(i)) == null)
            {
                Log.d("#^&#^#^", "index : " + tempDataList.get(i));
            }
            else
            {
                tempDataMap.put(tempDataList.get(i), TKManager.getInstance().UserData_Simple.get(tempDataList.get(i)).GetUserDist());

                switch (TKManager.getInstance().FilterData.GetGender())
                {
                    // 모두
                    case 0:
                        if(TKManager.getInstance().UserData_Simple.get(tempDataList.get(i)).GetUserAge() >= TKManager.getInstance().FilterData.GetMinAge() &&
                                TKManager.getInstance().UserData_Simple.get(tempDataList.get(i)).GetUserAge() <= TKManager.getInstance().FilterData.GetMaxAge())
                        {
                            if(TKManager.getInstance().FilterData.GetDistance() == 100)
                            {

                                switch (TKManager.getInstance().FilterData.GetConnect())
                                {
                                    case 0:
                                        if(GetConnectDate(TKManager.getInstance().UserData_Simple.get(tempDataList.get(i)).GetUserConnectDate()) == Long.parseLong(CommonFunc.getInstance().GetCurrentDate()))
                                        {
                                            ViewList.add(tempDataList.get(i));
                                        }
                                        break;

                                    case 1:
                                        if(GetConnectDate(TKManager.getInstance().UserData_Simple.get(tempDataList.get(i)).GetUserConnectDate() + 1 )>= Long.parseLong(CommonFunc.getInstance().GetCurrentDate()))
                                        {
                                            ViewList.add(tempDataList.get(i));
                                        }
                                        break;

                                    case 2:
                                        // if(TKManager.getInstance().UserData_Simple.get(tempDataList.get(i)).GetUserConnectDate() + 3 >= Long.parseLong(CommonFunc.getInstance().GetCurrentDate()))
                                    {
                                        ViewList.add(tempDataList.get(i));
                                    }
                                    break;

                                }
                            }
                            else
                            {
                                if((int)(TKManager.getInstance().UserData_Simple.get(tempDataList.get(i)).GetUserDist() / 1000) <= TKManager.getInstance().FilterData.GetDistance())
                                {

                                    switch (TKManager.getInstance().FilterData.GetConnect())
                                    {
                                        case 0:
                                            if(GetConnectDate(TKManager.getInstance().UserData_Simple.get(tempDataList.get(i)).GetUserConnectDate() )== Long.parseLong(CommonFunc.getInstance().GetCurrentDate()))
                                            {
                                                ViewList.add(tempDataList.get(i));
                                            }
                                            break;

                                        case 1:
                                            if(GetConnectDate(TKManager.getInstance().UserData_Simple.get(tempDataList.get(i)).GetUserConnectDate() + 1) >= Long.parseLong(CommonFunc.getInstance().GetCurrentDate()))
                                            {
                                                ViewList.add(tempDataList.get(i));
                                            }
                                            break;

                                        case 2:
                                            // if(TKManager.getInstance().UserData_Simple.get(tempDataList.get(i)).GetUserConnectDate() + 3 >= Long.parseLong(CommonFunc.getInstance().GetCurrentDate()))
                                        {
                                            ViewList.add(tempDataList.get(i));
                                        }
                                        break;

                                    }


                                }
                            }

                        }
                        break;

                    // 여자
                    case 1:
                        if(TKManager.getInstance().UserData_Simple.get(tempDataList.get(i)).GetUserGender() == 1)
                        {
                            if(TKManager.getInstance().UserData_Simple.get(tempDataList.get(i)).GetUserAge() >= TKManager.getInstance().FilterData.GetMinAge() &&
                                    TKManager.getInstance().UserData_Simple.get(tempDataList.get(i)).GetUserAge() <= TKManager.getInstance().FilterData.GetMaxAge())
                            {
                                if(TKManager.getInstance().FilterData.GetDistance() == 100)
                                {

                                    switch (TKManager.getInstance().FilterData.GetConnect())
                                    {
                                        case 0:
                                            if(GetConnectDate(TKManager.getInstance().UserData_Simple.get(tempDataList.get(i)).GetUserConnectDate() )== Long.parseLong(CommonFunc.getInstance().GetCurrentDate()))
                                            {
                                                ViewList.add(tempDataList.get(i));
                                            }
                                            break;

                                        case 1:
                                            if(GetConnectDate(TKManager.getInstance().UserData_Simple.get(tempDataList.get(i)).GetUserConnectDate() + 1 )>= Long.parseLong(CommonFunc.getInstance().GetCurrentDate()))
                                            {
                                                ViewList.add(tempDataList.get(i));
                                            }
                                            break;

                                        case 2:
                                            // if(TKManager.getInstance().UserData_Simple.get(tempDataList.get(i)).GetUserConnectDate() + 3 >= Long.parseLong(CommonFunc.getInstance().GetCurrentDate()))
                                        {
                                            ViewList.add(tempDataList.get(i));
                                        }
                                        break;

                                    }
                                }
                                else
                                {
                                    if((int)(TKManager.getInstance().UserData_Simple.get(tempDataList.get(i)).GetUserDist() / 1000) <= TKManager.getInstance().FilterData.GetDistance())
                                    {

                                        switch (TKManager.getInstance().FilterData.GetConnect())
                                        {
                                            case 0:
                                                if(GetConnectDate(TKManager.getInstance().UserData_Simple.get(tempDataList.get(i)).GetUserConnectDate()) == Long.parseLong(CommonFunc.getInstance().GetCurrentDate()))
                                                {
                                                    ViewList.add(tempDataList.get(i));
                                                }
                                                break;

                                            case 1:
                                                if(GetConnectDate(TKManager.getInstance().UserData_Simple.get(tempDataList.get(i)).GetUserConnectDate() + 1) >= Long.parseLong(CommonFunc.getInstance().GetCurrentDate()))
                                                {
                                                    ViewList.add(tempDataList.get(i));
                                                }
                                                break;

                                            case 2:
                                                // if(TKManager.getInstance().UserData_Simple.get(tempDataList.get(i)).GetUserConnectDate() + 3 >= Long.parseLong(CommonFunc.getInstance().GetCurrentDate()))
                                            {
                                                ViewList.add(tempDataList.get(i));
                                            }
                                            break;

                                        }


                                    }
                                }

                            }
                        }
                        break;
                    // 남자
                    case 2:
                        if(TKManager.getInstance().UserData_Simple.get(tempDataList.get(i)).GetUserGender() == 0)
                        {
                            if(TKManager.getInstance().UserData_Simple.get(tempDataList.get(i)).GetUserAge() >= TKManager.getInstance().FilterData.GetMinAge() &&
                                    TKManager.getInstance().UserData_Simple.get(tempDataList.get(i)).GetUserAge() <= TKManager.getInstance().FilterData.GetMaxAge())
                            {
                                if(TKManager.getInstance().FilterData.GetDistance() == 100)
                                {

                                    switch (TKManager.getInstance().FilterData.GetConnect())
                                    {
                                        case 0:
                                            if(GetConnectDate(TKManager.getInstance().UserData_Simple.get(tempDataList.get(i)).GetUserConnectDate()) == Long.parseLong(CommonFunc.getInstance().GetCurrentDate()))
                                            {
                                                ViewList.add(tempDataList.get(i));
                                            }
                                            break;

                                        case 1:
                                            if(GetConnectDate(TKManager.getInstance().UserData_Simple.get(tempDataList.get(i)).GetUserConnectDate() + 1) >= Long.parseLong(CommonFunc.getInstance().GetCurrentDate()))
                                            {
                                                ViewList.add(tempDataList.get(i));
                                            }
                                            break;

                                        case 2:
                                            // if(TKManager.getInstance().UserData_Simple.get(tempDataList.get(i)).GetUserConnectDate() + 3 >= Long.parseLong(CommonFunc.getInstance().GetCurrentDate()))
                                        {
                                            ViewList.add(tempDataList.get(i));
                                        }
                                        break;

                                    }
                                }
                                else
                                {
                                    if((int)(TKManager.getInstance().UserData_Simple.get(tempDataList.get(i)).GetUserDist() / 1000) <= TKManager.getInstance().FilterData.GetDistance())
                                    {

                                        switch (TKManager.getInstance().FilterData.GetConnect())
                                        {
                                            case 0:
                                                if(GetConnectDate(TKManager.getInstance().UserData_Simple.get(tempDataList.get(i)).GetUserConnectDate()) == Long.parseLong(CommonFunc.getInstance().GetCurrentDate()))
                                                {
                                                    ViewList.add(tempDataList.get(i));
                                                }
                                                break;

                                            case 1:
                                                if(GetConnectDate(TKManager.getInstance().UserData_Simple.get(tempDataList.get(i)).GetUserConnectDate() + 1 )>= Long.parseLong(CommonFunc.getInstance().GetCurrentDate()))
                                                {
                                                    ViewList.add(tempDataList.get(i));
                                                }
                                                break;

                                            case 2:
                                                // if(TKManager.getInstance().UserData_Simple.get(tempDataList.get(i)).GetUserConnectDate() + 3 >= Long.parseLong(CommonFunc.getInstance().GetCurrentDate()))
                                            {
                                                ViewList.add(tempDataList.get(i));
                                            }
                                            break;

                                        }


                                    }
                                }

                            }
                        }
                        break;
                }


                //if(TKManager.getInstance().UserData_Simple.get(tempDataList.get(i)).GetUserConnectDate())

            }
            // tempDataMap.put(tempDataList.get(i), (long) 0);
        }

  /*      Iterator it = sortByValue(tempDataMap, descending).iterator();

        UserList.clear();
        while(it.hasNext()) {
            String temp = (String) it.next();
            System.out.println(temp + " = " + tempDataMap.get(temp));
            UserList.add(temp);
        }*/
    }

    public double DistanceByDegree(double _latitude1, double _longitude1, double _latitude2, double _longitude2){
        Location startPos = new Location("PointA");
        Location endPos = new Location("PointB");

        startPos.setLatitude(_latitude1);
        startPos.setLongitude(_longitude1);
        endPos.setLatitude(_latitude2);
        endPos.setLongitude(_longitude2);

        double distance = startPos.distanceTo(endPos);

        return distance;
    }

    public void SortByDistance(ArrayList<String> UserList, ArrayList<String> ViewList, boolean descending)
    {
        ArrayList<String> tempDataList = new ArrayList<String>();
        tempDataList = UserList;
        Map<String, Long> tempDataMap = new LinkedHashMap<String, Long>();

        for(int i=0; i<UserList.size(); i++)
        {

            if(TKManager.getInstance().UserData_Simple.get(tempDataList.get(i)) == null)
            {
                Log.d("#^&#^#^", "index : " + tempDataList.get(i));

            }
            else
            {
                tempDataMap.put(tempDataList.get(i), TKManager.getInstance().UserData_Simple.get(tempDataList.get(i)).GetUserDist());
            }

            // tempDataMap.put(tempDataList.get(i), (long) 0);
        }

        Iterator it = sortByValue(tempDataMap, descending).iterator();

        UserList.clear();
        while(it.hasNext()) {
            String temp = (String) it.next();
            System.out.println(temp + " = " + tempDataMap.get(temp));
            UserList.add(temp);
        }


        SortByFilter(UserList, ViewList, true);

    }

    public void SortByChatDate(ArrayList<String> chatRoomIndex, boolean descending)
    {
        ArrayList<String> tempDataList = new ArrayList<String>();
        tempDataList = chatRoomIndex;
        Map<String, Long> tempDataMap = new LinkedHashMap<String, Long>();
        Map<String, ChatData> tempChatDataMap = new LinkedHashMap<String, ChatData>();

        for(int i=0; i<tempDataList.size(); i++)
        {
            tempDataMap.put(tempDataList.get(i), TKManager.getInstance().MyData.GetUserChatDataList(tempDataList.get(i)).GetMsgDate());
        }

        Iterator it = sortByValue(tempDataMap, descending).iterator();

        chatRoomIndex.clear();

        while(it.hasNext()) {
            String temp = (String) it.next();
            System.out.println(temp + " = " + tempDataMap.get(temp));
            chatRoomIndex.add(temp);
            tempChatDataMap.put(temp, TKManager.getInstance().MyData.GetUserChatDataList(temp));
        }
        TKManager.getInstance().MyData.ClearUserChatDataList();
        TKManager.getInstance().MyData.SetUserChatDataList(tempChatDataMap);

    }

    public static List sortByValue(final Map map, boolean descending) {
        List<String> list = new ArrayList();
        list.addAll(map.keySet());

        Collections.sort(list,new Comparator() {
            public int compare(Object o1,Object o2) {
                Object v1 = map.get(o1);
                Object v2 = map.get(o2);
                return ((Comparable) v2).compareTo(v1);
            }
        });

        if(descending)
            Collections.reverse(list); // 주석시 오름차순

        return list;
    }

    public void MoveAuthActivity(Activity activity) {
        final Intent intent = new Intent(activity, AuthActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }


    public void MoveLoginActivity(Activity activity) {

        final Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }

    public void MoveSignUpActivity(Activity activity) {

        final Intent intent = new Intent(activity, SignUpActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }

    public void MoveMainActivity(final Activity activity, boolean boot) {
        MoveMainActivity(activity, -1, -1, boot);
    }

    public void MoveMainActivity(final Activity activity , int mainTabPostion, int subTabPostion, boolean boot) {


        SharedPreferences sharedPreferences = activity.getSharedPreferences("userFile",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Index",TKManager.getInstance().MyData.GetUserIndex());
        editor.commit();

        if(boot)
        {
            GetChatReadIndex(activity);

            FirebaseManager.CheckFirebaseComplete listener = new FirebaseManager.CheckFirebaseComplete() {
                @Override
                public void CompleteListener() {

                    TKManager.getInstance().isLoadDataByBoot = false;
                    final Intent intent = new Intent(activity, MainActivity.class);
                    intent.putExtra("maintabpostion", mainTabPostion);
                    intent.putExtra("subtabpostion", subTabPostion);
                    activity.startActivity(intent);
                    activity.finish();
                }

                @Override
                public void CompleteListener_Yes() {

                }

                @Override
                public void CompleteListener_No() {

                }
            };
            FirebaseManager.getInstance().SetMyDataOnFireBase(boot, listener);
        }

        else
        {

            final Intent intent = new Intent(activity, MainActivity.class);
            intent.putExtra("maintabpostion", mainTabPostion);
            intent.putExtra("subtabpostion", subTabPostion);
            activity.startActivity(intent);
            activity.finish();
        }
    }

    public void RefreshUserList(final  Activity activity)
    {
        DialogFunc.getInstance().ShowLoadingPage(activity);

        TKManager.getInstance().UserList_Dist.clear();
        TKManager.getInstance().UserList_New.clear();
        TKManager.getInstance().UserData_Simple.clear();
        TKManager.getInstance().MyData.ClearUserFriendList();
        TKManager.getInstance().MyData.ClearRequestFriendList();

        FirebaseManager.CheckFirebaseComplete Innerlistener = new FirebaseManager.CheckFirebaseComplete() {
            @Override
            public void CompleteListener() {
                //   DialogFunc.getInstance().DismissLoadingPage();

                CommonFunc.getInstance().SortByDistance(TKManager.getInstance().UserList_Dist, TKManager.getInstance().View_UserList_Dist, true);
                //CommonFunc.getInstance().SortByDistance(TKManager.getInstance().UserList_New, TKManager.getInstance().View_UserList_New, true);
                TKManager.getInstance().View_UserList_New = TKManager.getInstance().UserList_New;
                TKManager.getInstance().View_UserList_Hot = TKManager.getInstance().UserList_Hot;

                long seed = System.nanoTime();
                Collections.shuffle(TKManager.getInstance().View_UserList_Hot, new Random(seed));



                MoveMainActivity(activity, false);
            }

            @Override
            public void CompleteListener_Yes() {
            }

            @Override
            public void CompleteListener_No() {
                DialogFunc.getInstance().DismissLoadingPage();
            }
        };

        FirebaseManager.getInstance().GetUserList(Innerlistener);
    }
    public void GetUserList(final Activity activity)
    {

        DialogFunc.getInstance().ShowLoadingPage(activity);

        FirebaseManager.CheckFirebaseComplete listener = new FirebaseManager.CheckFirebaseComplete() {
            @Override
            public void CompleteListener() {

                TKManager.getInstance().UserData_Simple.put(TKManager.getInstance().MyData.GetUserIndex(), TKManager.getInstance().MyData);


                FirebaseManager.CheckFirebaseComplete Innerlistener = new FirebaseManager.CheckFirebaseComplete() {
                    @Override
                    public void CompleteListener() {
                        //   DialogFunc.getInstance().DismissLoadingPage();

                        CommonFunc.getInstance().SortByDistance(TKManager.getInstance().UserList_Dist, TKManager.getInstance().View_UserList_Dist, true);
                        //CommonFunc.getInstance().SortByDistance(TKManager.getInstance().UserList_New, TKManager.getInstance().View_UserList_New, true);
                        TKManager.getInstance().View_UserList_New = TKManager.getInstance().UserList_New;

                        //TKManager.getInstance().UserList_Hot = TKManager.getInstance().UserList_Search_Hot;
                        TKManager.getInstance().View_UserList_Hot = TKManager.getInstance().UserList_Hot;

                        long seed = System.nanoTime();
                        Collections.shuffle(TKManager.getInstance().View_UserList_Hot, new Random(seed));



                        MoveMainActivity(activity, true);
                    }

                    @Override
                    public void CompleteListener_Yes() {
                    }

                    @Override
                    public void CompleteListener_No() {
                        DialogFunc.getInstance().DismissLoadingPage();
                    }
                };

                FirebaseManager.getInstance().GetUserList(Innerlistener);
            }

            @Override
            public void CompleteListener_Yes() {
            }

            @Override
            public void CompleteListener_No() {
                DialogFunc.getInstance().DismissLoadingPage();
                MoveSignUpActivity(activity);
            }
        };

        FirebaseManager.getInstance().GetUserData(TKManager.getInstance().MyData.GetUserIndex(), TKManager.getInstance().MyData, listener );
    }

    public static String getWhatKindOfNetwork(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                return WIFI_STATE;
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                return MOBILE_STATE;
            }
        }
        return NONE_STATE;
    }

    public Long GetConnectDate(long date)
    {
        String tempData = Long.toString(date);
        String result = tempData.substring(0, 8);
        return Long.parseLong(result);
    }

    public String GetConnectGap(String startDate, String EndDate){

        String result = "";

        if(EndDate.length() < 14)
        {
            EndDate += "000000";
        }
        if(startDate.length() < 14)
        {
            startDate += "000000";
        }

        String start = startDate.substring(0, 14);
        String end = EndDate.substring(0, 14);

        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            Date beginDate = formatter.parse(start);
            Date endDate = formatter.parse(end);

            // 시간차이를 시간,분,초를 곱한 값으로 나누면 하루 단위가 나옴
            long diff = beginDate.getTime() - endDate.getTime();

            long diffMin = diff / (60 * 1000);
            if(diffMin < 30)
            {
                result = "조금 전";
            }
            else if(diffMin < 60)
            {
                result = "1시간";
            }
            else if(diffMin < 60 * 24)
            {
                result = "1일 전";
            }
            else if(diffMin < 60 * 48)
            {
                result = "2일 전";
            }
            else if(diffMin < 60 * 72)
            {
                result = "3일 전";
            }
            else
            {
                result = "3일 이상";
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    //별도의 스태틱 함수로 구현

    public void SortByClubContentDate(Map<String, ClubContextData> list, boolean descending)
    {

        Map<String, Long> tempDataMap = new LinkedHashMap<String, Long>();
        Map<String, ClubContextData> tempClubContentDateDataMap = new LinkedHashMap<String, ClubContextData>();

        Iterator<String> keys = list.keySet().iterator();
        while( keys.hasNext() ){
            String key = keys.next();
            tempDataMap.put(key, Long.parseLong(list.get(key).GetDate()));
        }

        Iterator it = sortByValue(tempDataMap, descending).iterator();

        while(it.hasNext()) {
            String temp = (String) it.next();
            tempClubContentDateDataMap.put(temp, list.get(temp));
        }

        list.clear();
        list.putAll(tempClubContentDateDataMap);





       /* ArrayList<String> tempDataList = new ArrayList<>();
        tempDataList.addAll(list.keySet());
        Map<String, Long> tempDataMap = new LinkedHashMap<String, Long>();
        Map<String, ClubContextData> tempClubContentDateDataMap = new LinkedHashMap<String, ClubContextData>();

        for(int i=0; i<tempDataList.size(); i++)
        {
            Log.d("test", "@@@@@@@@@@@@@@11" + tempDataList.get(i) + " = " + Long.parseLong(list.get(tempDataList.get(i)).GetDate()));
            tempDataMap.put(tempDataList.get(i), Long.parseLong(list.get(tempDataList.get(i)).GetDate()));
        }

        Iterator it = sortByValue(tempDataMap, descending).iterator();

        tempClubContentDateDataMap.clear();

        while(it.hasNext()) {
            String temp = (String) it.next();
            Log.d("test", "@@@@@@@@@@@@@@22" + temp + " = " + tempDataMap.get(temp));
            tempClubContentDateDataMap.put(temp, list.get(temp));
        }

        list.clear();
        list.putAll(tempClubContentDateDataMap);*/
    }

    public final String getComleteWordByJongsung(String name, String firstValue, String secondValue) {

        char lastName = name.charAt(name.length() - 1);

        // 한글의 제일 처음과 끝의 범위밖일 경우는 오류
        if (lastName < 0xAC00 || lastName > 0xD7A3) {
            return name;
        }


        String seletedValue = (lastName - 0xAC00) % 28 > 0 ? firstValue : secondValue;

        return name+seletedValue;
    }

    public String ConvertTimeSrt(String time, String nextFormat)
    {
        String returnString = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

        try {
            Date date = dateFormat.parse(time);

            SimpleDateFormat sdfNow = new SimpleDateFormat(nextFormat);
            returnString = sdfNow.format(date);

            returnString = returnString.replace("PM", "오후");
            returnString = returnString.replace("AM", "오전");
        } catch (ParseException e) {
        }

        return returnString;
    }

    public void GetUserDataInFireBase(String userIndex, final Activity activity, final boolean intentNew)
    {
        if(userIndex.equals(TKManager.getInstance().MyData.GetUserIndex()))
        {
            DialogFunc.getInstance().ShowToast(activity, CommonFunc.getInstance().getStr(activity.getResources(), R.string.MSG_MY_PROFILE_CLICK), true);
            DialogFunc.getInstance().DismissLoadingPage();
        }
        else
        {
            DialogFunc.getInstance().ShowLoadingPage(activity);
            FirebaseManager.CheckFirebaseComplete listener = new FirebaseManager.CheckFirebaseComplete() {
                @Override
                public void CompleteListener() {

                    Set KeySet = TKManager.getInstance().TargetUserData.GetUserClubDataKeySet();

                    if(KeySet.size() > 0)
                    {
                        Iterator iterator = KeySet.iterator();

                        FirebaseManager.getInstance().SetFireBaseLoadingCount(TKManager.getInstance().TargetUserData.GetUserClubDataCount());

                        FirebaseManager.CheckFirebaseComplete listener = new FirebaseManager.CheckFirebaseComplete() {
                            @Override
                            public void CompleteListener() {

                                Intent intent = new Intent(activity, UserProfileActivity.class);
                                if(intentNew)
                                    intent.setFlags(FLAG_ACTIVITY_NEW_TASK);

                                activity.startActivity(intent);
                            }

                            @Override
                            public void CompleteListener_Yes() {
                            }

                            @Override
                            public void CompleteListener_No() {
                            }
                        };

                        while(iterator.hasNext()){
                            String key = (String)iterator.next();
                            if(TKManager.getInstance().ClubData_Simple.get(key) != null)
                            {
                                FirebaseManager.getInstance().Complete(listener);
                            }
                            else
                                FirebaseManager.getInstance().GetClubData_Simple(key, TKManager.getInstance().ClubData_Simple, listener);
                        }
                    }
                    else
                    {
                        Intent intent = new Intent(activity, UserProfileActivity.class);
                        if(intentNew)
                            intent.setFlags(FLAG_ACTIVITY_NEW_TASK);

                        activity.startActivity(intent);
                    }

                }

                @Override
                public void CompleteListener_Yes() {
                }

                @Override
                public void CompleteListener_No() {
                    DialogFunc.getInstance().DismissLoadingPage();
                }
            };

            FirebaseManager.getInstance().GetUserData(userIndex, TKManager.getInstance().TargetUserData, listener);
        }

    }

    public void SetSlotBG(int i, ImageView bg, Context context)
    {
        if(i % 2 == 0)
            ImageViewCompat.setImageTintList(bg, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.list_slot_bg_1)));
        else
            ImageViewCompat.setImageTintList(bg, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.list_slot_bg_2)));
    }

    public void SetChatReadIndex(Context activity, String chatRoomIndex, Long chatIndex)
    {
        HashMap<String, Long> HashMap = new HashMap<String, Long>();
        HashMap.put(chatRoomIndex, chatIndex);

        SharedPreferences pSharedPref = activity.getSharedPreferences("ChatReadIndex", Context.MODE_PRIVATE);
        if (pSharedPref != null){
            JSONObject jsonObject = new JSONObject(HashMap);
            String jsonString = jsonObject.toString();
            SharedPreferences.Editor editor = pSharedPref.edit();
            editor.remove("ReadIndex").commit();
            editor.putString("ReadIndex", jsonString);
            editor.commit();
        }
    }

    public void GetChatReadIndex(Activity activity)
    {
        Map<String,Boolean> outputMap = new HashMap<String,Boolean>();
        SharedPreferences pSharedPref = activity.getSharedPreferences("ChatReadIndex", Context.MODE_PRIVATE);
        try{
            if (pSharedPref != null){
                String jsonString = pSharedPref.getString("ReadIndex", (new JSONObject()).toString());
                JSONObject jsonObject = new JSONObject(jsonString);
                Iterator<String> keysItr = jsonObject.keys();
                while(keysItr.hasNext()) {
                    String key = keysItr.next();
                    //Boolean value = (Boolean) jsonObject.get(key);
                    TKManager.getInstance().MyData.SetUserChatReadIndexList(key, Long.parseLong(jsonObject.get(key).toString()));
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    public static String getDataSize(long size) {
        if (size < 0) {
            size = 0;
        }
        DecimalFormat format = new DecimalFormat("####.00");
        if (size < 1024) {
            return size + "bytes";
        } else if (size < 1024 * 1024) {
            float kbsize = size / 1024f;
            return format.format(kbsize) + "KB";
        } else if (size < 1024 * 1024 * 1024) {
            float mbsize = size / 1024f / 1024f;
            return format.format(mbsize) + "MB";
        } else if (size < 1024 * 1024 * 1024 * 1024) {
            float gbsize = size / 1024f / 1024f / 1024f;
            return format.format(gbsize) + "GB";
        } else {
            return "size: error";
        }

    }

    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
                if (isExternalStorageDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];
                    if ("primary".equalsIgnoreCase(type)) {
                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                    }
                }
                else if (isDownloadsDocument(uri)) {
                    final String id = DocumentsContract.getDocumentId(uri);
                    final Uri contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                    return getDataColumn(context, contentUri, null, null);
                }
                // MediaProvider
                else if (isMediaDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }

                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[] {
                            split[1]
                    };

                    return getDataColumn(context, contentUri, selection, selectionArgs);
                }
            }
            else if ("content".equalsIgnoreCase(uri.getScheme())) {
                return getDataColumn(context, uri, null, null);
            }
            else if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }
        }

        return null;
    }


    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public List<CustomGridListHolder> getCustomGridListHolderList(List<String> keyList)
    {
        List<CustomGridListHolder> items = new ArrayList<>();
        int bigsizetum = 0;
        for (int i = 0; i < keyList.size(); i++) {
            int colSpan = 1;

            if(bigsizetum > 0)
            {
                colSpan = 1;
                bigsizetum--;
            }
            else
            {
                colSpan = Math.random() < 0.3f ? 2 : 1;
                if(colSpan == 2)
                    bigsizetum = 2;
            }




            int rowSpan = colSpan;
            CustomGridListHolder item = new CustomGridListHolder(colSpan, rowSpan, i,keyList.get(i));
            items.add(item);
        }

        return items;
    }



































    //////////////////////////////////////////////////////////////////////////////////////////////////
    /// 테스트 함수 //
    //////////////////////////////////////////////////////////////////////////////////////////////////

    // 더미 집어넣기
    public void AddDummy(int count)
    {

        String[] tempThumb = new String[15];

        tempThumb[0] = "https://firebasestorage.googleapis.com/v0/b/fifty-e2d17.appspot.com/o/dummy_0.jpg?alt=media&token=ca161fa5-6cb3-44a2-906f-c0dc6af3fe91";
        tempThumb[1] = "https://firebasestorage.googleapis.com/v0/b/fifty-e2d17.appspot.com/o/dummy_1.jpg?alt=media&token=419af5d2-3252-4d87-a06f-a5fd8c34bfea";
        tempThumb[2] = "https://firebasestorage.googleapis.com/v0/b/fifty-e2d17.appspot.com/o/dummy_10.jpg?alt=media&token=0518202d-94b8-490c-b8fe-4ef9c5d9d7b9";
        tempThumb[3] = "https://firebasestorage.googleapis.com/v0/b/fifty-e2d17.appspot.com/o/dummy_11.jpg?alt=media&token=ead97f7f-ed07-45c6-8dd9-5375dbea7c4d";
        tempThumb[4] = "https://firebasestorage.googleapis.com/v0/b/fifty-e2d17.appspot.com/o/dummy_12.jpg?alt=media&token=181cefd5-79f9-4d19-b76b-4c1eb696f72c";

        tempThumb[5] = "https://firebasestorage.googleapis.com/v0/b/fifty-e2d17.appspot.com/o/dummy_13.jpg?alt=media&token=d106be73-a6aa-45d3-b43e-22290dcb29d6";
        tempThumb[6] = "https://firebasestorage.googleapis.com/v0/b/fifty-e2d17.appspot.com/o/dummy_14.jpg?alt=media&token=b846e5ba-b14d-4605-b4cf-28e273012686";
        tempThumb[7] = "https://firebasestorage.googleapis.com/v0/b/fifty-e2d17.appspot.com/o/dummy_15.jpg?alt=media&token=678f4493-3290-4cc9-ba32-633258b9198e";
        tempThumb[8] = "https://firebasestorage.googleapis.com/v0/b/fifty-e2d17.appspot.com/o/dummy_2.jpg?alt=media&token=b161ad73-7c32-4f9b-884b-933e1f45ce4b";
        tempThumb[9] = "https://firebasestorage.googleapis.com/v0/b/fifty-e2d17.appspot.com/o/dummy_3.jpg?alt=media&token=1885ea33-2007-4aaa-a5ee-7c469f605c36";

        tempThumb[10] = "https://firebasestorage.googleapis.com/v0/b/fifty-e2d17.appspot.com/o/dummy_4.jpg?alt=media&token=0b6a12a9-b07e-4b7b-ab19-f17be557a01b";
        tempThumb[11] = "https://firebasestorage.googleapis.com/v0/b/fifty-e2d17.appspot.com/o/dummy_5.jpg?alt=media&token=3541331d-1524-46b8-8d6c-174bcc75bb42";
        tempThumb[12] = "https://firebasestorage.googleapis.com/v0/b/fifty-e2d17.appspot.com/o/dummy_6.jpg?alt=media&token=d9a22103-c78b-460a-98a9-9559051501c1";
        tempThumb[13] = "https://firebasestorage.googleapis.com/v0/b/fifty-e2d17.appspot.com/o/dummy_7.jpg?alt=media&token=a3baf274-b2eb-4b16-a537-33d770a94341";
        tempThumb[14] = "https://firebasestorage.googleapis.com/v0/b/fifty-e2d17.appspot.com/o/dummy_8.jpg?alt=media&token=1fbada1a-ec61-4d53-a952-1622c21f1423";

        Random rnd = new Random();

        for(int i=0; i< count; i++)
        {
            UserData tempData = new UserData();
            String[] fav = new String[3];
            fav[0] = "Book";
            fav[1] = "Cook";
            fav[2] = "Golf";

            String[] img = new String[3];
            img[0] = tempThumb[rnd.nextInt(15)];
            img[1] = tempThumb[rnd.nextInt(15)];
            img[2] = tempThumb[rnd.nextInt(15)];


            // tempData.SetUserData(Integer.toString(i), Integer.toString(i), randomHangulName(), fav, tempThumb[rnd.nextInt(15)], tempThumb[rnd.nextInt(15)], rnd.nextInt(20) + 50, rnd.nextInt(2));
            //FirebaseManager.getInstance().SetUserDataOnFireBase(CommonData.CollentionType.USERS, Integer.toString(i), tempData);


            FirebaseManager.getInstance().randomList(Integer.toString(i));
        }
    }

    public static String randomHangulName() {
        List<String> 성 = Arrays.asList("김", "이", "박", "최", "정", "강", "조", "윤", "장", "임", "한", "오", "서", "신", "권", "황", "안",
                "송", "류", "전", "홍", "고", "문", "양", "손", "배", "조", "백", "허", "유", "남", "심", "노", "정", "하", "곽", "성", "차", "주",
                "우", "구", "신", "임", "나", "전", "민", "유", "진", "지", "엄", "채", "원", "천", "방", "공", "강", "현", "함", "변", "염", "양",
                "변", "여", "추", "노", "도", "소", "신", "석", "선", "설", "마", "길", "주", "연", "방", "위", "표", "명", "기", "반", "왕", "금",
                "옥", "육", "인", "맹", "제", "모", "장", "남", "탁", "국", "여", "진", "어", "은", "편", "구", "용");
        List<String> 이름 = Arrays.asList("가", "강", "건", "경", "고", "관", "광", "구", "규", "근", "기", "길", "나", "남", "노", "누", "다",
                "단", "달", "담", "대", "덕", "도", "동", "두", "라", "래", "로", "루", "리", "마", "만", "명", "무", "문", "미", "민", "바", "박",
                "백", "범", "별", "병", "보", "빛", "사", "산", "상", "새", "서", "석", "선", "설", "섭", "성", "세", "소", "솔", "수", "숙", "순",
                "숭", "슬", "승", "시", "신", "아", "안", "애", "엄", "여", "연", "영", "예", "오", "옥", "완", "요", "용", "우", "원", "월", "위",
                "유", "윤", "율", "으", "은", "의", "이", "익", "인", "일", "잎", "자", "잔", "장", "재", "전", "정", "제", "조", "종", "주", "준",
                "중", "지", "진", "찬", "창", "채", "천", "철", "초", "춘", "충", "치", "탐", "태", "택", "판", "하", "한", "해", "혁", "현", "형",
                "혜", "호", "홍", "화", "환", "회", "효", "훈", "휘", "희", "운", "모", "배", "부", "림", "봉", "혼", "황", "량", "린", "을", "비",
                "솜", "공", "면", "탁", "온", "디", "항", "후", "려", "균", "묵", "송", "욱", "휴", "언", "령", "섬", "들", "견", "추", "걸", "삼",
                "열", "웅", "분", "변", "양", "출", "타", "흥", "겸", "곤", "번", "식", "란", "더", "손", "술", "훔", "반", "빈", "실", "직", "흠",
                "흔", "악", "람", "뜸", "권", "복", "심", "헌", "엽", "학", "개", "롱", "평", "늘", "늬", "랑", "얀", "향", "울", "련");
        Collections.shuffle(성);
        Collections.shuffle(이름);
        return 성.get(0) + 이름.get(0) + 이름.get(1);
    }


}
