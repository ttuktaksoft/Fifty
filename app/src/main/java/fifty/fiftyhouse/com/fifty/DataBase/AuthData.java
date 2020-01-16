package fifty.fiftyhouse.com.fifty.DataBase;

import com.google.gson.annotations.SerializedName;

public     class AuthData{
    @SerializedName("imp_key")
    public String api_key;
    @SerializedName("imp_secret")
    public String api_secret;
    public AuthData(String key, String secret)
    {
        api_key = key;
        api_secret = secret;
    }
}