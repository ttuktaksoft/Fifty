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

    public static enum MSGType
    {
        MSG,
        IMG,
        VIDEO
    }

    // 처음에 보여줄 인기 관심사 갯수
    public static int Favorite_Pop_Count = 6;
    // 처음에 검색할 인기 관심사 갯수
    public static int Favorite_Search_Pop_Count = 30;

    // 메인 화면 기준별 보여지는 유져 인원 수
    public static int UserList_Loding_Count = 10;


    public static int UserData_Loding_Count = 5;

}
