package fifty.fiftyhouse.com.fifty.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Iterator;

import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DataBase.UserData;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;

public class UserListAdapter extends RecyclerView.Adapter<UserListListHolder> {

    Context mContext;
    int mItemCount = 0;
    ArrayList<Pair<String, Integer>> mItemList = new ArrayList<>();

    public static int CLUB_MATSER_VIEW = 1;

    public UserListAdapter(Context context) {
        mContext = context;
    }

    @Override
    public UserListListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_user_list, parent, false);

        return new UserListListHolder(view);
    }



    @Override
    public void onBindViewHolder(UserListListHolder holder, final int position) {
        int i = position;

        holder.setData(i, mItemList.get(i));
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
        Iterator iterator = list.iterator();
        while (iterator.hasNext()) {
            String element = (String) iterator.next();
            mItemList.add(new Pair<String, Integer>(element, 0));
        }
    }

    public void setSpecialItemData(ArrayList<Pair<String, Integer>> list)
    {
        mItemList.clear();
        mItemList.addAll(list);
    }
}

class UserListListHolder extends RecyclerView.ViewHolder {

    ImageView iv_Slot_BG, iv_User_List_Profile, iv_User_List_Gender, iv_User_List_Etc;
    TextView tv_User_List_Name, tv_User_List_Age, tv_User_List_Dis;
    Context mContext;

    public UserListListHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();

        iv_Slot_BG = itemView.findViewById(R.id.iv_Slot_BG);
        iv_User_List_Profile = itemView.findViewById(R.id.iv_User_List_Profile);
        iv_User_List_Gender = itemView.findViewById(R.id.iv_User_List_Gender);
        tv_User_List_Name = itemView.findViewById(R.id.tv_User_List_Name);
        tv_User_List_Age = itemView.findViewById(R.id.tv_User_List_Age);
        tv_User_List_Dis = itemView.findViewById(R.id.tv_User_List_Dis);
        iv_User_List_Etc = itemView.findViewById(R.id.iv_User_List_Etc);

    }

    public void setData(int index, Pair<String, Integer> key)
    {
        CommonFunc.getInstance().SetSlotBG(index, iv_Slot_BG, mContext);

        UserData data = TKManager.getInstance().UserData_Simple.get(key.first);

        Glide.with(mContext).load(data.GetUserImgThumb())
                .centerCrop()
                .circleCrop()
                .into(iv_User_List_Profile);


        if (data.GetUserGender() == 0) {
            Glide.with(mContext).load(R.drawable.ic_man_simple)
                    .centerCrop()
                    .into(iv_User_List_Gender);

        } else {
            Glide.with(mContext).load(R.drawable.ic_woman_simple)
                    .centerCrop()
                    .into(iv_User_List_Gender);
        }

        tv_User_List_Name.setText(data.GetUserNickName());
        tv_User_List_Age.setText(data.GetUserAge() + CommonFunc.getInstance().getStr(mContext.getResources(),R.string.MSG_AGE_END));


        if(data.GetUserDist() < 1000)
        {
            tv_User_List_Dis.setText("내 근처");
        }
        else if(data.GetUserDist() > 1000 * 100)
        {
            tv_User_List_Dis.setText("100" + CommonFunc.getInstance().getStr(mContext.getResources(),R.string.DEFAULT_DISTANCE) + "이상");
        }
        else
        {
            tv_User_List_Dis.setText((int)(data.GetUserDist()  / 1000) + CommonFunc.getInstance().getStr(mContext.getResources(),R.string.DEFAULT_DISTANCE));
        }

        iv_User_List_Etc.setVisibility(View.GONE);

        if(key.second == UserListAdapter.CLUB_MATSER_VIEW)
        {
            iv_User_List_Etc.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(R.drawable.ic_medal)
                    .centerCrop()
                    .into(iv_User_List_Etc);
        }
    }
}
