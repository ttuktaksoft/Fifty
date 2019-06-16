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

    public static enum MyProfileViewType
    {
        VISIT,
        LIKE,
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
    public static enum FavoriteSelectType
    {
        SIGNUP,
        EDIT
    }

    public static enum ACTIVITY_INTENT_FLAG
    {
        CHATFRAGMENT
    }


    // 처음에 보여줄 인기 관심사 갯수
    public static int Favorite_Pop_Count = 2;
    // 처음에 검색할 인기 관심사 갯수
    public static int Favorite_Search_Pop_Count = 30;

    // 메인 화면 기준별 보여지는 유져 인원 수
    public static int UserList_Loding_Count = 10;


    public static int UserData_Loding_Count = 5;

    public static int FavoriteSelectMinCount = 2;
    public static int FavoriteSelectMaxCount = 5;
    public static int NickNameMinSize = 2;
    public static int NickNameMaxSize = 10;
    public static int MemoMaxSize = 4000;
    public static int LocationMaxSize = 30;
    public static int UserReportMaxSize = 4000;

    public static final int GET_PHOTO_FROM_GALLERY = 1;
    public static final int GET_PHOTO_FROM_CAMERA = 2;
    public static final int GET_PHOTO_FROM_CROP = 3;

    public static final int MAX_PROFILE_IMG_COUNT = 8;

}
