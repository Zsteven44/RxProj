package com.zsteven44.android.myrxjavaproject.api;

import com.zsteven44.android.myrxjavaproject.model.ImgurGalleryList;

import io.reactivex.Flowable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ImgurService {

    @Headers("Authorization: Client-ID 28710bba9a6f38c")
    @GET("/3/gallery/search/{sort}/{window}/{page}")
    Flowable<Response<ImgurGalleryList>> getSearchGallery(@Path("sort")String sort,
                                                         @Path("window")String window,
                                                         @Path("page")int page,
                                                         @Query("q")String search);


}
