package fifty.fiftyhouse.com.fifty.util;

import android.app.Activity;

import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DialogFunc;
import fifty.fiftyhouse.com.fifty.MainActivity;
import fifty.fiftyhouse.com.fifty.R;

public class BackPressCloseHandler {
    private long backKeyPressedTime = 0;
    private Activity activity;
    public BackPressCloseHandler(Activity context) {
        this.activity = context;
    }
    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            showGuide();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {

            DialogFunc.MsgPopupListener listener = new DialogFunc.MsgPopupListener()
            {
                @Override
                public void Listener()
                {
                    activity.finish();
                    System.exit(0);
                    int pid = android.os.Process.myPid();
                    android.os.Process.killProcess(pid);
                }
            };
            DialogFunc.getInstance().ShowMsgPopup(activity, listener, null, CommonFunc.getInstance().getStr(activity.getResources(), R.string.MSG_APP_EXIT), CommonFunc.getInstance().getStr(activity.getResources(), R.string.MSG_EXIT), CommonFunc.getInstance().getStr(activity.getResources(), R.string.MSG_CANCEL));


        }
    }
        public void showGuide() {
        DialogFunc.getInstance().ShowToast(activity, "\'뒤로\'버튼을 한번 더 누르시면 종료됩니다.", true);
    }


}
