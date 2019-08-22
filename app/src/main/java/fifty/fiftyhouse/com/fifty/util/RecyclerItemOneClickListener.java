package fifty.fiftyhouse.com.fifty.util;

import android.os.SystemClock;
import android.view.View;
import android.widget.AdapterView;

abstract public class RecyclerItemOneClickListener implements AdapterView.OnItemClickListener {

    // 중복 클릭 방지 시간 설정
    private static final long MIN_CLICK_INTERVAL=600;

    private long mLastClickTime;

    public abstract void RecyclerItemOneClick(int position);

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        long currentClickTime=SystemClock.uptimeMillis();
        long elapsedTime=currentClickTime-mLastClickTime;
        mLastClickTime=currentClickTime;

        // 중복 클릭인 경우
        if(elapsedTime<=MIN_CLICK_INTERVAL){
            return;
        }

        // 중복 클릭아 아니라면 추상함수 호출
        RecyclerItemOneClick(position);
    }
}
