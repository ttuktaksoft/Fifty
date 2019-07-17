package fifty.fiftyhouse.com.fifty.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DataBase.ClubContextData;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.activty.ClubBodyActivity;
import fifty.fiftyhouse.com.fifty.adapter.ClubBodyImgAdapter;
import fifty.fiftyhouse.com.fifty.adapter.ClubBodyReplyAdapter;
import fifty.fiftyhouse.com.fifty.adapter.ClubWriteImgAdapter;
import fifty.fiftyhouse.com.fifty.util.OnRecyclerItemClickListener;
import fifty.fiftyhouse.com.fifty.util.RecyclerItemClickListener;

public class ClubBodyFragment extends Fragment {

    ImageView iv_Club_Body_Profile;
    TextView tv_Club_Body_Nickname, tv_Club_Body_Date, tv_Club_Body_Desc;
    RecyclerView rv_Club_Body_Img_List, rv_Club_Body_Reply_List;
    ClubBodyImgAdapter mImgAdapter;
    ClubBodyReplyAdapter mReplyAdapter;

    Context mContext;
    FragmentManager mFragmentMgr;
    View v_FragmentView = null;

    ArrayList<String> mReplyList = new ArrayList<>();

    public ClubContextData tempData = null;

    public ClubBodyFragment() {
    }

    // TODO: Rename and change types and number of parameters
    public static ClubWriteFragment newInstance(String param1, String param2) {
        ClubWriteFragment fragment = new ClubWriteFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mContext = getActivity();
        mFragmentMgr = ((FragmentActivity) mContext).getSupportFragmentManager();
        v_FragmentView = inflater.inflate(R.layout.fragment_club_body, container, false);

        iv_Club_Body_Profile = v_FragmentView.findViewById(R.id.iv_Club_Body_Profile);
        tv_Club_Body_Nickname = v_FragmentView.findViewById(R.id.tv_Club_Body_Nickname);
        tv_Club_Body_Date = v_FragmentView.findViewById(R.id.tv_Club_Body_Date);
        tv_Club_Body_Desc = v_FragmentView.findViewById(R.id.tv_Club_Body_Desc);
        rv_Club_Body_Img_List = v_FragmentView.findViewById(R.id.rv_Club_Body_Img_List);
        rv_Club_Body_Reply_List = v_FragmentView.findViewById(R.id.rv_Club_Body_Reply_List);

        CommonFunc.getInstance().DrawImageByGlide(mContext, iv_Club_Body_Profile, TKManager.getInstance().UserData_Simple.get(tempData.GetWriterIndex()).GetUserImgThumb(), true);
        tv_Club_Body_Nickname.setText(TKManager.getInstance().UserData_Simple.get(tempData.GetWriterIndex()).GetUserNickName());
        tv_Club_Body_Date.setText(CommonFunc.getInstance().ConvertTimeSrt(Long.parseLong(tempData.GetDate())));
        tv_Club_Body_Desc.setText(tempData.GetContext());

        initRecyclerImgView();
        initRecyclerReplyView();

        return v_FragmentView;
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        // ((MainActivity)context).setOnKeyBackPressedListener((MainActivity.onKeyBackPressedListener) this);
    }

    private void initRecyclerImgView()
    {
        mImgAdapter = new ClubBodyImgAdapter(mContext, tempData);
        mImgAdapter.setImgCount(tempData.GetImgCount());
        mImgAdapter.setHasStableIds(true);

        rv_Club_Body_Img_List.setAdapter(mImgAdapter);
        rv_Club_Body_Img_List.setLayoutManager(new GridLayoutManager(mContext, 1));
        rv_Club_Body_Img_List.addOnItemTouchListener(new RecyclerItemClickListener(mContext, rv_Club_Body_Img_List, new OnRecyclerItemClickListener() {
            @Override
            public void onSingleClick(View view, int position) {

            }
        }));
    }

    private void initRecyclerReplyView()
    {
        mReplyAdapter = new ClubBodyReplyAdapter(mContext);
        mReplyList.clear();
        mReplyList.addAll(tempData.GetReplyDataKeySet());
        mReplyAdapter.setItemCount(mReplyList.size());
        mReplyAdapter.setItemData(mReplyList);
        mReplyAdapter.setHasStableIds(true);

        rv_Club_Body_Reply_List.setAdapter(mReplyAdapter);
        rv_Club_Body_Reply_List.setLayoutManager(new GridLayoutManager(mContext, 1));
    }

    public void RefreshReply()
    {
        mReplyList.clear();
        mReplyList.addAll(tempData.GetReplyDataKeySet());
        mReplyAdapter.setItemCount(mReplyList.size());
        mReplyAdapter.setItemData(mReplyList);
        mReplyAdapter.notifyDataSetChanged();
    }
}