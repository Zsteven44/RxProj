package com.zsteven44.android.myrxjavaproject.imgurfragment;

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
import com.zsteven44.android.myrxjavaproject.imgurfragment.imgur.ImgurAdapter;
import com.zsteven44.android.myrxjavaproject.imgurfragment.imgur.ImgurGallery;
import com.zsteven44.android.myrxjavaproject.imgurfragment.imgur.ImgurGalleryList;
import com.zsteven44.android.myrxjavaproject.imgurfragment.imgur.ImgurPagination;
import com.zsteven44.android.myrxjavaproject.services.ServiceGenerator;

import org.reactivestreams.Publisher;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.processors.PublishProcessor;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;
import retrofit2.Response;
import timber.log.Timber;

import static com.zsteven44.android.myrxjavaproject.AppConstants.IMGUR_API_BASE_URL;

public class ImgurFragment extends Fragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.search_button)
    Button searchButton;

    @BindView(R.id.search_text)
    EditText searchText;

    private Unbinder unbinder;
    private ImgurAdapter<ImgurGallery> adapter;
    private CompositeDisposable disposables;
    private Flowable<Response<ImgurGalleryList>> imgurGalleryObservable;
    private SearchSort searchSort;
    private SearchWindow searchWindow;
    private String searchString;
    private int page= 0;

    private PublishProcessor<Integer> pagination;
    private boolean requestOnWay = false;

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
        return view;
    }

    @Override
    public void onViewCreated(@NonNull final View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pagination = PublishProcessor.create();
        GridLayoutManager layoutManager =new GridLayoutManager(getActivity(),
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
        disposables= new CompositeDisposable();
        disposables
                .add(RxView
                        .clicks(searchButton)
                        .subscribe(new Consumer<Object>() {
                            @Override
                            public void accept(Object aVoid) throws Exception {
                                Timber.d("SearchButton 'RxView.clicks' registered.");
                                searchWindow = SearchWindow.day;
                                searchSort = SearchSort.top;
                                searchString = searchText.getText().toString();
                                page = 1;
                                ImgurFragment.this.loadGalleries(searchSort.name(),
                                        searchWindow.name(),
                                        searchString,
                                        page,
                                        false);
                                recyclerView.smoothScrollToPosition(0);
                            }
                        }));
        recyclerView.addOnScrollListener(new ImgurPagination(layoutManager) {
            @Override
            public void onLoadMore(int currentPage, int totalItemCount, @NonNull View view) {
                if (!requestOnWay) pagination.onNext(currentPage);
            }
        });
        Disposable disposable = pagination
                .onBackpressureDrop()
                .doOnNext(integer -> {
                    requestOnWay = true;
                    // TODO progress bar fuckery
                })
                .concatMap((Function<Integer, Publisher<Response<ImgurGalleryList>>>) integer ->
                        loadGalleries(searchSort.name(),
                        searchWindow.name(),
                        searchString,
                        integer,
                        false))
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(imgurGalleryList -> {
                    if (imgurGalleryList.isSuccessful()) {
                        adapter.addItemList(imgurGalleryList.body().getData(), true);
                    } else {
                        Timber.e(String.valueOf(imgurGalleryList.code())+" "+imgurGalleryList.message());
                    }
                    requestOnWay = false;
//                        loadUser.setVisibility(View.INVISIBLE);
                })
                .doOnError(throwable -> {
                    if (throwable instanceof HttpException) {
                        Response<?> response = ((HttpException) throwable).response();
                        Timber.d(response.message());
                    }

                })
                .subscribe();

        disposables.add(disposable);
    }

    private Flowable<Response<ImgurGalleryList>> loadGalleries(@NonNull final String searchType,
                                                 @NonNull final String searchWindow,
                                                 @NonNull final String searchTerm,
                                                 final int resultsPage,
                                                 final boolean addingToList) {
        Timber.d("Running loadGalleries with arguments:\nsort='%s' \nwindow='%s'\nsearch='%s'\npage='%s'",
                searchType,
                searchWindow,
                searchTerm,
                resultsPage);
        ServiceGenerator.changeApiBaseUrl(IMGUR_API_BASE_URL);
        ImgurService service = ServiceGenerator.createService(ImgurService.class);
        imgurGalleryObservable = service.getSearchGallery(searchType,searchWindow,resultsPage,searchTerm);
        return imgurGalleryObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());


//        disposables
//                .add(imgurGalleryObservable
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribeWith(new DisposableFlowa<ImgurGalleryList>() {
//                        @Override
//                        public void onSuccess(ImgurGalleryList imgurGalleryList) {
//                            Timber.d("Running imgurGalleryObservable 'onSuccess': %s",
//                                    imgurGalleryList.toString());
//                            adapter.addItemList(imgurGalleryList.getData(), addingToList);
//                        }
//                        @Override
//                        public void onError(Throwable e) {
//                            Timber.e(e);
//                        }
//                    }));
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
