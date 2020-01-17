package fifty.fiftyhouse.com.fifty.Interface;

import fifty.fiftyhouse.com.fifty.DataBase.AccessToken;
import fifty.fiftyhouse.com.fifty.DataBase.AuthData;
import fifty.fiftyhouse.com.fifty.DataBase.Certification;
import fifty.fiftyhouse.com.fifty.DataBase.Payment;
import fifty.fiftyhouse.com.fifty.activty.PurchaseActivity;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public     interface IamportClient {
    @POST("/users/getToken")
    Call<AccessToken> token(@Body AuthData auth);

    @GET("/certifications/{imp_uid}")
    Call<Certification> certification_by_imp_uid(
            @Header("Authorization") String token,
            @Path("imp_uid") String imp_uid
    );

    @GET("/payments/{imp_uid}")
    Call<Payment> payment_by_imp_uid(
            @Header("Authorization") String token,
            @Path("imp_uid") String imp_uid
    );
}
