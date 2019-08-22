package fifty.fiftyhouse.com.fifty.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DataBase.ClubData;
import fifty.fiftyhouse.com.fifty.DataBase.UserData;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;

public class CustomClubAdapter extends ArrayAdapter<CustomGridListHolder> implements ListAdapter {

    private final LayoutInflater layoutInflater;

    public ImageView iv_Club_Profile, iv_Club_Tag, iv_Club_Master;
    public TextView tv_Club_Name, tv_Club_Count;
    public TextView tv_Club_Tag;
    Context mContext;
    private ArrayList<String> mCreate = new ArrayList<>();


    public CustomClubAdapter(Context context, List<CustomGridListHolder> items) {
        super(context, 0, items);
        layoutInflater = LayoutInflater.from(context);
        mCreate.clear();
    }

    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        mContext = getContext();
        View v = layoutInflater.inflate(R.layout.adapter_club,parent,false);

        iv_Club_Profile = v.findViewById(R.id.iv_Club_Profile);
        tv_Club_Name = v.findViewById(R.id.tv_Club_Name);
        iv_Club_Tag = v.findViewById(R.id.iv_Club_Tag);
        tv_Club_Tag = v.findViewById(R.id.tv_Club_Tag);
        iv_Club_Master = v.findViewById(R.id.iv_Club_Master);
        tv_Club_Count = v.findViewById(R.id.tv_Club_Count);

        String clubKey = getItem(position).key;
        ClubData tempClubSimple = TKManager.getInstance().ClubData_Simple.get(clubKey);

        tv_Club_Name.setText(tempClubSimple.ClubName);
        tv_Club_Count.setText(tempClubSimple.ClubMemberCount + "명");
        CommonFunc.getInstance().DrawImageByGlide(mContext, iv_Club_Profile, tempClubSimple.ClubThumbNail, false);

        iv_Club_Master.setVisibility(View.GONE);
        tv_Club_Tag.setText("");
        iv_Club_Tag.setVisibility(View.GONE);

        if(TKManager.getInstance().MyData.GetUserClubData(clubKey) != null)
        {
            // 가입된 클럽
            if(CommonFunc.getInstance().CheckStringNull(tempClubSimple.GetClubMasterIndex()) == false)
            {
                if(tempClubSimple.GetClubMasterIndex().equals(TKManager.getInstance().MyData.GetUserIndex()))
                    iv_Club_Master.setVisibility(View.VISIBLE);
            }
        }
        else if(TKManager.getInstance().MyData.GetRequestJoinClubList(clubKey) != null)
        {
            // 가입 대기중
            tv_Club_Tag.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MSG_REQUEST_JOIN_CLUB));
            iv_Club_Tag.setVisibility(View.VISIBLE);
            iv_Club_Tag.setColorFilter(ContextCompat.getColor(mContext, R.color.request_club_tag), PorterDuff.Mode.MULTIPLY);
        }
        else if(TKManager.getInstance().MyData.GetUserRecommendClubData(clubKey) != null)
        {
            // 추천
            tv_Club_Tag.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MSG_RECOM_CLUB));
            iv_Club_Tag.setVisibility(View.VISIBLE);
            iv_Club_Tag.setColorFilter(ContextCompat.getColor(mContext, R.color.recom_club_tag), PorterDuff.Mode.MULTIPLY);
        }

        return v;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void appendItems(List<CustomGridListHolder> newItems) {
        addAll(newItems);
        notifyDataSetChanged();
    }

    public void setItems(List<CustomGridListHolder> moreItems) {
        clear();
        appendItems(moreItems);
    }
}
