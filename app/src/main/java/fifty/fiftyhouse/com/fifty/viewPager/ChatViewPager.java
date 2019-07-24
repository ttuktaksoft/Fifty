package fifty.fiftyhouse.com.fifty.viewPager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import fifty.fiftyhouse.com.fifty.CommonData;
import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DataBase.ChatData;
import fifty.fiftyhouse.com.fifty.MainActivity;
import fifty.fiftyhouse.com.fifty.Manager.FirebaseManager;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.activty.ChatBodyActivity;
import fifty.fiftyhouse.com.fifty.adapter.ChatAdapter;
import fifty.fiftyhouse.com.fifty.adapter.ChatBodyAdapter;
import fifty.fiftyhouse.com.fifty.adapter.UserListAdapter;
import fifty.fiftyhouse.com.fifty.util.OnRecyclerItemClickListener;
import fifty.fiftyhouse.com.fifty.util.RecyclerItemClickListener;

public class ChatViewPager extends Fragment {

    RecyclerView rv_ChatList;
    TextView tv_Chat_Empty;
    View v_FragmentView = null;

    ChatAdapter mAdapter;
    ArrayList<String> mChatList = new ArrayList<>();

  /*  public static int CHAT_LIST_TYPE_BOOKMARK = 0;
    public static int CHAT_LIST_TYPE_DEFAULT = 1;*/

    public CommonData.CHAT_ROOM_TYPE mType = CommonData.CHAT_ROOM_TYPE.BOOKMARK;

    public ChatViewPager() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (v_FragmentView == null) {
            v_FragmentView = inflater.inflate(R.layout.viewpager_chat_list, container, false);
            rv_ChatList = v_FragmentView.findViewById(R.id.rv_ChatList);
            tv_Chat_Empty = v_FragmentView.findViewById(R.id.tv_Chat_Empty);

            initRecyclerView();
        } else {
            mAdapter.notifyDataSetChanged();
        }

        return v_FragmentView;
    }

    private void initRecyclerView() {
        mAdapter = new ChatAdapter(getContext(), mType);
        RefreshAdapter();
        mAdapter.setHasStableIds(true);
        mAdapter.notifyDataSetChanged();
       // rv_ChatList.scrollToPosition(mAdapter.getItemCount() - 1);

    /*    FirebaseManager.CheckFirebaseComplete listener = new FirebaseManager.CheckFirebaseComplete() {
            @Override
            public void CompleteListener() {
                //if(TKManager.getInstance().MyData.UserList_Chat.size() == TKManager.getInstance().MyData.GetUserChatDataListCount())
                {
                    //   CommonFunc.getInstance().SortByChatDate(TKManager.getInstance().MyData.UserList_Chat, false );
                    // 채팅방 정렬후
                    RefreshAdapter();
                    mAdapter.notifyDataSetChanged();
                    rv_ChatList.scrollToPosition(mAdapter.getItemCount() - 1);
                }
                //

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
        }*/

        rv_ChatList.setAdapter(mAdapter);
        rv_ChatList.setLayoutManager(new GridLayoutManager(getContext(), 1));
//        rv_ChatList.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), rv_ChatList, new OnRecyclerItemClickListener() {
//            @Override
//            public void onSingleClick(View view, int position) {
//
//                final String strTargetIndex;
//                Set tempKey = TKManager.getInstance().MyData.GetUserChatDataListKeySet();
//                List array = new ArrayList(tempKey);
//                final ChatData tempChatData = TKManager.getInstance().MyData.GetUserChatDataList(array.get(position).toString());
//
//                int idx = tempChatData.GetRoomIndex().indexOf("_");
//                String tempStr = tempChatData.GetRoomIndex().substring(0, idx);
//                String tempStrBack = tempChatData.GetRoomIndex().substring(idx+1);
//                if(tempStr.equals(TKManager.getInstance().MyData.GetUserIndex()))
//                {
//                    strTargetIndex = tempStrBack;
//                }
//                else
//                {
//                    strTargetIndex = tempStr;
//                }
//
//
//                FirebaseManager.CheckFirebaseComplete listener = new FirebaseManager.CheckFirebaseComplete() {
//                    @Override
//                    public void CompleteListener() {
//
//                        FirebaseManager.CheckFirebaseComplete listener = new FirebaseManager.CheckFirebaseComplete() {
//                            @Override
//                            public void CompleteListener() {
//                                Intent intent = new Intent(getContext(), ChatBodyActivity.class);
//                                intent.putExtra("RoomIndex",tempChatData.GetRoomIndex());
//                                //startActivity(intent);
//                                startActivityForResult(intent, REFRESH_CHATFRAGMENT);
//                            }
//
//                            @Override
//                            public void CompleteListener_Yes() {
//                            }
//
//                            @Override
//                            public void CompleteListener_No() {
//                            }
//                        };
//
//                        if(TKManager.getInstance().UserData_Simple.get(strTargetIndex) != null)
//                        {
//                            Intent intent = new Intent(getContext(), ChatBodyActivity.class);
//                            intent.putExtra("RoomIndex",tempChatData.GetRoomIndex());
//                            startActivityForResult(intent, REFRESH_CHATFRAGMENT);
//                        }
//                        else
//                        {
//                            FirebaseManager.getInstance().SetFireBaseLoadingCount(2);
//                            FirebaseManager.getInstance().GetUserData_Simple(strTargetIndex, TKManager.getInstance().UserData_Simple, listener);
//                        }
//
//                    }
//
//                    @Override
//                    public void CompleteListener_Yes() {
//                    }
//
//                    @Override
//                    public void CompleteListener_No() {
//                    }
//                };
//
//                FirebaseManager.getInstance().GetUserChatData(tempChatData.GetRoomIndex(), TKManager.getInstance().MyData, listener);
//
//                //startActivity(new Intent(getApplicationContext(), UserProfileActivity.class));
//                /*//CommonFunc.getInstance().ShowToast(view.getContext(), position+"번 째 아이템 클릭", true);
//                if (mAppStatus.bCheckMultiSend == false) {
//                    stTargetData = mMyData.arrUserAll_Hot_Age.get(position);
//
//                    if (mCommon.getClickStatus() == false)
//                        mCommon.MoveUserPage(getActivity(), stTargetData);
//                }*/
//            }
//
//            @Override
//            public void onLongItemClick(View view, int position) {
//                //  Toast.makeText(getApplicationContext(),position+"번 째 아이템 롱 클릭",Toast.LENGTH_SHORT).show();
//            }
//        }));
    }



    public void RefreshChatList() {
        mChatList.clear();

        if(mType == CommonData.CHAT_ROOM_TYPE.BOOKMARK)
        {
            // 즐겨찾기 채팅방
            mChatList.addAll(TKManager.getInstance().MyData.GetUserBookMarkChatDataListKeySet());
        }
        else
        {
            // 일반
            mChatList.addAll(TKManager.getInstance().MyData.GetUserChatDataListKeySet());
        }


        if (mChatList.size() == 0) {
            tv_Chat_Empty.setVisibility(View.VISIBLE);
            rv_ChatList.setVisibility(View.GONE);
        } else {
            tv_Chat_Empty.setVisibility(View.GONE);
            rv_ChatList.setVisibility(View.VISIBLE);
        }
    }

    public void RefreshAdapter() {
        RefreshChatList();
        mAdapter.setItemData(mChatList);
    }

    public void RefreshRecyclerView()
    {
        RefreshAdapter();
        mAdapter.notifyDataSetChanged();
    }

//    RecyclerView rv_ChatList;
//    TextView tv_Chat_Empty;
//    View v_FragmentView = null;
//
//    ChatAdapter mAdapter;
//    ArrayList<String> mChatList = new ArrayList<>();
//
//    public static int CHAT_LIST_TYPE_BOOKMARK = 0;
//    public static int CHAT_LIST_TYPE_DEFAULT = 1;
//
//    static final int REFRESH_CHATFRAGMENT = 0;
//
//    public int mType = CHAT_LIST_TYPE_BOOKMARK;
//
//    public ChatViewPager() {
//        super();
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        if (v_FragmentView == null) {
//            v_FragmentView = inflater.inflate(R.layout.viewpager_chat_list, container, false);
//            rv_ChatList = v_FragmentView.findViewById(R.id.rv_ChatList);
//            tv_Chat_Empty = v_FragmentView.findViewById(R.id.tv_Chat_Empty);
//
//            initRecyclerView();
//        } else {
//            mAdapter.notifyDataSetChanged();
//        }
//
//        return v_FragmentView;
//    }
//
//    private void initRecyclerView() {
//        mAdapter = new ChatAdapter(getContext());
//        RefreshAdapter();
//        mAdapter.setHasStableIds(true);
//
//        FirebaseManager.CheckFirebaseComplete listener = new FirebaseManager.CheckFirebaseComplete() {
//            @Override
//            public void CompleteListener() {
//                //if(TKManager.getInstance().MyData.UserList_Chat.size() == TKManager.getInstance().MyData.GetUserChatDataListCount())
//                {
//                    //   CommonFunc.getInstance().SortByChatDate(TKManager.getInstance().MyData.UserList_Chat, false );
//                    // 채팅방 정렬후
//                    RefreshAdapter();
//                    mAdapter.notifyDataSetChanged();
//                    rv_ChatList.scrollToPosition(mAdapter.getItemCount() - 1);
//                }
//                //
//
//            }
//
//            @Override
//            public void CompleteListener_Yes() {
//            }
//
//            @Override
//            public void CompleteListener_No() {
//            }
//        };
//
//        Set KeySet = TKManager.getInstance().MyData.GetUserChatReadIndexListKeySet();
//        Iterator iterator = KeySet.iterator();
//
//        while(iterator.hasNext()){
//            String key = (String)iterator.next();
//            FirebaseManager.getInstance().MonitorChatData(key, TKManager.getInstance().MyData, listener);
//        }
//
//        rv_ChatList.setAdapter(mAdapter);
//        rv_ChatList.setLayoutManager(new GridLayoutManager(getContext(), 1));
//        rv_ChatList.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), rv_ChatList, new OnRecyclerItemClickListener() {
//            @Override
//            public void onSingleClick(View view, int position) {
//
//                final String strTargetIndex;
//                Set tempKey = TKManager.getInstance().MyData.GetUserChatDataListKeySet();
//                List array = new ArrayList(tempKey);
//                final ChatData tempChatData = TKManager.getInstance().MyData.GetUserChatDataList(array.get(position).toString());
//
//                int idx = tempChatData.GetRoomIndex().indexOf("_");
//                String tempStr = tempChatData.GetRoomIndex().substring(0, idx);
//                String tempStrBack = tempChatData.GetRoomIndex().substring(idx+1);
//                if(tempStr.equals(TKManager.getInstance().MyData.GetUserIndex()))
//                {
//                    strTargetIndex = tempStrBack;
//                }
//                else
//                {
//                    strTargetIndex = tempStr;
//                }
//
//
//                FirebaseManager.CheckFirebaseComplete listener = new FirebaseManager.CheckFirebaseComplete() {
//                    @Override
//                    public void CompleteListener() {
//
//                        FirebaseManager.CheckFirebaseComplete listener = new FirebaseManager.CheckFirebaseComplete() {
//                            @Override
//                            public void CompleteListener() {
//                                Intent intent = new Intent(getContext(), ChatBodyActivity.class);
//                                intent.putExtra("RoomIndex",tempChatData.GetRoomIndex());
//                                //startActivity(intent);
//                                startActivityForResult(intent, REFRESH_CHATFRAGMENT);
//                            }
//
//                            @Override
//                            public void CompleteListener_Yes() {
//                            }
//
//                            @Override
//                            public void CompleteListener_No() {
//                            }
//                        };
//
//                        if(TKManager.getInstance().UserData_Simple.get(strTargetIndex) != null)
//                        {
//                            Intent intent = new Intent(getContext(), ChatBodyActivity.class);
//                            intent.putExtra("RoomIndex",tempChatData.GetRoomIndex());
//                            startActivityForResult(intent, REFRESH_CHATFRAGMENT);
//                        }
//                        else
//                        {
//                            FirebaseManager.getInstance().SetFireBaseLoadingCount(2);
//                            FirebaseManager.getInstance().GetUserData_Simple(strTargetIndex, TKManager.getInstance().UserData_Simple, listener);
//                        }
//
//                    }
//
//                    @Override
//                    public void CompleteListener_Yes() {
//                    }
//
//                    @Override
//                    public void CompleteListener_No() {
//                    }
//                };
//
//                FirebaseManager.getInstance().GetUserChatData(tempChatData.GetRoomIndex(), TKManager.getInstance().MyData, listener);
//
//                //startActivity(new Intent(getApplicationContext(), UserProfileActivity.class));
//                /*//CommonFunc.getInstance().ShowToast(view.getContext(), position+"번 째 아이템 클릭", true);
//                if (mAppStatus.bCheckMultiSend == false) {
//                    stTargetData = mMyData.arrUserAll_Hot_Age.get(position);
//
//                    if (mCommon.getClickStatus() == false)
//                        mCommon.MoveUserPage(getActivity(), stTargetData);
//                }*/
//            }
//
//            @Override
//            public void onLongItemClick(View view, int position) {
//                //  Toast.makeText(getApplicationContext(),position+"번 째 아이템 롱 클릭",Toast.LENGTH_SHORT).show();
//            }
//        }));
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        if (requestCode == REFRESH_CHATFRAGMENT) {
//            RefreshChatList();
//            mAdapter.notifyDataSetChanged();
//        }
//    }
//
//    public void RefreshChatList() {
//        mChatList.clear();
//
//        if(mType == CHAT_LIST_TYPE_BOOKMARK)
//        {
//            // 즐겨찾기 채팅방
//            mChatList.addAll(TKManager.getInstance().MyData.GetUserChatDataKeySet());
//        }
//        else
//        {
//            // 일반
//            mChatList.addAll(TKManager.getInstance().MyData.GetUserChatDataKeySet());
//        }
//
//
//        if (mChatList.size() == 0) {
//            tv_Chat_Empty.setVisibility(View.VISIBLE);
//            rv_ChatList.setVisibility(View.GONE);
//        } else {
//            tv_Chat_Empty.setVisibility(View.GONE);
//            rv_ChatList.setVisibility(View.VISIBLE);
//        }
//    }
//
//    public void RefreshAdapter() {
//        RefreshChatList();
//        mAdapter.setItemData(mChatList);
//    }
}