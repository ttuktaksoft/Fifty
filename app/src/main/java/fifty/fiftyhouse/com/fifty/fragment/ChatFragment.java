package fifty.fiftyhouse.com.fifty.fragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.common.internal.service.Common;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DataBase.ChatData;
import fifty.fiftyhouse.com.fifty.DialogFunc;
import fifty.fiftyhouse.com.fifty.Manager.FirebaseManager;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.activty.ChatBodyActivity;
import fifty.fiftyhouse.com.fifty.activty.ClubActivity;
import fifty.fiftyhouse.com.fifty.activty.UserListActivity;
import fifty.fiftyhouse.com.fifty.adapter.ChatAdapter;
import fifty.fiftyhouse.com.fifty.util.RecyclerItemClickListener;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChatFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatFragment extends Fragment {

    private Context mContext;
    private View ChatFragView;
    private TextView txt_empty;
    RecyclerView ChatRecyclerView;
    ChatAdapter mAdapter;

    String strTargetIndex;

    static final int REFRESH_CHATFRAGMENT = 0;

    public ChatFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ChatFragment newInstance(String param1, String param2) {
        ChatFragment fragment = new ChatFragment();
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
            //CommonFunc.getInstance().RefreshChatListData(mAdapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mContext = getContext();
        ChatFragView = inflater.inflate(R.layout.fragment_chat, container, false);
        ChatFragView.setTag("ChatFragment");

        txt_empty = ChatFragView.findViewById(R.id.tv_ChatFrag_empty);

        ChatRecyclerView = ChatFragView.findViewById(R.id.rv_ChatFrag_list_recy);

        mAdapter = new ChatAdapter(getContext());
        mAdapter.setHasStableIds(true);

        FirebaseManager.CheckFirebaseComplete listener = new FirebaseManager.CheckFirebaseComplete() {
            @Override
            public void CompleteListener() {
                mAdapter.notifyDataSetChanged();
                ChatRecyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
            }

            @Override
            public void CompleteListener_Yes() {
            }

            @Override
            public void CompleteListener_No() {
            }
        };

        Set KeySet = TKManager.getInstance().MyData.GetUserChatReadIndexListKeySet();
        Iterator iterator = KeySet.iterator();

        while(iterator.hasNext()){
            String key = (String)iterator.next();
            FirebaseManager.getInstance().MonitorChatData(key, TKManager.getInstance().MyData, listener);
        }

        ChatRecyclerView.setAdapter(mAdapter);
        ChatRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));

        ChatRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(mContext, ChatRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Set tempKey = TKManager.getInstance().MyData.GetUserChatDataListKeySet();
                List array = new ArrayList(tempKey);
                final ChatData tempChatData = TKManager.getInstance().MyData.GetUserChatDataList(array.get(position).toString());

                int idx = tempChatData.GetRoomIndex().indexOf("_");
                String tempStr = tempChatData.GetRoomIndex().substring(0, idx);
                String tempStrBack = tempChatData.GetRoomIndex().substring(idx+1);
                if(tempStr.equals(TKManager.getInstance().MyData.GetUserIndex()))
                {
                    strTargetIndex = tempStrBack;
                }
                else
                {
                    strTargetIndex = tempStr;
                }


                FirebaseManager.CheckFirebaseComplete listener = new FirebaseManager.CheckFirebaseComplete() {
                    @Override
                    public void CompleteListener() {

                        FirebaseManager.CheckFirebaseComplete listener = new FirebaseManager.CheckFirebaseComplete() {
                            @Override
                            public void CompleteListener() {
                                Intent intent = new Intent(mContext, ChatBodyActivity.class);
                                intent.putExtra("RoomIndex",tempChatData.GetRoomIndex());
                                //startActivity(intent);
                                    startActivityForResult(intent, REFRESH_CHATFRAGMENT);
                            }

                            @Override
                            public void CompleteListener_Yes() {
                            }

                            @Override
                            public void CompleteListener_No() {
                            }
                        };

                        if(TKManager.getInstance().UserData_Simple.get(strTargetIndex) != null)
                        {
                            Intent intent = new Intent(mContext, ChatBodyActivity.class);
                            intent.putExtra("RoomIndex",tempChatData.GetRoomIndex());
                            startActivityForResult(intent, REFRESH_CHATFRAGMENT);
                        }
                        else
                        {
                            FirebaseManager.getInstance().SetFireBaseLoadingCount(2);
                            FirebaseManager.getInstance().GetUserData_Simple(strTargetIndex, TKManager.getInstance().UserData_Simple, listener);
                        }

                    }

                    @Override
                    public void CompleteListener_Yes() {
                    }

                    @Override
                    public void CompleteListener_No() {
                    }
                };

                FirebaseManager.getInstance().GetUserChatData(tempChatData.GetRoomIndex(), TKManager.getInstance().MyData, listener);

                //startActivity(new Intent(getApplicationContext(), UserProfileActivity.class));
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

        return ChatFragView;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REFRESH_CHATFRAGMENT) {
            mAdapter.notifyDataSetChanged();

        }
    }
}
