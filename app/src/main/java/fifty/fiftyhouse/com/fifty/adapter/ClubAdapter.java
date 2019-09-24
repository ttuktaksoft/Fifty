package fifty.fiftyhouse.com.fifty.adapter;

import android.content.Context;
import android.graphics.PorterDuff;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.Random;

import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DataBase.ClubData;
import fifty.fiftyhouse.com.fifty.DataBase.UserData;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.util.OnSingleClickListener;

public class ClubAdapter extends RecyclerView.Adapter<ClubAdapterHolder> {

    public interface ClubListener {
        void Listener(String key);
    }

    Context mContext;
    int mItemCount = 0;
    ArrayList<String[]> mItemList = new ArrayList<>();

    int DEFAULT_INDEX_TYPE = 0;
    int LAST_INDEX_TYPE = 1;

    ClubAdapter.ClubListener mListener;

    public ClubAdapter(Context context, ClubAdapter.ClubListener listener) {
        mContext = context;
        mListener = listener;
    }

    @Override
    public ClubAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_club, parent, false);

        ClubAdapterHolder holder = new ClubAdapterHolder(view);

        if(viewType == LAST_INDEX_TYPE)
        {
            holder.mPicType = ClubAdapterHolder.USER_BIG_PIC_TYPE.NO_BIG;
            view.setLayoutParams(new ConstraintLayout.LayoutParams(CommonFunc.getInstance().getWidthByDevice(),CommonFunc.getInstance().getWidthByDevice() / 3));
        }
        else
        {
            if(holder.mPicType == ClubAdapterHolder.USER_BIG_PIC_TYPE.START_BIG ||
                    holder.mPicType == ClubAdapterHolder.USER_BIG_PIC_TYPE.END_BIG)
            {
                view.setLayoutParams(new ConstraintLayout.LayoutParams(CommonFunc.getInstance().getWidthByDevice(),(CommonFunc.getInstance().getWidthByDevice() / 3) * 2));
            }
            else
                view.setLayoutParams(new ConstraintLayout.LayoutParams(CommonFunc.getInstance().getWidthByDevice(),CommonFunc.getInstance().getWidthByDevice() / 3));
        }


        return holder;
    }

    @Override
    public void onBindViewHolder(ClubAdapterHolder holder, final int position) {
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

class ClubAdapterHolder extends RecyclerView.ViewHolder{

    public enum USER_BIG_PIC_TYPE{
        START_BIG,
        END_BIG,
        NO_BIG
    }

    public USER_BIG_PIC_TYPE mPicType;
    ConstraintLayout v_club_slot_1, v_club_slot_2, v_club_slot_3;
    View ui_club_slot_1, ui_club_slot_2, ui_club_slot_3;
    Context mContext;

    public ClubAdapterHolder(View itemView) {
        super(itemView);
        initHolderImgType();
        mContext = itemView.getContext();

        v_club_slot_1 = itemView.findViewById(R.id.v_club_slot_1);
        v_club_slot_2 = itemView.findViewById(R.id.v_club_slot_2);
        v_club_slot_3 = itemView.findViewById(R.id.v_club_slot_3);
        ui_club_slot_1 = itemView.findViewById(R.id.ui_club_slot_1);
        ui_club_slot_2 = itemView.findViewById(R.id.ui_club_slot_2);
        ui_club_slot_3 = itemView.findViewById(R.id.ui_club_slot_3);
    }
    public void setData(String[] arr, ClubAdapter.ClubListener listener)
    {

        int bigSizeWidthPX = (CommonFunc.getInstance().getWidthByDevice() / 3) * 2;
        int smallSizeWidthPX = CommonFunc.getInstance().getWidthByDevice() / 3;
        int club_1_size = 0;
        int club_2_size = 0;
        int club_3_size = 0;

        switch (mPicType)
        {
            case START_BIG:
                club_1_size = bigSizeWidthPX;
                club_2_size = smallSizeWidthPX;
                club_3_size = smallSizeWidthPX;
                break;
            case END_BIG:
                club_1_size = smallSizeWidthPX;
                club_2_size = smallSizeWidthPX;
                club_3_size = bigSizeWidthPX;
                break;
            case NO_BIG:
                club_1_size = smallSizeWidthPX;
                club_2_size = smallSizeWidthPX;
                club_3_size = smallSizeWidthPX;
                break;
        }

        ConstraintLayout.LayoutParams lp_club_1 = null;
        ConstraintLayout.LayoutParams lp_club_2 = null;
        ConstraintLayout.LayoutParams lp_club_3 = null;

        if(mPicType == USER_BIG_PIC_TYPE.START_BIG)
        {
            lp_club_1 = new ConstraintLayout.LayoutParams(club_1_size, club_1_size);
            lp_club_1.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
            lp_club_1.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;

            lp_club_2 = new ConstraintLayout.LayoutParams(club_2_size, club_2_size);
            lp_club_2.leftToRight = v_club_slot_1.getId();
            lp_club_2.topToTop = v_club_slot_1.getId();

            lp_club_3 = new ConstraintLayout.LayoutParams(club_3_size, club_3_size);
            lp_club_3.topToBottom = v_club_slot_2.getId();
            lp_club_3.leftToLeft = v_club_slot_2.getId();
        }
        else if(mPicType == USER_BIG_PIC_TYPE.END_BIG)
        {
            lp_club_1 = new ConstraintLayout.LayoutParams(club_1_size, club_1_size);
            lp_club_1.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
            lp_club_1.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;

            lp_club_2 = new ConstraintLayout.LayoutParams(club_2_size, club_2_size);
            lp_club_2.leftToLeft = v_club_slot_1.getId();
            lp_club_2.topToBottom = v_club_slot_1.getId();

            lp_club_3 = new ConstraintLayout.LayoutParams(club_3_size, club_3_size);
            lp_club_3.topToTop = v_club_slot_1.getId();
            lp_club_3.leftToRight = v_club_slot_1.getId();
        }
        else
        {
            lp_club_1 = new ConstraintLayout.LayoutParams(club_1_size, club_1_size);
            lp_club_1.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
            lp_club_1.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;

            lp_club_2 = new ConstraintLayout.LayoutParams(club_2_size, club_2_size);
            lp_club_2.leftToRight = v_club_slot_1.getId();
            lp_club_2.topToTop = v_club_slot_1.getId();

            lp_club_3 = new ConstraintLayout.LayoutParams(club_3_size, club_3_size);
            lp_club_3.leftToRight = v_club_slot_2.getId();
            lp_club_3.topToTop = v_club_slot_2.getId();
        }

        v_club_slot_1.setLayoutParams(lp_club_1);
        v_club_slot_2.setLayoutParams(lp_club_2);
        v_club_slot_3.setLayoutParams(lp_club_3);

        ui_club_slot_1.setVisibility(View.GONE);
        ui_club_slot_2.setVisibility(View.GONE);
        ui_club_slot_3.setVisibility(View.GONE);
        ui_club_slot_1.setOnClickListener(null);
        ui_club_slot_2.setOnClickListener(null);
        ui_club_slot_3.setOnClickListener(null);

        if(arr.length >= 1)
            setUserData(arr[0], ui_club_slot_1, listener);
        if(arr.length >= 2)
            setUserData(arr[1], ui_club_slot_2, listener);
        if(arr.length >= 3)
            setUserData(arr[2], ui_club_slot_3, listener);
    }

    private void setUserData(String key, View view, ClubAdapter.ClubListener listener)
    {
        ImageView iv_Club_Profile, iv_Club_Tag, iv_Club_Master;
        TextView tv_Club_Name, tv_Club_Count;
        TextView tv_Club_Tag;

        if(CommonFunc.getInstance().CheckStringNull(key) == false)
        {
            view.setVisibility(View.VISIBLE);

            iv_Club_Profile = view.findViewById(R.id.iv_Club_Profile);
            tv_Club_Name = view.findViewById(R.id.tv_Club_Name);
            iv_Club_Tag = view.findViewById(R.id.iv_Club_Tag);
            tv_Club_Tag = view.findViewById(R.id.tv_Club_Tag);
            iv_Club_Master = view.findViewById(R.id.iv_Club_Master);
            tv_Club_Count = view.findViewById(R.id.tv_Club_Count);


            view.setOnClickListener(new OnSingleClickListener() {
                @Override
                public void onSingleClick(View view) {
                    listener.Listener(key);
                }
            });

            ClubData tempClubSimple = TKManager.getInstance().ClubData_Simple.get(key);

            tv_Club_Name.setText(tempClubSimple.ClubName);
            tv_Club_Count.setText(tempClubSimple.ClubMemberCount + "명");
            CommonFunc.getInstance().DrawImageByGlide(mContext, iv_Club_Profile, tempClubSimple.ClubThumbNail, false);

            iv_Club_Master.setVisibility(View.GONE);
            tv_Club_Tag.setText("");
            iv_Club_Tag.setVisibility(View.GONE);

            if(TKManager.getInstance().MyData.GetUserClubData(key) != null)
            {
                // 가입된 클럽
                if(CommonFunc.getInstance().CheckStringNull(tempClubSimple.GetClubMasterIndex()) == false)
                {
                    if(tempClubSimple.GetClubMasterIndex().equals(TKManager.getInstance().MyData.GetUserIndex()))
                        iv_Club_Master.setVisibility(View.VISIBLE);
                }
            }
            else if(TKManager.getInstance().MyData.GetRequestJoinClubList(key) != null)
            {
                // 가입 대기중
                tv_Club_Tag.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MSG_WAIT_APPROVAL));
                iv_Club_Tag.setVisibility(View.VISIBLE);
                iv_Club_Tag.setColorFilter(ContextCompat.getColor(mContext, R.color.request_club_tag), PorterDuff.Mode.MULTIPLY);
            }
            else if(TKManager.getInstance().MyData.GetUserRecommendClubData(key) != null)
            {
                // 추천
                tv_Club_Tag.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MSG_RECOMMEND));
                iv_Club_Tag.setVisibility(View.VISIBLE);
                iv_Club_Tag.setColorFilter(ContextCompat.getColor(mContext, R.color.recom_club_tag), PorterDuff.Mode.MULTIPLY);
            }
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