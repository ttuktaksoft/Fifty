package fifty.fiftyhouse.com.fifty.DataBase;

import com.google.gson.annotations.SerializedName;

import fifty.fiftyhouse.com.fifty.activty.AuthActivity;

public class AccessToken{
    @SerializedName("code")
    public Integer code;
    @SerializedName("message")
    public  Object message;
    @SerializedName("response")
    public AccessTokenResponse response;

    public AccessToken(Integer Code, Object msg, AccessTokenResponse Response)
    {
        code = Code;
        message = msg;
        response = Response;
    }

}