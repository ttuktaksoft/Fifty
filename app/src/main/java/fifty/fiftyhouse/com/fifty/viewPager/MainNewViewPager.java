package fifty.fiftyhouse.com.fifty.viewPager;

import android.os.Bundle;

import com.felipecsl.asymmetricgridview.library.widget.AsymmetricGridView;
import com.felipecsl.asymmetricgridview.library.widget.AsymmetricGridViewAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fifty.fiftyhouse.com.fifty.CommonData;
import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DialogFunc;
import fifty.fiftyhouse.com.fifty.MainActivity;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.adapter.CustomGridListHolder;
import fifty.fiftyhouse.com.fifty.adapter.CustomMainAdapterOne;
import fifty.fiftyhouse.com.fifty.adapter.MainAdapterOne;
import fifty.fiftyhouse.com.fifty.util.OnRecyclerItemClickListener;
import fifty.fiftyhouse.com.fifty.util.OnSingleClickListener;
import fifty.fiftyhouse.com.fifty.util.RecyclerItemClickListener;
import fifty.fiftyhouse.com.fifty.util.RecyclerItemOneClickListener;

public class MainNewViewPager extends Fragment {

    TextView tv_Main_New_Desc, tv_Main_New_UserList_Empty;
    FloatingActionButton fa_Main_New_Search;
    AsymmetricGridView rv_Main_New_UserList;
    View v_FragmentView = null;
    public CustomMainAdapterOne mAdapter;
    ArrayList<String> mUserList = new ArrayList<>();
    private String UserIndex;

    public MainNewViewPager() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (v_FragmentView == null) {
            // Inflate the layout for this fragment
            v_FragmentView = inflater.inflate(R.layout.viewpager_main_new, container, false);
            tv_Main_New_UserList_Empty = v_FragmentView.findViewById(R.id.tv_Main_New_UserList_Empty);
            rv_Main_New_UserList = v_FragmentView.findViewById(R.id.rv_Main_New_UserList);
            tv_Main_New_Desc = v_FragmentView.findViewById(R.id.tv_Main_New_Desc);
            fa_Main_New_Search = v_FragmentView.findViewById(R.id.fa_Main_New_Search);

            initRecyclerView();


            fa_Main_New_Search.setOnClickListener(new OnSingleClickListener() {
                @Override
                public void onSingleClick(View view) {
                    DialogFunc.getInstance().ShowUserSearchPopup(getContext(), MainActivity.mActivity);
                }
            });

        } else {
            RefreshAdapter();
        }
        return v_FragmentView;
    }


    private void initRecyclerView()
    {
        /*mAdapter = new MainAdapterOne(getContext());
        RefreshAdapter();
        mAdapter.setHasStableIds(true);
        mAdapter.SetItemCountByType(CommonData.MainViewType.NEW, TKManager.getInstance().View_UserList_New.size());

        rv_Main_New_UserList.setAdapter(mAdapter);
        rv_Main_New_UserList.setLayoutManager(new GridLayoutManager(getContext(), 3));
        rv_Main_New_UserList.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), rv_Main_New_UserList, new OnRecyclerItemClickListener() {
            @Override
            public void onSingleClick(View view, int position) {

                UserIndex = TKManager.getInstance().UserData_Simple.get(TKManager.getInstance().View_UserList_New.get(position)).GetUserIndex();
                CommonFunc.getInstance().GetUserDataInFireBase(UserIndex, MainActivity.mActivity, false);
            }

            @Override
            public void onLongItemClick(View view, int position) {
                //  Toast.makeText(getApplicationContext(),position+"번 째 아이템 롱 클릭",Toast.LENGTH_SHORT).show();
            }
        }));*/

        RefreshUserList();
        mAdapter =  new CustomMainAdapterOne(getContext(), CommonFunc.getInstance().getCustomGridListHolderList(mUserList));
        mAdapter.SetItemCountByType(CommonData.MainViewType.NEW);
        rv_Main_New_UserList.setRequestedColumnCount(3);
        rv_Main_New_UserList.setAdapter(new AsymmetricGridViewAdapter(getContext(), rv_Main_New_UserList,mAdapter));
        rv_Main_New_UserList.setOnItemClickListener(
                new RecyclerItemOneClickListener() {
                    @Override
                    public void RecyclerItemOneClick(int position) {
                        UserIndex = TKManager.getInstance().UserData_Simple.get(mUserList.get(position)).GetUserIndex();
                        CommonFunc.getInstance().GetUserDataInFireBase(UserIndex, MainActivity.mActivity, false);
                    }
                });



    }

    public void RefreshAdapter()
    {
        RefreshUserList();

        List<CustomGridListHolder> list = CommonFunc.getInstance().getCustomGridListHolderList(mUserList);
        mAdapter.setItems(list);
    }

    public void RefreshUserList()
    {
        mUserList.clear();
        mUserList.addAll(TKManager.getInstance().View_UserList_New);

        if(TKManager.getInstance().View_UserList_New.size() == 0)
        {
            rv_Main_New_UserList.setVisibility(View.GONE);
            tv_Main_New_UserList_Empty.setVisibility(View.VISIBLE);
        }
        else
        {
            rv_Main_New_UserList.setVisibility(View.VISIBLE);
            tv_Main_New_UserList_Empty.setVisibility(View.GONE);
        }
    }


}
