package com.zsteven44.android.myrxjavaproject.ui.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxAdapterView;
import com.zsteven44.android.myrxjavaproject.MyRxApplication;
import com.zsteven44.android.myrxjavaproject.R;
import com.zsteven44.android.myrxjavaproject.model.ImgurGallery;
import com.zsteven44.android.myrxjavaproject.ui.adapters.ImgurAdapter;
import com.zsteven44.android.myrxjavaproject.ui.components.ImgurPagination;
import com.zsteven44.android.myrxjavaproject.ui.viewmodels.ImgurViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

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

public class ImgurFragment extends Fragment {

    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.search_button) Button searchButton;
    @BindView(R.id.search_text) EditText searchText;
    @BindView(R.id.type_spinner) Spinner typeSpinner;
    @BindView(R.id.window_spinner) Spinner windowSpinner;

    private Unbinder unbinder;
    private CompositeDisposable disposables;

    private ImgurViewModel imgurViewModel;
    private Observer<List<ImgurGallery>> galleryListObserver;

    @Inject public Resources resources;

    private ImgurAdapter<ImgurGallery> adapter;
    private GridLayoutManager layoutManager;

    private PublishProcessor<Integer> pagination;
    private ImgurPagination imgurPagination;

    private int searchType;
    private int searchWindow;
    private String searchString;


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
    public void onAttach(Context context) {
        ((MyRxApplication) context.getApplicationContext()).getAppComponent().inject(this);
        super.onAttach(context);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull final View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /*
         * Init widgets, observers, view selections
         */
        disposables= new CompositeDisposable();
        initSpinners();
        initObservers();
        /*
         * RecyclerView, Adapter, LayoutManager setup
         */
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
        /*
         * ScrollListener / Pagination
         */
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
                    imgurViewModel.getGalleries(resources.getStringArray(R.array.search_type_array)[searchType],
                            resources.getStringArray(R.array.search_window_array)[searchWindow],
                            searchString,
                            integer);
                })
                .doOnError(throwable -> {
                    if (throwable instanceof HttpException) {
                        Response<?> response = ((HttpException) throwable).response();
                        Timber.d(response.message());
                    }
                })
                .subscribe();
        disposables.add(disposable);
        /*
         * RxBinding Search Button
         */
        disposables
                .add(RxView
                        .clicks(searchButton)
                        .debounce(2, TimeUnit.SECONDS)
                        .subscribe(new Consumer<Object>() {
                            @Override
                            public void accept(Object aVoid) throws Exception {
                                Timber.d("SearchButton 'RxView.clicks registered.");
                                imgurPagination.setCurrentPage(1);
                                searchString = searchText.getText().toString();
                                imgurViewModel.clearGalleries();

                                imgurViewModel.getGalleries(resources.getStringArray(R.array.search_type_array)[searchType],
                                        resources.getStringArray(R.array.search_window_array)[searchWindow],
                                        searchString,
                                        1);
                            }
                        }));



    }

    private void initSpinners() {
        this.searchText.setText(imgurViewModel.getSearchTerm());
        // window spinner adapter
        ArrayAdapter<CharSequence> windowAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.search_window_array, android.R.layout.simple_spinner_item);
        windowAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        windowSpinner.setAdapter(windowAdapter);
        // type spinner adapter
        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.search_type_array, android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeAdapter);
        Timber.d("ImgurFragment SearchType is: %s", searchType);
        Timber.d("ImgurFragment SearchWindow is: %s", searchWindow);
        searchWindow = Arrays.asList(resources.getStringArray(R.array.search_window_array)).indexOf(imgurViewModel.getSearchWindow());
        searchType = Arrays.asList(resources.getStringArray(R.array.search_type_array)).indexOf(imgurViewModel.getSearchType());
        Timber.d("ImgurFragment new SearchType is: %s", searchType);
        Timber.d("ImgurFragment new SearchWindow is: %s", searchWindow);
        typeSpinner.setSelection(searchType);
        windowSpinner.setSelection(searchWindow);
        // Set spinner item select listeners
        disposables.add(RxAdapterView.itemSelections(windowSpinner)
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        searchWindow = integer;
                        Timber.d("WindowSpinner selection changed to: %s", integer);
                    }
                }));
        disposables.add(RxAdapterView.itemSelections(typeSpinner)
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        searchType = integer;

                        Timber.d("TypeSpinner selection changed to: %s", integer);
                    }
                }));

        // initialize spinner values


    }

    private void initObservers() {
        imgurViewModel
                .getGalleries(
                        imgurViewModel.getSearchType(),
                        imgurViewModel.getSearchWindow(),
                        imgurViewModel.getSearchTerm(),
                        1
                        ).observe(this, new Observer<List<ImgurGallery>>() {
            @Override
            public void onChanged(@Nullable List<ImgurGallery> imgurGalleries) {
                adapter.addItemList(imgurGalleries != null ? imgurGalleries : new ArrayList<ImgurGallery>());
            }
        });
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
