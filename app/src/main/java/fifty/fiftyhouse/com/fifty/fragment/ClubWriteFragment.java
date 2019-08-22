package fifty.fiftyhouse.com.fifty.fragment;

import android.content.Context;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Iterator;

import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DataBase.ClubContextData;
import fifty.fiftyhouse.com.fifty.DialogFunc;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.adapter.ClubWriteImgAdapter;
import fifty.fiftyhouse.com.fifty.util.OnRecyclerItemClickListener;
import fifty.fiftyhouse.com.fifty.util.OnSingleTouchListener;
import fifty.fiftyhouse.com.fifty.util.RecyclerItemClickListener;

public class ClubWriteFragment extends Fragment {

    ConstraintLayout v_ClubWrite_Touch;
    EditText et_ClubWrite_Desc;
    RecyclerView rv_ClubWrite_Pic;

    Context mContext;
    FragmentManager mFragmentMgr;
    View v_FragmentView = null;

    InputMethodManager imm;

    ClubWriteImgAdapter mAdapter;
    ArrayList<String> TempClubWriteImgLIst = new ArrayList<>();
    public ClubContextData tempData = null;

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

        if(v_FragmentView != null)
        {
            mAdapter.notifyDataSetChanged();
            return v_FragmentView;
        }


        // Inflate the layout for this fragment
        mContext = getActivity();
        mFragmentMgr = ((FragmentActivity) mContext).getSupportFragmentManager();
        v_FragmentView = inflater.inflate(R.layout.fragment_club_write, container, false);

        imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);

        et_ClubWrite_Desc = v_FragmentView.findViewById(R.id.et_ClubWrite_Desc);
        rv_ClubWrite_Pic = v_FragmentView.findViewById(R.id.rv_ClubWrite_Pic);
        v_ClubWrite_Touch = v_FragmentView.findViewById(R.id.v_ClubWrite_Touch);
        et_ClubWrite_Desc.setText("");

        v_ClubWrite_Touch.setOnTouchListener(new OnSingleTouchListener() {
            @Override
            public void onSingleTouch(View view) {
                et_ClubWrite_Desc.post(new Runnable() {
                    @Override
                    public void run() {
                        et_ClubWrite_Desc.setFocusableInTouchMode(true);
                        et_ClubWrite_Desc.requestFocus();
                        imm.showSoftInput(et_ClubWrite_Desc,0);
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                    }
                });
            }
        });

        et_ClubWrite_Desc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                TKManager.getInstance().CreateTempClubContextData.SetContext(et_ClubWrite_Desc.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        TempClubWriteImgLIst.clear();

        initRecyclerView();

        if(tempData != null)
        {
            et_ClubWrite_Desc.setText(tempData.Context);

            TempClubWriteImgLIst.clear();;
            Iterator<String> iterator = tempData.GetImg().keySet().iterator();
            while(iterator.hasNext()){
                String key = iterator.next();
                TempClubWriteImgLIst.add(tempData.GetImg(key));
            }
            RefreshAdapter();
        }

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
        RefreshAdapter();
        mAdapter.setHasStableIds(true);

        rv_ClubWrite_Pic.setAdapter(mAdapter);
        rv_ClubWrite_Pic.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_ClubWrite_Pic.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), rv_ClubWrite_Pic, new OnRecyclerItemClickListener() {
            @Override
            public void onSingleClick(View view, int position) {
                TempClubWriteImgLIst.remove(position);
                RefreshAdapter();
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

    public boolean IsWrite()
    {
        imm.hideSoftInputFromWindow(et_ClubWrite_Desc.getWindowToken(), 0);

        if(CommonFunc.getInstance().CheckStringNull(et_ClubWrite_Desc.getText().toString()))
        {
            DialogFunc.getInstance().ShowMsgPopup(mContext, CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MSG_DESC_EMPTY));
            return false;
        }

        return true;
    }
}
