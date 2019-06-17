package fifty.fiftyhouse.com.fifty.util;

import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;

public abstract class OnSingleTouchListener implements View.OnTouchListener {
    // 중복 클릭 방지 시간 설정
    private static final long MIN_CLICK_INTERVAL=600;

    private long mLastClickTime;

    public abstract void onSingleTouch(View v);

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        long currentClickTime=SystemClock.uptimeMillis();
        long elapsedTime=currentClickTime-mLastClickTime;
        mLastClickTime=currentClickTime;

        // 중복 클릭인 경우
        if(elapsedTime<=MIN_CLICK_INTERVAL){
            return false;
        }

        // 중복 클릭아 아니라면 추상함수 호출
        onSingleTouch(v);
        return  false;
    }
}
