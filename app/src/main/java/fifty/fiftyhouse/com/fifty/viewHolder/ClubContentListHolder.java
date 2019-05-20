package fifty.fiftyhouse.com.fifty.viewHolder;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import fifty.fiftyhouse.com.fifty.R;

public class ClubContentListHolder extends RecyclerView.ViewHolder {

    public ImageView iv_Club_Con_Profile;
    public TextView tv_Club_Con_Nickname, tv_Club_Con_Date, tv_Club_Con_Desc;
    public View v_Club_Con_Reply_1, v_Club_Con_Reply_2;
    public ImageView iv_Club_Con_Reply_1_Profile, iv_Club_Con_Reply_2_Profile;
    public TextView tv_Club_Con_Reply_1_Nickname, tv_Club_Con_Reply_2_Nickname;
    public TextView tv_Club_Con_Reply_1_Date, tv_Club_Con_Reply_2_Date;
    public TextView tv_Club_Con_Reply_1_Desc, tv_Club_Con_Reply_2_Desc;

    public ClubContentListHolder(View itemView) {
        super(itemView);

        iv_Club_Con_Profile = itemView.findViewById(R.id.iv_Club_Con_Profile);
        tv_Club_Con_Nickname = itemView.findViewById(R.id.tv_Club_Con_Nickname);
        tv_Club_Con_Date = itemView.findViewById(R.id.tv_Club_Con_Date);
        tv_Club_Con_Desc = itemView.findViewById(R.id.tv_Club_Con_Desc);
        v_Club_Con_Reply_1 = itemView.findViewById(R.id.v_Club_Con_Reply_1);
        v_Club_Con_Reply_2 = itemView.findViewById(R.id.v_Club_Con_Reply_2);
        iv_Club_Con_Reply_1_Profile = itemView.findViewById(R.id.iv_Club_Con_Reply_1_Profile);
        iv_Club_Con_Reply_2_Profile = itemView.findViewById(R.id.iv_Club_Con_Reply_2_Profile);
        tv_Club_Con_Reply_1_Nickname = itemView.findViewById(R.id.tv_Club_Con_Reply_1_Nickname);
        tv_Club_Con_Reply_2_Nickname = itemView.findViewById(R.id.tv_Club_Con_Reply_2_Nickname);
        tv_Club_Con_Reply_1_Date = itemView.findViewById(R.id.tv_Club_Con_Reply_1_Date);
        tv_Club_Con_Reply_2_Date = itemView.findViewById(R.id.tv_Club_Con_Reply_2_Date);
        tv_Club_Con_Reply_1_Desc = itemView.findViewById(R.id.tv_Club_Con_Reply_1_Desc);
        tv_Club_Con_Reply_2_Desc = itemView.findViewById(R.id.tv_Club_Con_Reply_2_Desc);
    }
}