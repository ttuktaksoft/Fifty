package fifty.fiftyhouse.com.fifty;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.util.DisplayMetrics;

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
        DisplayMetrics dm = res.getDisplayMetrics();
        return Math.round(pixel * dm.density);
    }

    public String getStr(Resources res, int id)
    {
        if(res == null)
            return "ERROR";

        return res.getString(id);
    }

}
