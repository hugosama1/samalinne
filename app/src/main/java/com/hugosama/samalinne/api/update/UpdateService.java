package com.hugosama.samalinne.api.update;

import com.hugosama.samalinne.api.update.entities.Version;
import com.hugosama.samalinne.data.entities.Message;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by hugo on 8/23/16.
 */
public interface UpdateService {

    @GET("/version")
    Call<Version> getVersion();

    @GET("/messages")
    Call<List<Message>> getMessages(@Query("date") long date);


}
