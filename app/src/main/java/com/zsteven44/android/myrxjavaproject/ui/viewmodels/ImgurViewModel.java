package com.zsteven44.android.myrxjavaproject.ui.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.zsteven44.android.myrxjavaproject.api.ImgurService;
import com.zsteven44.android.myrxjavaproject.api.ServiceGenerator;
import com.zsteven44.android.myrxjavaproject.model.ImgurGallery;
import com.zsteven44.android.myrxjavaproject.model.ImgurGalleryList;
import com.zsteven44.android.myrxjavaproject.model.ImgurImage;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import timber.log.Timber;

import static com.zsteven44.android.myrxjavaproject.utils.AppConstants.IMGUR_API_BASE_URL;

public class ImgurViewModel extends ViewModel {
    private CompositeDisposable disposables = new CompositeDisposable();

    private MutableLiveData<List<ImgurGallery>> imgurGalleries;
    private MutableLiveData<List<ImgurImage>> imgurImages;

    public LiveData<List<ImgurGallery>> getGalleries(@NonNull final String searchType,
                                                     @NonNull final String searchWindow,
                                                     @NonNull final String searchTerm,
                                                     final int resultsPage,
                                                     final boolean addingToList) {
        if (imgurGalleries == null) {
            imgurGalleries = new MutableLiveData<List<ImgurGallery>>();
            loadGalleries(searchType,
                searchWindow,
                searchTerm,
                resultsPage,
                addingToList);
        }
        return imgurGalleries;
    }

    private void loadGalleries(@NonNull final String searchType,
                                @NonNull final String searchWindow,
                                @NonNull final String searchTerm,
                                final int resultsPage,
                                final boolean addingToList) {
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
                        if (addingToList) {
                            List<ImgurGallery> currentList = ((LiveData<List<ImgurGallery>>) imgurGalleries).getValue();
                            currentList.addAll(galleries);
                            imgurGalleries.postValue(currentList);
                        } else {
                            imgurGalleries.postValue(galleries);
                        }
                        imgurGalleries.postValue(response.body().getData());

                    }

                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Timber.e(throwable);
                    }
                }));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (!disposables.isDisposed()) disposables.dispose();
    }
}
