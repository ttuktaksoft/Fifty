package fifty.fiftyhouse.com.fifty.viewHolder;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;

public class UserListListHolder extends RecyclerView.ViewHolder {

    ImageView iv_User_List_Profile, iv_User_List_Gender;
    TextView tv_User_List_Name, tv_User_List_Age, tv_User_List_Dis;
    Context mContext;

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
        if(i == 0)
        {
            Glide.with(mContext).load(R.drawable.dummy_6)
                    .centerCrop()
                    .into(iv_User_List_Profile);

            Glide.with(mContext).load(R.drawable.ic_woman_simple)
                    .centerCrop()
                    .into(iv_User_List_Gender);

            tv_User_List_Name.setText("안녕하세요.");
            tv_User_List_Age.setText("233세");
            tv_User_List_Dis.setText("12344km");
        }
        else
        {
            Glide.with(mContext).load(R.drawable.dummy_1)
                    .centerCrop()
                    .into(iv_User_List_Profile);

            Glide.with(mContext).load(R.drawable.ic_man_simple)
                    .centerCrop()
                    .into(iv_User_List_Gender);

            tv_User_List_Name.setText("안녕하세요.2222");
            tv_User_List_Age.setText("555세");
            tv_User_List_Dis.setText("123144km");
        }
    }
}
