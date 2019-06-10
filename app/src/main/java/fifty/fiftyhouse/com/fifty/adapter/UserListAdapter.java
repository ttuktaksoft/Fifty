package fifty.fiftyhouse.com.fifty.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fifty.fiftyhouse.com.fifty.CommonData;
import fifty.fiftyhouse.com.fifty.DataBase.UserData;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.viewHolder.UserListListHolder;

public class UserListAdapter extends RecyclerView.Adapter<UserListListHolder> {

    Context mContext;
    int UserCount = 0;
    CommonData.MyProfileViewType UserType = CommonData.MyProfileViewType.VISIT;


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

        holder.SetItemByType(UserType, i);

        holder.setData(i);
    }

    @Override
    public int getItemCount() {
        return UserCount;
        //return  TKManager.getInstance().TargetUserData.GetUserImgCount();
        //return mMyData.arrChatTargetData.size();
    }

    public void SetItemCountByType(CommonData.MyProfileViewType type, int count)
    {
        UserType = type;
        UserCount = count;
    }

}