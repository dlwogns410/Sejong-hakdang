package com.pProject.ganada;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ExampleParsingRetrofitInterface {
    @GET("search?key=" + BuildConfig.API_KEY + "&req_type=json&part=exam&sort=popular&num=10")
    Call<JsonObject> getExample(@Query("q") String word);
}
