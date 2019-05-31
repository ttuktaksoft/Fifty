package fifty.fiftyhouse.com.fifty;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialog;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import fifty.fiftyhouse.com.fifty.DataBase.UserData;
import fifty.fiftyhouse.com.fifty.Manager.FirebaseManager;

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
    private AppCompatDialog mProgressDialog = null;

    public void GoMainActivity(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        intent.setFlags(Intent. FLAG_ACTIVITY_SINGLE_TOP);
        activity.startActivity(intent);
        activity.finish();
    }

    public String getMainSortTypeStr(Resources res, CommonData.MainSortType type)
    {
        switch (type)
        {
            case ALL:
                return getStr(res, R.string.bt_Main_Sort_Type_All);
            case ONLINE:
                return getStr(res, R.string.bt_Main_Sort_Type_Online);
        }

        return getStr(res, R.string.STR_ERROR);
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

    public interface ShowDefaultPopup_YesListener {
        void YesListener();
    }

    public void ShowDefaultPopup(Context context, String title, String centerDesc) {
        ShowDefaultPopup(context, null, null, title, centerDesc, null, null, false);
    }

    public void ShowDefaultPopup(Context context, final ShowDefaultPopup_YesListener listenerYes, final ShowDefaultPopup_YesListener listenerNo, String title, String centerDesc, String yesDesc, String noDesc) {
        ShowDefaultPopup(context, listenerYes, listenerNo, title, centerDesc, yesDesc, noDesc, true);
    }

    public void ShowDefaultPopup(Context context, final ShowDefaultPopup_YesListener listenerYes, final ShowDefaultPopup_YesListener listenerNo, String title, String centerDesc, String yesDesc, String noDesc, Boolean btnView) {
        TextView Title, CenterDesc;
        Button YesButton, NoButton;

        View v = LayoutInflater.from(context).inflate(R.layout.dialog_default_popup, null, false);

        Title = (TextView) v.findViewById(R.id.tv_defaultPop_title);
        CenterDesc = (TextView) v.findViewById(R.id.tv_defaultPop_msg);
        YesButton = (Button) v.findViewById(R.id.bt_defaultPop_yes);
        NoButton = (Button) v.findViewById(R.id.bt_defaultPop__no);

        Title.setText(title);
        CenterDesc.setText(centerDesc);


        final AlertDialog dialog = new AlertDialog.Builder(context).setView(v).create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();

        if (btnView) {
            YesButton.setVisibility(View.VISIBLE);
            NoButton.setVisibility(View.VISIBLE);

            if(noDesc == null || noDesc.equals(""))
                NoButton.setVisibility(View.GONE);

            YesButton.setText(yesDesc);
            NoButton.setText(noDesc);

            YesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listenerYes != null)
                        listenerYes.YesListener();
                    dialog.dismiss();
                }
            });
            NoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listenerNo != null)
                        listenerNo.YesListener();
                    dialog.dismiss();
                }
            });
        } else {
            YesButton.setVisibility(View.GONE);
            NoButton.setVisibility(View.GONE);

            YesButton.setOnClickListener(null);
            NoButton.setOnClickListener(null);
        }
    }

    public void ShowLoadingPage(Context context, String message) {

        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            SetShowLoadingPageMsg(context, message);
        } else {

            mProgressDialog = new AppCompatDialog(context);
            mProgressDialog.setCancelable(false);
            mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            mProgressDialog.setContentView(R.layout.dialog_default_loading);
            mProgressDialog.show();

        }

        TextView tv_progress_message = (TextView) mProgressDialog.findViewById(R.id.tv_progress_message);

        if(message.isEmpty())
        {
            //tv_progress_message.setText("로딩중");
        }


        ImageView iv_progress_loading = (ImageView) mProgressDialog.findViewById(R.id.iv_loading_logo);
        Glide.with(context)
                .asGif()
                .load(R.raw.progressbar)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(iv_progress_loading);


        //tv_progress_message.setText(message);
    }

    public void SetShowLoadingPageMsg(Context context, String message) {

        if (mProgressDialog == null || !mProgressDialog.isShowing()) {
            return;
        }
        TextView tv_progress_message = (TextView) mProgressDialog.findViewById(R.id.tv_progress_message);
        if(message.isEmpty())
            message = "로딩중";

        ImageView iv_progress_loading = (ImageView) mProgressDialog.findViewById(R.id.iv_loading_logo);
        Glide.with(context)
                .asGif()
                .load(R.raw.progressbar)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(iv_progress_loading);
        //tv_progress_message.setText(message);
    }

    public void DismissLoadingPage() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public void ShowToast(Context context, String msg, boolean shortView)
    {
        if(shortView)
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }





    //////////////////////////////////////////////////////////////////////////////////////////////////
    /// 테스트 함수 //
    //////////////////////////////////////////////////////////////////////////////////////////////////

    // 더미 집어넣기
    public void AddDummy(int count)
    {
            for(int i=0; i< count; i++)
            {
                UserData tempData = new UserData();
                String[] fav = new String[3];
                fav[0] = "A";
                fav[1] = "1";
                fav[2] = "3";

                tempData.SetUserData(i, Integer.toString(i), Integer.toString(i), fav, Integer.toString(i), Integer.toString(i), i, 0);
                FirebaseManager.getInstance().SetUserDataOnFireBase(CommonData.CollentionType.USERS, Integer.toString(i), tempData);
                for(int j = 0; j<fav.length; j++)
                    FirebaseManager.getInstance().SetUserFavoriteOnFireBase(fav[j], Integer.toString(i), Integer.toString(i));
            }
    }

}
