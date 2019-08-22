package fifty.fiftyhouse.com.fifty.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DataBase.UserData;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MainAdapterOneList extends ArrayAdapter<MainAdapterOneListHolder> implements ListAdapter{

    private final LayoutInflater layoutInflater;

    public ImageView iv_Main_Thumbnail, iv_Main_Gender, iv_Main_Thumbnail_Online_State;
    public TextView tv_Main_Dis, tv_Main_Thumbnail_Online_State;


    public MainAdapterOneList(Context context, List<MainAdapterOneListHolder> items) {
        super(context, 0, items);
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View v = layoutInflater.inflate(R.layout.adapter_main_one,parent,false);

        iv_Main_Thumbnail = v.findViewById(R.id.iv_Main_Thumbnail);
        iv_Main_Gender = v.findViewById(R.id.iv_Main_Gender);
        iv_Main_Thumbnail_Online_State = v.findViewById(R.id.iv_Main_Thumbnail_Online_State);
        tv_Main_Dis = v.findViewById(R.id.tv_Main_Dis);
        tv_Main_Thumbnail_Online_State = v.findViewById(R.id.tv_Main_Thumbnail_Online_State);

        UserData tempData = (UserData)(TKManager.getInstance().UserData_Simple.get(TKManager.getInstance().View_UserList_Dist.get(position)));

        String mUserName = null;
        String mUserDist = null;
        int mUserGender = 0;
        String mUserState = null;
        String[] mUserImg = new String[8];

        if(tempData.GetUserDist() < 1000)
        {
            mUserDist = "내 근처";
        }
        else if(tempData.GetUserDist() > 1000 * 100)
        {
            mUserDist = "100km 이상";
        }
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
            Glide.with(getContext()).load(R.drawable.ic_man_simple)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(iv_Main_Gender);
        } else {
            Glide.with(getContext()).load(R.drawable.ic_woman_simple)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(iv_Main_Gender);
        }

        if(CommonFunc.getInstance().CheckStringNull(mUserImg[0]) == false)
        {
            Glide.with(getContext()).load(mUserImg[0])
                    .centerCrop()
                    .into(iv_Main_Thumbnail);
        }
        else
            CommonFunc.getInstance().DrawImageByGlide(getContext(), iv_Main_Thumbnail, R.drawable.bg_empty_square, false);

        return v;
    }

    @Override public int getViewTypeCount() {
        return 2;
    }

    @Override public int getItemViewType(int position) {
        return position % 2 == 0 ? 1 : 0;
    }

    public void appendItems(List<MainAdapterOneListHolder> newItems) {
        addAll(newItems);
        notifyDataSetChanged();
    }

    public void setItems(List<MainAdapterOneListHolder> moreItems) {
        clear();
        appendItems(moreItems);
    }
}