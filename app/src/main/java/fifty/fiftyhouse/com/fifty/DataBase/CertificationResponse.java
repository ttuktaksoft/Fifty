package fifty.fiftyhouse.com.fifty.DataBase;

import com.google.gson.annotations.SerializedName;

public   class CertificationResponse{
    @SerializedName("imp_uid")
    public String impUid ;
    @SerializedName("merchant_uid")
    public  String merchantUid ;
    @SerializedName("pg_tid")
    public  String pgTid ;
    @SerializedName("pg_provider")
    public  String pgProvider;
    @SerializedName("name")
    public  String name;
    @SerializedName("gender")
    public  String gender;
    @SerializedName("birth")
    public  Integer birth;
    @SerializedName("foreigner")
    public  Boolean foreigner;
    @SerializedName("certified")
    public  Boolean certified;
    @SerializedName("certified_at")
    public  Integer certifiedAt;
    @SerializedName("unique_key")
    public  String uniqueKey;
    @SerializedName("unique_in_site")
    public  String uniqueInSite;
    @SerializedName("origin")
    public  String origin;

    public CertificationResponse(String impuid, String merchantuid, String pgtid, String pgprovider, String Name, String Gender, Integer Birth, Boolean Foreigner, Boolean Certified, Integer CertifiedAt, String UniqueKey, String UniqueInSite, String Origin)
    {
        impUid = impuid;
        merchantUid = merchantuid;
        pgTid = pgtid;
        pgProvider = pgprovider;
        name = Name;
        gender = Gender;
        birth = Birth;
        foreigner = Foreigner;
        certified = Certified;
        certifiedAt = CertifiedAt;
        uniqueKey = UniqueKey;
        uniqueInSite = UniqueInSite;
        origin = Origin;


    }

}