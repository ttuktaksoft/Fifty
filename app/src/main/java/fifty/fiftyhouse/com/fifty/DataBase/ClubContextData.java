package fifty.fiftyhouse.com.fifty.DataBase;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class ClubContextData {
    public String writerIndex;
    public String Context;
    public String Date;
    public int ContextType;

    public Map<String, String> Img = new LinkedHashMap<String, String>();
    public Map<String, ClubContextData> Reply = new LinkedHashMap<String, ClubContextData>();

}
