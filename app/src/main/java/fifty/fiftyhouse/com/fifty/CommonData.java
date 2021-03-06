package fifty.fiftyhouse.com.fifty;

public class CommonData {

    private static CommonData _Instance;

    public static CommonData getInstance() {
        if (_Instance == null)
            _Instance = new CommonData();

        return _Instance;
    }

    private CommonData() {

    }

    public static enum CollentionType
    {
        USERS,
        FAVORITELIST
    }

    public static enum GenderType
    {
        MAN,
        WOMAN
    }

    public static enum MainViewType
    {
        DIST,
        NEW,
        HOT,
        FRIEND
    }

    public static enum MyProfileMenuType
    {
        EVENT,
        NOTICE,
        FAQ,
        SETTING,
    }

    public static enum MSGType
    {
        MSG,
        IMG,
        VIDEO
    }
    public static enum ACTIVITY_INTENT_FLAG
    {
        CHATFRAGMENT
    }
    public static enum USER_LIST_TYPE
    {
        MY_LIKE_LIST,
        MY_VISIT_LIST,
        MY_FRIEND_LIST
    }

    public static enum SORT_SETTING_GENDER
    {
        ALL,
        WOMAN,
        MAN,
    }
    public static enum SORT_SETTING_ONLINE
    {
        ONLINE,
        SHORT,
        LONG,
    }

    public static enum CHAT_ROOM_TYPE
    {
        BOOKMARK,
        DEFAULT,
        CLUB
    }

    // 처음에 보여줄 인기 관심사 갯수
    public static int Favorite_Pop_Count = 10;
    // 처음에 검색할 인기 관심사 갯수
    public static int Favorite_Search_Pop_Count = 300;

    // 메인 화면 기준별 보여지는 유져 인원 수
    public static int UserList_First_Loding_Count = 500;
    public static int UserList_Loding_Count = 3;
    public static int UserList_Loding_Count_Index = 0;
    public static int ClubList_First_Loding_Count = 500;
    public static int UserList_First_View_Count = 15;
    public static int UserList_View_Count = 3;
    public static int ClubList_First_View_Count = 15;
    public static int ClubList_View_Count = 3;

    public static int UserData_Loding_Count = 5;

    public static int ClubFavoriteSelectMinCount = 1;
    public static int FavoriteSelectMinCount = 3;
    public static int FavoriteSelectMaxCount = 5;
    public static int FavoriteSelectMaxCountCheck = 6;
    public static int NickNameMinSize = 2;
    public static int NickNameMaxSize = 10;
    public static int ClubNameMinSize = 2;
    public static int ClubNameMaxSize = 10;
    public static int FavoriteNameMinSize = 1;
    public static int FavoriteNameMaxSize = 10;
    public static int ClubUserCountMinSize = 2;
    public static int ClubUserCountMaxSize = 100;
    public static int PassWordMinSize = 8;
    public static int PassWordMaxSize = 10;
    public static int MemoMaxSize = 4000;
    public static int LocationMaxSize = 30;
    public static int UserReportMaxSize = 4000;
    public static int ClubContentMaxSize = 4000;

    public static final int MAX_PROFILE_IMG_COUNT = 8;

    public static String DAILY_FAVORITE = null;

    public static final int USER_LIST_MY_LIKE = 1;
    public static final int USER_LIST_MY_VISIT = 2;
    public static final int USER_LIST_MY_FRIEND = 3;
    public static final int USER_LIST_CLUB = 4;
    public static final int USER_LIST_CLUB_JOIN_WAIT = 5;
    public static final int USER_LIST_CLUB_CHAT = 6;
    public static final int USER_LIST_BLOCK = 7;
    public static final int USER_LIST_CLUB_INVITE = 8;

    public static final String WIFI_STATE = "WIFE";
    public static final String MOBILE_STATE = "MOBILE";
    public static final String NONE_STATE = "NONE";

    public static int CHAT_FONT_SIZE = 21;
    public static final int CHAT_FONT_SIZE_SMALL = 18;
    public static final int CHAT_FONT_SIZE_DEFAULT = 21;
    public static final int CHAT_FONT_SIZE_BIG = 24;

    public static final int CLUB_MINI_USER_MAX_VIEW = 10;
    public static final int FAVORITE_FONT_SIZE_SMALL = 15;
    public static final int FAVORITE_FONT_SIZE_DEFAULT = 18;

    public static int FAVORITE_RANK_COUNT = 8;

    public static String SETTING_MSG_ALARM = "알람";
    public static String SETTING_MSG_SOUND = "소리";
    public static String SETTING_MSG_VIBRATION = "진동";

    public static boolean TEST_TEXT_VISIBLE = false;

    public static int NOTICE_TYPE_STR = 0;
    public static int NOTICE_TYPE_IMG = 1;

    public static int PROFILE_CLUB_VIEW_MAX = 3;

    public static final int CLUB_LIST_FAVORITE_SEARCH = 0;
    public static final int CLUB_LIST_MY = 1;
    public static final int CLUB_LIST_USER = 2;

    public static String SERVER_KEY = "AAAAuDqdci0:APA91bErkkUdJ4c1PAWjcUxF85U-jluM39AmuhbGCI4VgGDzfqt_KA2d8eKdwdh_oOLyXuqMeSCd762VjZUFFKLpulKoecq54m4LByFE250FwQVMFWajBU3JB84pipQOZKbS8hvuDFUH";
    public static String MSG_URL = "https://fcm.googleapis.com/fcm/send";
}
