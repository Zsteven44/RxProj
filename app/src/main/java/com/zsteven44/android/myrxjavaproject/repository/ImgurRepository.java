package com.zsteven44.android.myrxjavaproject.repository;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.zsteven44.android.myrxjavaproject.MyRxApplication;
import com.zsteven44.android.myrxjavaproject.api.ImgurService;
import com.zsteven44.android.myrxjavaproject.api.ServiceGenerator;
import com.zsteven44.android.myrxjavaproject.di.AppComponent;
import com.zsteven44.android.myrxjavaproject.di.AppModule;
import com.zsteven44.android.myrxjavaproject.di.DaggerAppComponent;
import com.zsteven44.android.myrxjavaproject.model.ImgurGallery;
import com.zsteven44.android.myrxjavaproject.model.ImgurGalleryDao;
import com.zsteven44.android.myrxjavaproject.model.ImgurGalleryList;
import com.zsteven44.android.myrxjavaproject.repository.utils.CachedData;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import timber.log.Timber;

import static com.zsteven44.android.myrxjavaproject.utils.AppConstants.IMGUR_API_BASE_URL;

/**
 * This class is responsible for consolidating/handling data from network requests,
 * shared preferences and local db. ImgurViewModel will retrieve data from
 * this repository.
 */
public class ImgurRepository {
    private ImgurDatabase imgurDatabase;
    private ImgurGalleryDao galleryDao;

    private CompositeDisposable disposables;

    @Inject public CachedData cachedData;

    private LiveData<List<ImgurGallery>> imgurGalleries;


    /**
     * On creation, gallery objects cached in Database will be retried and Dagger
     * will inject an instance of CachedData and its dependencies.
     */
    public ImgurRepository(){
        this.imgurDatabase = ImgurDatabase.getInstance(MyRxApplication.getAppContext());
        this.galleryDao = imgurDatabase.getGalleryDao();
        disposables = new CompositeDisposable();
        AppComponent appComponent = DaggerAppComponent
                .builder()
                .appModule(new AppModule(MyRxApplication.getInstance()))
                .build();
        appComponent.injectCachedData(this);
        Timber.d("ImgurRepository created.");
    }

    public String getCachedSearchTerm() {
        return cachedData.getCachedSearchTerm();
    }

    public String getCachedSearchWindow(){
        return cachedData.getCachedSearchWindow();
    }
    public String getCachedSearchType(){
        return cachedData.getCachedSearchType();
    }

    public void setCachedSearchParams(@NonNull final String term,
                                    @NonNull final String type,
                                    @NonNull final String window) {
        cachedData.setCachedSearchParams(term, type, window);
    }

    public LiveData<List<ImgurGallery>> getCachedGalleries() {
        return this.imgurGalleries;
    }

    public LiveData<List<ImgurGallery>> fetchGalleries(@NonNull final String searchType,
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
                .observeOn(Schedulers.io())
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

    public void
    clearGalleries() {
        galleryDao.deleteAll();
    }

}
