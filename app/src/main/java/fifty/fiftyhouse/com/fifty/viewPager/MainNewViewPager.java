package fifty.fiftyhouse.com.fifty.viewPager;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DialogFunc;
import fifty.fiftyhouse.com.fifty.MainActivity;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.adapter.MainUserAdapter;
import fifty.fiftyhouse.com.fifty.util.OnSingleClickListener;

public class MainNewViewPager extends Fragment {

    TextView tv_Main_New_Desc, tv_Main_New_UserList_Empty;
    FloatingActionButton fa_Main_New_Search;
    RecyclerView rv_Main_New_UserList;
    View v_FragmentView = null;
    public MainUserAdapter mAdapter;
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
        MainUserAdapter.MainUserListener listener = new MainUserAdapter.MainUserListener()
        {
            @Override
            public void Listener(String key)
            {
                CommonFunc.getInstance().GetUserDataInFireBase(key, MainActivity.mActivity, false);
            }
        };
        mAdapter =  new MainUserAdapter(getContext(), listener);
        RefreshUserList();
        mAdapter.setItemData(mUserList);
        rv_Main_New_UserList.setAdapter(mAdapter);
        rv_Main_New_UserList.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public void RefreshAdapter()
    {
        RefreshUserList();
        mAdapter.setItemData(mUserList);
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
