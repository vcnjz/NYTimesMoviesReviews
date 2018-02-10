package com.example.workstation.nytimesmoviesreviews.Model;

import com.example.workstation.nytimesmoviesreviews.Model.Results;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Workstation1 on 10.02.2018.
 */

public interface NYTimesApi {


    @GET("reviews/{resource-type}.json")
    Observable<Results> getReviews(@Path("resource-type") String resourceType, @Query("offset") int offset, @Query("api-key") String apiKey);
}
