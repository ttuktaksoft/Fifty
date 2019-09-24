package fifty.fiftyhouse.com.fifty.adapter;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import fifty.fiftyhouse.com.fifty.CommonData;
import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DataBase.UserData;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.util.OnSingleClickListener;
import okhttp3.internal.Util;

public class MainUserAdapter extends RecyclerView.Adapter<MainUserAdapterHolder> {

    public interface MainUserListener {
        void Listener(String key);
    }

    Context mContext;
    int mItemCount = 0;
    ArrayList<String[]> mItemList = new ArrayList<>();

    int DEFAULT_INDEX_TYPE = 0;
    int LAST_INDEX_TYPE = 1;

    MainUserListener mListener;

    public MainUserAdapter(Context context, MainUserListener listener) {
        mContext = context;
        mListener = listener;
    }

    @Override
    public MainUserAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_main_user, parent, false);

        MainUserAdapterHolder holder = new MainUserAdapterHolder(view);

        if(viewType == LAST_INDEX_TYPE)
        {
            holder.mPicType = MainUserAdapterHolder.USER_BIG_PIC_TYPE.NO_BIG;
            view.setLayoutParams(new ConstraintLayout.LayoutParams(CommonFunc.getInstance().getWidthByDevice(),CommonFunc.getInstance().getWidthByDevice() / 3));
        }
        else
        {
            if(holder.mPicType == MainUserAdapterHolder.USER_BIG_PIC_TYPE.START_BIG ||
                    holder.mPicType == MainUserAdapterHolder.USER_BIG_PIC_TYPE.END_BIG)
            {
                view.setLayoutParams(new ConstraintLayout.LayoutParams(CommonFunc.getInstance().getWidthByDevice(),(CommonFunc.getInstance().getWidthByDevice() / 3) * 2));
            }
            else
                view.setLayoutParams(new ConstraintLayout.LayoutParams(CommonFunc.getInstance().getWidthByDevice(),CommonFunc.getInstance().getWidthByDevice() / 3));
        }


        return holder;
    }

    @Override
    public void onBindViewHolder(MainUserAdapterHolder holder, final int position) {
        int i = position;

        holder.setData(mItemList.get(i), mListener);
    }

    @Override
    public long getItemId(int position) {
        long rtValue = 0;
        return position;
    }


    @Override
    public int getItemCount() {
        return mItemCount;
    }

    public void setItemCount(int count)
    {
        mItemCount = count;
    }

    public void setItemData(ArrayList<String> list)
    {
        mItemList.clear();
        int cutSize = 0;
        String[] keyArr = new String[3];

        for (String key : list)
        {
            if(cutSize == 3)
            {
                mItemList.add(keyArr);
                keyArr = new String[3];
                cutSize = 0;
            }
            keyArr[cutSize] = key;
            cutSize++;
        }

        mItemList.add(keyArr);
        mItemCount = mItemList.size();
    }

    @Override
    public int getItemViewType(int position) {
        // Just as an example, return 0 or 2 depending on position
        // Note that unlike in ListView adapters, types don't have to be contiguous
        return position == (mItemCount - 1) ? LAST_INDEX_TYPE : DEFAULT_INDEX_TYPE;
    }
}

class MainUserAdapterHolder extends RecyclerView.ViewHolder{

    public enum USER_BIG_PIC_TYPE{
        START_BIG,
        END_BIG,
        NO_BIG
    }

    public USER_BIG_PIC_TYPE mPicType;
    ConstraintLayout v_main_user_1, v_main_user_2, v_main_user_3;
    View ui_main_user_1, ui_main_user_2, ui_main_user_3;
    Context mContext;

    public MainUserAdapterHolder(View itemView) {
        super(itemView);
        initHolderImgType();
        mContext = itemView.getContext();

        v_main_user_1 = itemView.findViewById(R.id.v_main_user_1);
        v_main_user_2 = itemView.findViewById(R.id.v_main_user_2);
        v_main_user_3 = itemView.findViewById(R.id.v_main_user_3);
        ui_main_user_1 = itemView.findViewById(R.id.ui_main_user_1);
        ui_main_user_2 = itemView.findViewById(R.id.ui_main_user_2);
        ui_main_user_3 = itemView.findViewById(R.id.ui_main_user_3);
    }
    public void setData(String[] arr, MainUserAdapter.MainUserListener listener)
    {

        int bigSizeWidthPX = (CommonFunc.getInstance().getWidthByDevice() / 3) * 2;
        int smallSizeWidthPX = CommonFunc.getInstance().getWidthByDevice() / 3;
        int user_1_size = 0;
        int user_2_size = 0;
        int user_3_size = 0;

        switch (mPicType)
        {
            case START_BIG:
                user_1_size = bigSizeWidthPX;
                user_2_size = smallSizeWidthPX;
                user_3_size = smallSizeWidthPX;
                break;
            case END_BIG:
                user_1_size = smallSizeWidthPX;
                user_2_size = smallSizeWidthPX;
                user_3_size = bigSizeWidthPX;
                break;
            case NO_BIG:
                user_1_size = smallSizeWidthPX;
                user_2_size = smallSizeWidthPX;
                user_3_size = smallSizeWidthPX;
                break;
        }

        ConstraintLayout.LayoutParams lp_user_1 = null;
        ConstraintLayout.LayoutParams lp_user_2 = null;
        ConstraintLayout.LayoutParams lp_user_3 = null;

        if(mPicType == USER_BIG_PIC_TYPE.START_BIG)
        {
            lp_user_1 = new ConstraintLayout.LayoutParams(user_1_size, user_1_size);
            lp_user_1.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
            lp_user_1.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;

            lp_user_2 = new ConstraintLayout.LayoutParams(user_2_size, user_2_size);
            lp_user_2.leftToRight = v_main_user_1.getId();
            lp_user_2.topToTop = v_main_user_1.getId();

            lp_user_3 = new ConstraintLayout.LayoutParams(user_3_size, user_3_size);
            lp_user_3.topToBottom = v_main_user_2.getId();
            lp_user_3.leftToLeft = v_main_user_2.getId();
        }
        else if(mPicType == USER_BIG_PIC_TYPE.END_BIG)
        {
            lp_user_1 = new ConstraintLayout.LayoutParams(user_1_size, user_1_size);
            lp_user_1.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
            lp_user_1.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;

            lp_user_2 = new ConstraintLayout.LayoutParams(user_2_size, user_2_size);
            lp_user_2.leftToLeft = v_main_user_1.getId();
            lp_user_2.topToBottom = v_main_user_1.getId();

            lp_user_3 = new ConstraintLayout.LayoutParams(user_3_size, user_3_size);
            lp_user_3.topToTop = v_main_user_1.getId();
            lp_user_3.leftToRight = v_main_user_1.getId();
        }
        else
        {
            lp_user_1 = new ConstraintLayout.LayoutParams(user_1_size, user_1_size);
            lp_user_1.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
            lp_user_1.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;

            lp_user_2 = new ConstraintLayout.LayoutParams(user_2_size, user_2_size);
            lp_user_2.leftToRight = v_main_user_1.getId();
            lp_user_2.topToTop = v_main_user_1.getId();

            lp_user_3 = new ConstraintLayout.LayoutParams(user_3_size, user_3_size);
            lp_user_3.leftToRight = v_main_user_2.getId();
            lp_user_3.topToTop = v_main_user_2.getId();
        }

        v_main_user_1.setLayoutParams(lp_user_1);
        v_main_user_2.setLayoutParams(lp_user_2);
        v_main_user_3.setLayoutParams(lp_user_3);

        ui_main_user_1.setVisibility(View.GONE);
        ui_main_user_2.setVisibility(View.GONE);
        ui_main_user_3.setVisibility(View.GONE);
        ui_main_user_1.setOnClickListener(null);
        ui_main_user_2.setOnClickListener(null);
        ui_main_user_3.setOnClickListener(null);

        if(arr.length >= 1)
            setUserData(arr[0], ui_main_user_1, listener);
        if(arr.length >= 2)
            setUserData(arr[1], ui_main_user_2, listener);
        if(arr.length >= 3)
            setUserData(arr[2], ui_main_user_3, listener);
    }

    private void setUserData(String key, View view, MainUserAdapter.MainUserListener listener)
    {
        ImageView iv_Main_Thumbnail, iv_Main_Gender, iv_Main_Thumbnail_Online_State;
        TextView tv_Main_Dis, tv_Main_Thumbnail_Online_State;

        if(CommonFunc.getInstance().CheckStringNull(key) == false)
        {
            view.setVisibility(View.VISIBLE);

            iv_Main_Thumbnail = view.findViewById(R.id.iv_Main_Thumbnail);
            iv_Main_Gender = view.findViewById(R.id.iv_Main_Gender);
            iv_Main_Thumbnail_Online_State = view.findViewById(R.id.iv_Main_Thumbnail_Online_State);
            tv_Main_Dis = view.findViewById(R.id.tv_Main_Dis);
            tv_Main_Thumbnail_Online_State = view.findViewById(R.id.tv_Main_Thumbnail_Online_State);


            view.setOnClickListener(new OnSingleClickListener() {
                @Override
                public void onSingleClick(View view) {
                    listener.Listener(key);
                }
            });
            UserData tempData = (UserData)(TKManager.getInstance().UserData_Simple.get(key));

            String mUserDist = null;
            int mUserGender = 0;
            String mUserState = null;
            String[] mUserImg = new String[8];

            if(tempData.GetUserDist() < 1000)
            {
                mUserDist = "내 근처";
            }
           /* else if(tempData.GetUserDist() > 1000 * 100)
            {
                mUserDist = "100km 이상";
            }*/
            else
            {
                mUserDist = (int)(tempData.GetUserDist()  / 1000) + "km";
            }

            for(int i = 0 ; i< 8 ; i++)
                mUserImg[i] = null;

            for(int i = 0; i < tempData.GetUserImgCount(); i++)
            {
                mUserImg[i] = tempData.GetUserImg(Integer.toString(i));
            }

            mUserGender = tempData.GetUserGender();
            mUserState = CommonFunc.getInstance().GetConnectGap(CommonFunc.getInstance().GetCurrentTime(), Long.toString(tempData.GetUserConnectDate()));

            tv_Main_Dis.setText(mUserDist);
            tv_Main_Thumbnail_Online_State.setText(mUserState);


            if (mUserGender == 0) {
                Glide.with(mContext).load(R.drawable.ic_man_simple)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(iv_Main_Gender);
            } else {
                Glide.with(mContext).load(R.drawable.ic_woman_simple)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(iv_Main_Gender);
            }

            if(CommonFunc.getInstance().CheckStringNull(mUserImg[0]) == false)
            {
                Glide.with(mContext).load(mUserImg[0])
                        .centerCrop()
                        .into(iv_Main_Thumbnail);
            }
            else
                CommonFunc.getInstance().DrawImageByGlide(mContext, iv_Main_Thumbnail, R.drawable.bg_empty_square, false);
        }
    }

    public void initHolderImgType()
    {
        Random ran = new Random();
        int typeIndex = ran.nextInt(3);
        if(typeIndex == 0)
            mPicType = USER_BIG_PIC_TYPE.START_BIG;
        else if(typeIndex == 1)
            mPicType = USER_BIG_PIC_TYPE.END_BIG;
        else
            mPicType = USER_BIG_PIC_TYPE.NO_BIG;
    }
}
