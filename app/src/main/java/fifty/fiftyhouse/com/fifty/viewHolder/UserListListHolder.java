package fifty.fiftyhouse.com.fifty.viewHolder;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import fifty.fiftyhouse.com.fifty.CommonData;
import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DataBase.UserData;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;

public class UserListListHolder extends RecyclerView.ViewHolder {

    ImageView iv_User_List_Profile, iv_User_List_Gender;
    TextView tv_User_List_Name, tv_User_List_Age, tv_User_List_Dis;
    Context mContext;

    String UserNickName,UserImgThumb , UserDist;
    int UserAge, UserGender;

    public UserListListHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();

        iv_User_List_Profile = itemView.findViewById(R.id.iv_User_List_Profile);
        iv_User_List_Gender = itemView.findViewById(R.id.iv_User_List_Gender);
        tv_User_List_Name = itemView.findViewById(R.id.tv_User_List_Name);
        tv_User_List_Age = itemView.findViewById(R.id.tv_User_List_Age);
        tv_User_List_Dis = itemView.findViewById(R.id.tv_User_List_Dis);
    }

    public void setData(int i)
    {

            Glide.with(mContext).load(UserImgThumb)
                    .centerCrop()
                    .circleCrop()
                    .into(iv_User_List_Profile);


        if (UserGender == 0) {
            Glide.with(mContext).load(R.drawable.ic_man_simple)
                    .centerCrop()
                    .into(iv_User_List_Gender);

        } else {
            Glide.with(mContext).load(R.drawable.ic_woman_simple)
                    .centerCrop()
                    .into(iv_User_List_Gender);
        }

            tv_User_List_Name.setText(UserNickName);
            tv_User_List_Age.setText(UserAge + "세");
            //tv_User_List_Dis.setText(UserDist);
        tv_User_List_Dis.setText("1Km");
    }

    public void SetItemByType(CommonData.MyProfileViewType type, int position)
    {
        UserData tempData = new UserData();
        Set tempKey = null;
        List array = new ArrayList();

        switch (type)
        {
            case VISIT:
                tempKey =  TKManager.getInstance().MyData.GetUserVisitKeySet();
                break;

            case LIKE:
                tempKey = TKManager.getInstance().MyData.GetUserLikeKeySet();
                break;

            case FRIEND:
                tempKey = TKManager.getInstance().MyData.GetUserFriendListKeySet();
                break;

        }

        array = new ArrayList(tempKey);

        UserNickName = TKManager.getInstance().MyData_Simple.get(array.get(position).toString()).GetUserNickName();
        UserImgThumb = TKManager.getInstance().MyData_Simple.get(array.get(position).toString()).GetUserImgThumb();

        UserAge = TKManager.getInstance().MyData_Simple.get(array.get(position).toString()).GetUserAge();
        UserGender = TKManager.getInstance().MyData_Simple.get(array.get(position).toString()).GetUserGender();


    }
}
