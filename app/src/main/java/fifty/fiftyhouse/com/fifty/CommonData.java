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
    public static int UserList_Loding_Count = 10;

    public static int UserData_Loding_Count = 5;

    public static int FavoriteSelectMinCount = 3;
    public static int FavoriteSelectMaxCount = 5;
    public static int FavoriteSelectMaxCountCheck = 6;
    public static int NickNameMinSize = 2;
    public static int NickNameMaxSize = 10;
    public static int PassWordMinSize = 8;
    public static int PassWordMaxSize = 10;
    public static int MemoMaxSize = 4000;
    public static int LocationMaxSize = 30;
    public static int UserReportMaxSize = 4000;

    public static final int GET_PHOTO_FROM_GALLERY = 1;
    public static final int GET_PHOTO_FROM_CAMERA = 2;
    public static final int GET_PHOTO_FROM_CROP = 3;

    public static final int MAX_PROFILE_IMG_COUNT = 8;

    public static String DAILY_FAVORITE = null;

    public static final int USER_LIST_MY_LIKE = 1;
    public static final int USER_LIST_MY_VISIT = 2;
    public static final int USER_LIST_MY_FRIEND = 3;
    public static final int USER_LIST_CLUB = 4;
    public static final int USER_LIST_CLUB_JOIN_WAIT = 5;

    public static final String WIFI_STATE = "WIFE";
    public static final String MOBILE_STATE = "MOBILE";
    public static final String NONE_STATE = "NONE";

    public static int CHAT_FONT_SIZE = 21;
    public static final int CHAT_FONT_SIZE_SMALL = 18;
    public static final int CHAT_FONT_SIZE_DEFAULT = 21;
    public static final int CHAT_FONT_SIZE_BIG = 24;

    public static final int CLUB_MINI_USER_MAX_VIEW = 10;

}
