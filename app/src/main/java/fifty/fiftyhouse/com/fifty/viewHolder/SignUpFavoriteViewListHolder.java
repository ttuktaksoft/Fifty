package fifty.fiftyhouse.com.fifty.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;

public class SignUpFavoriteViewListHolder extends RecyclerView.ViewHolder {

    TextView tv_SignUp_Favorite_View_Name;
    public SignUpFavoriteViewListHolder(View itemView) {
        super(itemView);
        tv_SignUp_Favorite_View_Name = itemView.findViewById(R.id.tv_SignUp_Favorite_View_Name);
    }

    public void setData(int i)
    {
        tv_SignUp_Favorite_View_Name.setText(TKManager.getInstance().FavoriteLIst_Pop.get(i));
        /*
        if(i == 0)
            tv_SignUp_Favorite_View_Name.setText(TKManager.getInstance().FavoriteLIst_Pop.get(i));
        else if(i == 1)
            tv_SignUp_Favorite_View_Name.setText("배구");
        else if(i == 2)
            tv_SignUp_Favorite_View_Name.setText("탁구");
        else if(i == 3)
            tv_SignUp_Favorite_View_Name.setText("요가");
        else if(i == 4)
            tv_SignUp_Favorite_View_Name.setText("마라톤");
        else
            tv_SignUp_Favorite_View_Name.setText("축구");*/
    }
}