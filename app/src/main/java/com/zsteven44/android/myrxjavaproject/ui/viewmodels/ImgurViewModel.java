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

    }

    public void searchGalleries() {


    }

    public LiveData<List<ImgurGallery>> getGalleries() {
        if (imgurGalleries == null) {
            imgurGalleries = new MutableLiveData<List<ImgurGallery>>();
            imgurGalleries = imgurRepository.fetchGalleries(searchType, searchWindow, searchTerm, page);
        }
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

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public void setSearchWindow(String searchWindow) {
        this.searchWindow = searchWindow;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (!disposables.isDisposed()) disposables.dispose();
    }
}
