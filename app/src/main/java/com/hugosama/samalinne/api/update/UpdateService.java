package com.hugosama.samalinne.api.update;

import com.hugosama.samalinne.api.update.entities.Version;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by hugo on 8/23/16.
 */
public interface UpdateService {

    @GET("/version")
    Call<Version> getVersion();


}
