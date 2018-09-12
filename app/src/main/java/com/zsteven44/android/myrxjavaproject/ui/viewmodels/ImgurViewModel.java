package com.zsteven44.android.myrxjavaproject.ui.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.zsteven44.android.myrxjavaproject.model.ImgurGallery;
import com.zsteven44.android.myrxjavaproject.model.ImgurImage;
import com.zsteven44.android.myrxjavaproject.repository.ImgurRepository;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

public class ImgurViewModel extends AndroidViewModel {
    private CompositeDisposable disposables = new CompositeDisposable();
    private ImgurRepository imgurRepository;

    private LiveData<List<ImgurGallery>> imgurGalleries;
    private LiveData<List<ImgurImage>> imgurImages;
    private LiveData<String> searchType;
    private LiveData<String> searchWindow;
    private LiveData<String> searchTerm;
    private int page = 1;

    public ImgurViewModel(@NonNull final Application application) {
        super(application);
        this.imgurRepository = new ImgurRepository();
        this.imgurGalleries = imgurRepository.getCachedGalleries();
        this.searchTerm = imgurRepository.getCachedSearchTerm();
        this.searchType = imgurRepository.getCachedSearchType();
        this.searchWindow = imgurRepository.getCachedSearchWindow();
    }

    public void searchGalleries() {


    }

    /*
    This initializes the search fields to match what was last used.  Values are saved
    in locally using SharedPrefs and accessed via the repository.
     */



    public LiveData<List<ImgurGallery>> getGalleries() {
        if (imgurGalleries == null) {
            imgurGalleries = new MutableLiveData<List<ImgurGallery>>();
            imgurGalleries = imgurRepository.fetchGalleries(searchType, searchWindow, searchTerm, page);
        }
        return imgurGalleries;
    }

    public LiveData<String> getSearchType() {
        return searchType;
    }

    public LiveData<String> getSearchWindow() {
        return searchWindow;
    }

    public LiveData<String> getSearchTerm() {
        return searchTerm;
    }

    public void updateSearchSettings(@NonNull final String searchTerm,
                                     @NonNull final String searchType,
                                     @NonNull final String searchWindow) {

    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (!disposables.isDisposed()) disposables.dispose();
    }
}
