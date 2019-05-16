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
    public int MainUserListSlotCount = 0;

    public void GoMainActivity(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        intent.setFlags(Intent. FLAG_ACTIVITY_SINGLE_TOP);
        activity.startActivity(intent);
        activity.finish();
    }

    public int GetMainSortTypeStrID(CommonData.MainSortType type)
    {
        switch (type)
        {
            case ALL:
                return R.string.bt_Main_Sort_Type_All;
            case ONLINE:
                return R.string.bt_Main_Sort_Type_Online;
        }

        return R.string.STR_ERROR;
    }

    public int GetWidthByDevice()
    {
        return mWidth;
    }

    public void SetWidthByDevice(int width)
    {
        mWidth = width;
    }

    public int GetHeightByDevice()
    {
        return mHeight;
    }

    public void SetHeightByDevice(int height)
    {
        mHeight = height;
    }

    public int convertPXtoDP(Resources res, int pixel)
    {
        DisplayMetrics dm = res.getDisplayMetrics();
        return Math.round(pixel * dm.density);
    }

}
