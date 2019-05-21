package fifty.fiftyhouse.com.fifty.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import fifty.fiftyhouse.com.fifty.R;
public class MainUserListHolder extends RecyclerView.ViewHolder{

    public ImageView iv_Main_Thumbnail_1,iv_Main_Thumbnail_2,iv_Main_Thumbnail_3;
    public TextView tv_Main_Thumbnail_Info_NickName, tv_Main_Thumbnail_Info_State;

    public MainUserListHolder(View itemView) {
        super(itemView);
        //v_Main_Thumbnail = itemView.findViewById(R.id.v_Main_Thumbnail);
        iv_Main_Thumbnail_1 = itemView.findViewById(R.id.iv_Main_Thumbnail_1);
        iv_Main_Thumbnail_2 = itemView.findViewById(R.id.iv_Main_Thumbnail_2);
        iv_Main_Thumbnail_3 = itemView.findViewById(R.id.iv_Main_Thumbnail_3);
        tv_Main_Thumbnail_Info_NickName = itemView.findViewById(R.id.tv_Main_Thumbnail_Info_NickName);
        tv_Main_Thumbnail_Info_State = itemView.findViewById(R.id.tv_Main_Thumbnail_Info_State);
    }
}
