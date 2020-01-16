package fifty.fiftyhouse.com.fifty.DataBase;

import com.google.gson.annotations.SerializedName;

import fifty.fiftyhouse.com.fifty.activty.AuthActivity;

public    class Certification{
    @SerializedName("code")
    public Integer code;
    @SerializedName("message")
    public String message;
    @SerializedName("response")
    public CertificationResponse response;

    public Certification(Integer Code, String msg, CertificationResponse Response)
    {
        code = Code;
        message = msg;
        response = Response;
    }

}