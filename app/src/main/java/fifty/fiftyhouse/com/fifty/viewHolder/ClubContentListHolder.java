package fifty.fiftyhouse.com.fifty.viewHolder;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.w3c.dom.Text;

import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.activty.Profile.UserProfileActivity;
import fifty.fiftyhouse.com.fifty.adapter.ClubContentReplyAdapter;
import fifty.fiftyhouse.com.fifty.adapter.MainAdapter;
import fifty.fiftyhouse.com.fifty.util.RecyclerItemClickListener;

public class ClubContentListHolder extends RecyclerView.ViewHolder {

    public enum CLUB_CONTENT_TYPE {
        DESC,
        BIG_IMG,
        IMG,
    }
    public ImageView iv_Club_Con_Profile;
    public TextView tv_Club_Con_Nickname, tv_Club_Con_Date, tv_Club_Con_Desc;
    public ImageView tv_Club_Con_BigImg, tv_Club_Con_Img_1, tv_Club_Con_Img_2, tv_Club_Con_Img_3;
    public RecyclerView rv_Club_Reply_List;
    CLUB_CONTENT_TYPE mContentType;
    ClubContentReplyAdapter mAdapter;
    Context mContext;

    public ClubContentListHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();

        iv_Club_Con_Profile = itemView.findViewById(R.id.iv_Club_Con_Profile);
        tv_Club_Con_Nickname = itemView.findViewById(R.id.tv_Club_Con_Nickname);
        tv_Club_Con_Date = itemView.findViewById(R.id.tv_Club_Con_Date);
        tv_Club_Con_Desc = itemView.findViewById(R.id.tv_Club_Con_Desc);
        tv_Club_Con_BigImg = itemView.findViewById(R.id.tv_Club_Con_BigImg);
        tv_Club_Con_Img_1 = itemView.findViewById(R.id.tv_Club_Con_Img_1);
        tv_Club_Con_Img_2 = itemView.findViewById(R.id.tv_Club_Con_Img_2);
        tv_Club_Con_Img_3 = itemView.findViewById(R.id.tv_Club_Con_Img_3);
        rv_Club_Reply_List = itemView.findViewById(R.id.rv_Club_Reply_List);

        int viewCount = 3;
        int thumbnailMargin = CommonFunc.getInstance().convertPXtoDP(mContext.getResources(), 2);
        int thumbnailMargin_2 = CommonFunc.getInstance().convertPXtoDP(mContext.getResources(), 10);
        int thumbnailSize = CommonFunc.getInstance().getWidthByDevice() / viewCount;

        ConstraintLayout.LayoutParams lp_Club_Con_BigImg = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, thumbnailSize);
        lp_Club_Con_BigImg.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
        lp_Club_Con_BigImg.topToBottom = tv_Club_Con_Desc.getId();
        lp_Club_Con_BigImg.setMargins(thumbnailMargin_2,thumbnailMargin,thumbnailMargin_2,thumbnailMargin);
        tv_Club_Con_BigImg.setLayoutParams(lp_Club_Con_BigImg);

        ConstraintLayout.LayoutParams lp_Club_Con_Img_1 = new ConstraintLayout.LayoutParams(thumbnailSize, thumbnailSize);
        lp_Club_Con_Img_1.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
        lp_Club_Con_Img_1.topToBottom = tv_Club_Con_Desc.getId();
        lp_Club_Con_Img_1.setMargins(thumbnailMargin_2,thumbnailMargin,thumbnailMargin,thumbnailMargin_2);
        tv_Club_Con_Img_1.setLayoutParams(lp_Club_Con_Img_1);

        ConstraintLayout.LayoutParams lp_Club_Con_Img_2 = new ConstraintLayout.LayoutParams(thumbnailSize, thumbnailSize);
        lp_Club_Con_Img_2.leftToRight = tv_Club_Con_Img_1.getId();
        lp_Club_Con_Img_2.topToTop = tv_Club_Con_Img_1.getId();
        lp_Club_Con_Img_2.setMargins(thumbnailMargin,0,thumbnailMargin,0);
        tv_Club_Con_Img_2.setLayoutParams(lp_Club_Con_Img_2);

        ConstraintLayout.LayoutParams lp_Club_Con_Img_3 = new ConstraintLayout.LayoutParams(thumbnailSize, thumbnailSize);
        lp_Club_Con_Img_3.leftToRight = tv_Club_Con_Img_2.getId();
        lp_Club_Con_Img_3.topToTop = tv_Club_Con_Img_2.getId();
        lp_Club_Con_Img_3.setMargins(thumbnailMargin,0,thumbnailMargin,0);
        tv_Club_Con_Img_3.setLayoutParams(lp_Club_Con_Img_3);

        initReplyList();
    }

    public void setClubContent(int pos)
    {
        // TODO 클럽 내용 추가
        if(pos == 0 || pos == 3)
        {
            setClubType(CLUB_CONTENT_TYPE.DESC);
            Glide.with(mContext)
                    //.load(mMyData.arrSendDataList.get(position).strTargetImg)
                    .load(R.drawable.login_icon)
                    .circleCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .thumbnail(0.1f)
                    .into(iv_Club_Con_Profile);
            tv_Club_Con_Nickname.setText("닉네임입니다_0");
            tv_Club_Con_Date.setText("지금막");
            tv_Club_Con_Desc.setText("내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용");

            mAdapter.setReplyCount(2);
        }
        else if(pos == 1|| pos == 4)
        {
            setClubType(CLUB_CONTENT_TYPE.BIG_IMG);
            Glide.with(mContext)
                    //.load(mMyData.arrSendDataList.get(position).strTargetImg)
                    .load(R.drawable.login_icon)
                    .circleCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .thumbnail(0.1f)
                    .into(iv_Club_Con_Profile);
            tv_Club_Con_Nickname.setText("닉네임입니다_0");
            tv_Club_Con_Date.setText("지금막");
            tv_Club_Con_Desc.setText("내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용");

            Glide.with(mContext)
                    .load(R.drawable.login_icon)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .thumbnail(0.1f)
                    .centerCrop()
                    .into(tv_Club_Con_BigImg);

            mAdapter.setReplyCount(1);
        }
        else
        {
            setClubType(CLUB_CONTENT_TYPE.IMG);
            Glide.with(mContext)
                    //.load(mMyData.arrSendDataList.get(position).strTargetImg)
                    .load(R.drawable.login_icon)
                    .circleCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .thumbnail(0.1f)
                    .into(iv_Club_Con_Profile);
            tv_Club_Con_Nickname.setText("닉네임입니다_0");
            tv_Club_Con_Date.setText("지금막");
            tv_Club_Con_Desc.setText("내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용");

            Glide.with(mContext)
                    .load(R.drawable.login_icon)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .thumbnail(0.1f)
                    .centerCrop()
                    .into(tv_Club_Con_Img_1);

            Glide.with(mContext)
                    .load(R.drawable.login_icon)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .thumbnail(0.1f)
                    .centerCrop()
                    .into(tv_Club_Con_Img_2);

            mAdapter.setReplyCount(1);
        }
    }

    private void setClubType(CLUB_CONTENT_TYPE type)
    {
        mContentType = type;
        tv_Club_Con_BigImg.setVisibility(View.GONE);
        tv_Club_Con_Img_1.setVisibility(View.GONE);
        tv_Club_Con_Img_2.setVisibility(View.GONE);
        tv_Club_Con_Img_3.setVisibility(View.GONE);

        ConstraintLayout.LayoutParams lp_Club_Reply_List = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        switch (type)
        {
            case DESC:
                lp_Club_Reply_List.topToBottom = tv_Club_Con_Desc.getId();
                break;
            case BIG_IMG:
                tv_Club_Con_BigImg.setVisibility(View.VISIBLE);
                lp_Club_Reply_List.topToBottom = tv_Club_Con_BigImg.getId();
                break;
            case IMG:
                tv_Club_Con_Img_1.setVisibility(View.VISIBLE);
                tv_Club_Con_Img_2.setVisibility(View.VISIBLE);
                tv_Club_Con_Img_3.setVisibility(View.VISIBLE);
                lp_Club_Reply_List.topToBottom = tv_Club_Con_Img_1.getId();
                break;
        }

        rv_Club_Reply_List.setLayoutParams(lp_Club_Reply_List);
    }

    private void initReplyList()
    {
        mAdapter = new ClubContentReplyAdapter(mContext);
        mAdapter.setHasStableIds(true);

        rv_Club_Reply_List.setAdapter(mAdapter);
        rv_Club_Reply_List.setLayoutManager(new GridLayoutManager(mContext, 1));
        rv_Club_Reply_List.addOnItemTouchListener(new RecyclerItemClickListener(mContext, rv_Club_Reply_List, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //startActivity(new Intent(getContext(), UserProfileActivity.class));
                /*//CommonFunc.getInstance().ShowToast(view.getContext(), position+"번 째 아이템 클릭", true);
                if (mAppStatus.bCheckMultiSend == false) {
                    stTargetData = mMyData.arrUserAll_Hot_Age.get(position);

                    if (mCommon.getClickStatus() == false)
                        mCommon.MoveUserPage(getActivity(), stTargetData);
                }*/
            }

            @Override
            public void onLongItemClick(View view, int position) {
                //  Toast.makeText(getApplicationContext(),position+"번 째 아이템 롱 클릭",Toast.LENGTH_SHORT).show();
            }
        }));

        rv_Club_Reply_List.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                /*int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                int nSize = 0;
                nSize = recyclerView.getAdapter().getItemCount() - 1;

                if (lastVisibleItemPosition == nSize) {
                    // Toast.makeText(getContext(), "Last Position", Toast.LENGTH_SHORT).show();
                    //    CommonFunc.getInstance().ShowLoadingPage(getContext(), "로딩중");
                    //  FirebaseData.getInstance().GetHotData(RecvAdapter, false);
                }*/
            }
        });
    }
}