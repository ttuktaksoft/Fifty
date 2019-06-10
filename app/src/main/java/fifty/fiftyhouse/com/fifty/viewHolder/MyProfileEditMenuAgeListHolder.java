package fifty.fiftyhouse.com.fifty.viewHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;

public class MyProfileEditMenuAgeListHolder extends RecyclerView.ViewHolder {
    ImageView tv_MyProfile_Edit_Age_Menu_Gender;
    TextView tv_MyProfile_Edit_Age_Menu_Age;
    Context mContext;

    public MyProfileEditMenuAgeListHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();

        tv_MyProfile_Edit_Age_Menu_Gender = itemView.findViewById(R.id.tv_MyProfile_Edit_Age_Menu_Gender);
        tv_MyProfile_Edit_Age_Menu_Age = itemView.findViewById(R.id.tv_MyProfile_Edit_Age_Menu_Age);


    }

    public void setData()
    {
        if (true) {
            Glide.with(mContext).load(R.drawable.ic_man_simple)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(tv_MyProfile_Edit_Age_Menu_Gender);
        } else {
            Glide.with(mContext).load(R.drawable.ic_woman_simple)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(tv_MyProfile_Edit_Age_Menu_Gender);
        }
        tv_MyProfile_Edit_Age_Menu_Age.setText(TKManager.getInstance().MyData.GetUserAge() + "세");
    }
}
