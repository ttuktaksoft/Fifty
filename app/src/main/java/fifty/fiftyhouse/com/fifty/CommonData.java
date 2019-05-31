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

    public static enum MainSortType
    {
        ALL,
        ONLINE
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

    // 처음에 보여줄 인기 관심사 갯수
    public static int Favorite_Pop_Count = 2;

}
