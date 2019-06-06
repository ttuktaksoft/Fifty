package fifty.fiftyhouse.com.fifty.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;

public class FavoriteEditSelectListHolder extends RecyclerView.ViewHolder {

    TextView tv_Favorite_Edit_Select_Name;

    public FavoriteEditSelectListHolder(View itemView) {
        super(itemView);

        tv_Favorite_Edit_Select_Name = itemView.findViewById(R.id.tv_Favorite_Edit_Select_Name);
    }

    public void setData(int i)
    {
        if(i == 0)
            tv_Favorite_Edit_Select_Name.setText("야구");
        else
            tv_Favorite_Edit_Select_Name.setText("축구");
    }
}