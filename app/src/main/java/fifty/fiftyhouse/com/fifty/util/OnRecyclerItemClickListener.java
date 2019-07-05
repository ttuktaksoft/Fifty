package fifty.fiftyhouse.com.fifty.util;

import android.os.SystemClock;
import android.view.View;

public abstract class OnRecyclerItemClickListener implements RecyclerItemClickListener.OnItemClickListener{
    // 중복 클릭 방지 시간 설정
    private static final long MIN_CLICK_INTERVAL=600;

    private long mLastClickTime;

    public abstract void onSingleClick(View v, int position);

    @Override
    public void onItemClick(View view, int position) {
        long currentClickTime=SystemClock.uptimeMillis();
        long elapsedTime=currentClickTime-mLastClickTime;
        mLastClickTime=currentClickTime;

        // 중복 클릭인 경우
        if(elapsedTime<=MIN_CLICK_INTERVAL){
            return;
        }

        // 중복 클릭아 아니라면 추상함수 호출
        onSingleClick(view, position);
    }

    @Override
    public void onLongItemClick(View view, int position) {
        //  Toast.makeText(getApplicationContext(),position+"번 째 아이템 롱 클릭",Toast.LENGTH_SHORT).show();
    }
}