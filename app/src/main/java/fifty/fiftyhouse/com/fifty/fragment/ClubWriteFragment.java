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

import java.util.ArrayList;
import java.util.List;

import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.adapter.ClubWriteImgAdapter;
import fifty.fiftyhouse.com.fifty.util.OnRecyclerItemClickListener;
import fifty.fiftyhouse.com.fifty.util.RecyclerItemClickListener;

public class ClubWriteFragment extends Fragment {

    EditText et_ClubWrite_Desc;
    RecyclerView rv_ClubWrite_Pic;

    Context mContext;
    FragmentManager mFragmentMgr;
    View v_FragmentView = null;

    InputMethodManager imm;

    ClubWriteImgAdapter mAdapter;
    ArrayList<String> TempClubWriteImgLIst = new ArrayList<>();

    public ClubWriteFragment() {
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
        v_FragmentView = inflater.inflate(R.layout.fragment_club_write, container, false);

        imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);

        et_ClubWrite_Desc = v_FragmentView.findViewById(R.id.et_ClubWrite_Desc);
        rv_ClubWrite_Pic = v_FragmentView.findViewById(R.id.rv_ClubWrite_Pic);

        et_ClubWrite_Desc.setText("");
        TempClubWriteImgLIst.clear();

        initRecyclerView();

        return v_FragmentView;
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        // ((MainActivity)context).setOnKeyBackPressedListener((MainActivity.onKeyBackPressedListener) this);
    }

    private void initRecyclerView()
    {
        mAdapter = new ClubWriteImgAdapter(getContext());
        mAdapter.setHasStableIds(true);

        rv_ClubWrite_Pic.setAdapter(mAdapter);
        rv_ClubWrite_Pic.setLayoutManager(new GridLayoutManager(getContext(), 1));
        rv_ClubWrite_Pic.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), rv_ClubWrite_Pic, new OnRecyclerItemClickListener() {
            @Override
            public void onSingleClick(View view, int position) {

            }

        }));
    }

    public void RefreshAdapter()
    {
        mAdapter.setItemCount(TempClubWriteImgLIst.size());
        mAdapter.setItemData(TempClubWriteImgLIst);
        mAdapter.notifyDataSetChanged();
    }

    public void AddImg(String uri)
    {
        TempClubWriteImgLIst.add(uri);
        RefreshAdapter();
    }
}
