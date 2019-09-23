package fifty.fiftyhouse.com.fifty.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import fifty.fiftyhouse.com.fifty.CommonData;
import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DataBase.UserData;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;

public class ClubMiniUserAdapter extends RecyclerView.Adapter<ClubMiniUserListHolder> {

    Context mContext;
    int mItemCount;
    ArrayList<String> mItemData = new ArrayList<>();

    public ClubMiniUserAdapter(Context context) {
        mContext = context;
    }

    @Override
    public ClubMiniUserListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_club_mini_user, parent, false);

        return new ClubMiniUserListHolder(view);
    }

    @Override
    public void onBindViewHolder(ClubMiniUserListHolder holder, final int position) {
        int i = position;
        holder.setData(mItemData.get(i));
    }

    @Override
    public int getItemCount() {
        if(mItemCount >= CommonData.CLUB_MINI_USER_MAX_VIEW)
            return CommonData.CLUB_MINI_USER_MAX_VIEW;
        else
            return mItemCount;
    }

    public void setItemData(ArrayList<String> list)
    {
        mItemData.clear();
        mItemData.addAll(list);
        mItemCount = mItemData.size();
    }

    public ClubMiniUserListHolderDecorator getDecorator()
    {
        ClubMiniUserListHolderDecorator itemDecorator = new ClubMiniUserListHolderDecorator(CommonFunc.getInstance().convertDPtoPX(mContext.getResources(), -15));
        return itemDecorator;
    }
}

class ClubMiniUserListHolder extends RecyclerView.ViewHolder {

    ImageView iv_Club_Mini_User_Menu, iv_Club_Mini_User;
    Context mContext;

    public ClubMiniUserListHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();

        iv_Club_Mini_User_Menu = itemView.findViewById(R.id.iv_Club_Mini_User_Menu);
        iv_Club_Mini_User = itemView.findViewById(R.id.iv_Club_Mini_User);
    }

    public void setData(String key)
    {
        if(key.equals("emtpy_user"))
        {
            iv_Club_Mini_User_Menu.setVisibility(View.VISIBLE);
            iv_Club_Mini_User.setVisibility(View.GONE);
        }
        else
        {
            iv_Club_Mini_User_Menu.setVisibility(View.GONE);
            iv_Club_Mini_User.setVisibility(View.VISIBLE);

            UserData data = TKManager.getInstance().UserData_Simple.get(key);
            CommonFunc.getInstance().DrawImageByGlide(mContext, iv_Club_Mini_User, data.GetUserImgThumb(), true);
        }
    }
}

class ClubMiniUserListHolderDecorator extends RecyclerView.ItemDecoration {
    private final int mSpace;

    public ClubMiniUserListHolderDecorator(int space) {
        this.mSpace = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = parent.getChildAdapterPosition(view);
        int itemCount = state.getItemCount();

        //상하 설정
        if(position == 0) {
        } else {
            outRect.left = mSpace;
        }
    }
}
