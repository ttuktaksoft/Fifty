package fifty.fiftyhouse.com.fifty.DataBase;

import com.google.gson.annotations.SerializedName;

public class AccessTokenResponse
{
    @SerializedName("access_token")
    public String accessToken;
    @SerializedName("now")
    public Integer now;
    @SerializedName("expired_at")
    public Integer expiredAt;

    public AccessTokenResponse(String Code, Integer msg, Integer Response)
    {
        accessToken  = Code;
        now = msg;
        expiredAt = Response;
    }

}