package com.zsteven44.android.myrxjavaproject.ui.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.jakewharton.rxbinding2.view.RxView;
import com.zsteven44.android.myrxjavaproject.R;
import com.zsteven44.android.myrxjavaproject.api.ImgurService;
import com.zsteven44.android.myrxjavaproject.api.ServiceGenerator;
import com.zsteven44.android.myrxjavaproject.model.ImgurGallery;
import com.zsteven44.android.myrxjavaproject.model.ImgurGalleryList;
import com.zsteven44.android.myrxjavaproject.ui.adapters.ImgurAdapter;
import com.zsteven44.android.myrxjavaproject.ui.components.ImgurPagination;
import com.zsteven44.android.myrxjavaproject.ui.viewmodels.ImgurViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.processors.PublishProcessor;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;
import retrofit2.Response;
import timber.log.Timber;

import static com.zsteven44.android.myrxjavaproject.utils.AppConstants.IMGUR_API_BASE_URL;

public class ImgurFragment extends Fragment {

    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.search_button) Button searchButton;
    @BindView(R.id.search_text) EditText searchText;

    private Unbinder unbinder;
    private CompositeDisposable disposables;

    private ImgurViewModel imgurViewModel;
    private Observer<List<ImgurGallery>> galleryListObserver;

    private ImgurAdapter<ImgurGallery> adapter;
    private GridLayoutManager layoutManager;

    private PublishProcessor<Integer> pagination;
    private ImgurPagination imgurPagination;

    private SearchSort searchSort;
    private SearchWindow searchWindow;
    private String searchString;

    private enum SearchWindow{
        day,
        week,
        month,
        year,
        all;
    }
    private enum SearchSort{
        time,
        viral,
        top;
    }

    public ImgurFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_imgur, container, false);
        unbinder = ButterKnife.bind(this, view);
        imgurViewModel = ViewModelProviders.of(this).get(ImgurViewModel.class);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull final View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        galleryListObserver = new Observer<List<ImgurGallery>>() {
            @Override
            public void onChanged(@Nullable List<ImgurGallery> galleryList) {
                adapter.addItemList(galleryList != null ? galleryList : new ArrayList<ImgurGallery>(), false);
            }
        };
        if (imgurViewModel.getGalleries().getValue().isEmpty())imgurViewModel.getGalleries().observe(this,galleryListObserver);

        // RecyclerView, Adapter, LayoutManager
        layoutManager =new GridLayoutManager(getActivity(),
                2,
                GridLayoutManager.VERTICAL,
                false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ImgurAdapter<ImgurGallery>(new ArrayList<ImgurGallery>(),
                R.layout.row_layout_imgur);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(10);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setAdapter(adapter);
        // ScrollListener
        pagination = PublishProcessor.create();
        imgurPagination = new ImgurPagination(layoutManager) {
            @Override
            public void onLoadMore(int currentPage, int totalItemCount, @NonNull View view) {
                Timber.d("onLoadMoreTriggered, current page: %s and current itemCount: %s", currentPage, totalItemCount);
                pagination.onNext(currentPage);
            }
        };
        recyclerView.addOnScrollListener(imgurPagination);
        Disposable disposable = pagination
                .onBackpressureDrop()
                .debounce(2, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(integer -> {
                    Timber.d("doOnNext pagination triggered.");
                    ImgurFragment.this.fetchGalleries(searchSort.name(),
                            searchWindow.name(),
                            searchString,
                            integer,
                            true);
                })
                .doOnError(throwable -> {
                    if (throwable instanceof HttpException) {
                        Response<?> response = ((HttpException) throwable).response();
                        Timber.d(response.message());
                    }
                })
                .subscribe();
        disposables.add(disposable);
        // RxBinding Search Button
        disposables= new CompositeDisposable();
        disposables
                .add(RxView
                        .clicks(searchButton)
                        .debounce(2, TimeUnit.SECONDS)
                        .subscribe(new Consumer<Object>() {
                            @Override
                            public void accept(Object aVoid) throws Exception {
                                Timber.d("SearchButton 'RxView.clicks' registered.");
                                imgurPagination.setCurrentPage(1);
                                searchWindow = SearchWindow.day;
                                searchSort = SearchSort.top;
                                searchString = searchText.getText().toString();
                                ImgurFragment.this.fetchGalleries(searchSort.name(),
                                        searchWindow.name(),
                                        searchString,
                                        1,
                                        false);
                                recyclerView.scrollToPosition(0);
                            }
                        }));


    }


    private void fetchGalleries(@NonNull final String searchType,
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
                        adapter.addItemList(response.body().getData(), addingToList);
                    }

                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Timber.e(throwable);
                    }
                }));
    }




    @Override
    public void onDestroy() {
        unbinder.unbind();
        if (this.disposables != null && !this.disposables.isDisposed()) {
            this.disposables.dispose();
        }
        super.onDestroy();
    }
}
