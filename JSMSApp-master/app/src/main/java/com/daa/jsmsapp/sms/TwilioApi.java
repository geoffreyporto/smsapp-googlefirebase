package com.daa.jsmsapp.sms;

import java.util.Map;

//Solución de Rretrofit usando okhttp3 en su core.
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface TwilioApi {
    @FormUrlEncoded
    @POST("Accounts/{ACCOUNT_SID}/SMS/Messages")
    Call<ResponseBody> sendMessage(
            @Path("ACCOUNT_SID") String accountSId,
            @Header("Authorization") String signature,
            @FieldMap Map<String, String> metadata );
}