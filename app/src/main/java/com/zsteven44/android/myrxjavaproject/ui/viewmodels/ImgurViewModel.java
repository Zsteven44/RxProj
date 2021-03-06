package com.zsteven44.android.myrxjavaproject.ui.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.zsteven44.android.myrxjavaproject.model.ImgurGallery;
import com.zsteven44.android.myrxjavaproject.model.ImgurImage;
import com.zsteven44.android.myrxjavaproject.repository.ImgurRepository;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

public class ImgurViewModel extends AndroidViewModel {
    private CompositeDisposable disposables = new CompositeDisposable();
    private ImgurRepository imgurRepository;

    private LiveData<List<ImgurGallery>> imgurGalleries;
    private LiveData<List<ImgurImage>> imgurImages;
    private String searchType;
    private String searchWindow;
    private String searchTerm;
    private int page = 1;

    public ImgurViewModel(@NonNull final Application application) {
        super(application);
        this.imgurRepository = new ImgurRepository();
        this.imgurGalleries = imgurRepository.getCachedGalleries();
        this.searchTerm = imgurRepository.getCachedSearchTerm();
        this.searchType = imgurRepository.getCachedSearchType();
        this.searchWindow = imgurRepository.getCachedSearchWindow();
        Timber.d("ViewModel SearchType is: %s", searchType);
        Timber.d("ViewModel SearchWindow is: %s", searchWindow);
        Timber.d("ViewModel SearchTerm is: %s", searchTerm);
    }

    public void clearGalleries() {
        imgurRepository.clearGalleries();

    }

    public LiveData<List<ImgurGallery>> getGalleries(@NonNull final String type,
                                                     @NonNull final String window,
                                                     @NonNull final String term,
                                                     final int page ) {
        Timber.d("getting Galleries for: %s", term);
        if (imgurGalleries == null) {
            imgurGalleries = new MutableLiveData<List<ImgurGallery>>();
        }
        updateSearchParams(term, type, window);
        imgurGalleries = imgurRepository.fetchGalleries(type, window, term, page);
        return imgurGalleries;
    }

    public String getSearchType() {
        return searchType;
    }

    public String getSearchWindow() {
        return searchWindow;
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    public void updateSearchParams(@NonNull final String searchTerm,
                                     @NonNull final String searchType,
                                     @NonNull final String searchWindow) {
        imgurRepository.setCachedSearchParams(searchTerm, searchType, searchWindow);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (!disposables.isDisposed()) disposables.dispose();
    }
}
