package com.zsteven44.android.myrxjavaproject.imgur;

public class ImgurClient {
//    private String IMGUR_API_BASE_URL = "https://api.imgur.com/";
//
//    public ImgurGalleryList runSearchGalleryRequest(@NonNull final String sort,
//                                 @NonNull final String window,
//                                 @Nullable final String search) {
//        Timber.d("Running ImgurClient.runSearchGalleryRequest...");
//
//        OkHttpClient.Builder okhttpclientBuilder = new OkHttpClient.Builder();
//
//        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
//                .baseUrl(IMGUR_API_BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create());
//
//        Retrofit retrofit = retrofitBuilder
//                .client(okhttpclientBuilder.build())
//                .build();
//
//        ImgurService imgurService = retrofit.create(ImgurService.class);
//
//        try {
//            Call<ResponseBody> call = imgurService
//                    .getSearchGallery(sort, window, 1, search);
//
//            call.enqueue(new Callback<ImgurGalleryList>() {
//                @Override
//                public void onResponse(Call<ImgurGalleryList> call,
//                                       Response<ImgurGalleryList> response) {
//                    Timber.d(response.body().toString());
//                }
//
//                @Override
//                public void onFailure(Call<ImgurGalleryList> call,
//                                      Throwable t) {
//                    Timber.e(t);
//                }
//            });
//
//
//            return
//        } catch(IOException e) {
//            Timber.e(e);
//        } finally {
//            Timber.d("returning null");
//            return null;
//        }
//    }
}
