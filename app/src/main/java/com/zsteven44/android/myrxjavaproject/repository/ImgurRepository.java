package com.zsteven44.android.myrxjavaproject.repository;

import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.zsteven44.android.myrxjavaproject.MyRxApplication;
import com.zsteven44.android.myrxjavaproject.api.ImgurService;
import com.zsteven44.android.myrxjavaproject.api.ServiceGenerator;
import com.zsteven44.android.myrxjavaproject.model.ImgurGallery;
import com.zsteven44.android.myrxjavaproject.model.ImgurGalleryDao;
import com.zsteven44.android.myrxjavaproject.model.ImgurGalleryList;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import timber.log.Timber;

import static com.zsteven44.android.myrxjavaproject.utils.AppConstants.IMGUR_API_BASE_URL;

/*
    This class is responsible for handling multiple data sources. These include any online
    APIs and the device local storage.  It "is the single source of truth for all app
    data", a clean api that communicates with the ViewModel.
 */
public class ImgurRepository {
    private ImgurDatabase imgurDatabase;
    private ImgurGalleryDao galleryDao;
    private CompositeDisposable disposables;

    public ImgurRepository(){
        this.imgurDatabase = ImgurDatabase.getInstance(MyRxApplication.getAppContext());
        this.galleryDao = imgurDatabase.getGalleryDao();
        disposables = new CompositeDisposable();
    }

    public MutableLiveData<List<ImgurGallery>> fetchGalleries(@NonNull final String searchType,
                                                              @NonNull final String searchWindow,
                                                              @NonNull final String searchTerm,
                                                              final int resultsPage){
        requestGalleries(searchType, searchWindow, searchTerm, resultsPage);
        return galleryDao.getAll();
    }

    private void requestGalleries(@NonNull final String searchType,
                               @NonNull final String searchWindow,
                               @NonNull final String searchTerm,
                               final int resultsPage) {
        Timber.d("Running fetchGalleries with arguments:\nsort='%s' \nwindow='%s'\nsearch='%s'\npage='%s'",
                searchType,
                searchWindow,
                searchTerm,
                resultsPage);
        ServiceGenerator.changeApiBaseUrl(IMGUR_API_BASE_URL);
        ImgurService service = ServiceGenerator.createService(ImgurService.class);
        Timber.d("finishing fetchGalleries request.");
        disposables.add(service.getSearchGallery(searchType,searchWindow,resultsPage,searchTerm)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Response<ImgurGalleryList>>() {
                    @Override
                    public void accept(@NonNull final Response<ImgurGalleryList> response) throws Exception {
                        Timber.d("Consumer is subscribed to imgurGalleryObservable.");
                        Timber.d(response.body().toString());
                        List<ImgurGallery> galleries = response.body().getData();
                        galleryDao.insertAll(galleries);
                    }

                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Timber.e(throwable);
                    }
                }));
    }

    public void clearGalleries() {
        galleryDao.deleteAll();
    }

}