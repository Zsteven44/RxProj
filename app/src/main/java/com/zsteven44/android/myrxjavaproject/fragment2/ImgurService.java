package com.zsteven44.android.myrxjavaproject.fragment2;

import com.zsteven44.android.myrxjavaproject.imgur.ImgurGalleryList;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ImgurService {

    @Headers("Authorization: Client-ID 28710bba9a6f38c")
    @GET("/3/gallery/search/{sort}/{window}/{page}")
    Observable<ImgurGalleryList> getSearchGallery(@Path("sort")String sort,
                                                  @Path("window")String window,
                                                  @Path("page")int page,
                                                  @Query("q")String search);


}
