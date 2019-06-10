package fifty.fiftyhouse.com.fifty.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import fifty.fiftyhouse.com.fifty.R;

public class MyPofileFavoriteListHolder extends RecyclerView.ViewHolder{

    TextView tv_MyProfile_Favorite_Name;

    public MyPofileFavoriteListHolder(View itemView) {
        super(itemView);

        tv_MyProfile_Favorite_Name = itemView.findViewById(R.id.tv_MyProfile_Favorite_Name);
    }

    public void setData(int i)
    {
        if(i == 0)
            tv_MyProfile_Favorite_Name.setText("축구");
        else
            tv_MyProfile_Favorite_Name.setText("등산");
    }
}
