package fifty.fiftyhouse.com.fifty.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;

public class FavoriteEditViewListHolder extends RecyclerView.ViewHolder {

    TextView tv_Favorite_Edit_View_Name;
    public FavoriteEditViewListHolder(View itemView) {
        super(itemView);
        tv_Favorite_Edit_View_Name = itemView.findViewById(R.id.tv_Favorite_Edit_View_Name);
    }

    public void setData(int i)
    {
        if(i == 0)
            tv_Favorite_Edit_View_Name.setText("축구");
        else if(i == 1)
            tv_Favorite_Edit_View_Name.setText("배구");
        else if(i == 2)
            tv_Favorite_Edit_View_Name.setText("탁구");
        else if(i == 3)
            tv_Favorite_Edit_View_Name.setText("요가");
        else if(i == 4)
            tv_Favorite_Edit_View_Name.setText("마라톤");
        else
            tv_Favorite_Edit_View_Name.setText("축구");
    }
}