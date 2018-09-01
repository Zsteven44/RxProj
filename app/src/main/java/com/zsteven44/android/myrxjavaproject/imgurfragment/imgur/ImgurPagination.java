package com.zsteven44.android.myrxjavaproject.imgurfragment.imgur;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

abstract public class ImgurPagination extends RecyclerView.OnScrollListener {
    private GridLayoutManager gridLayoutManager;
    private int visibleThreshold = 5;
    private int currentPage = 0;
    private int previousTotalItemCount = 0;
    private boolean loading = true;
    private int startingPageIndex = 0;

    protected ImgurPagination(@NonNull final RecyclerView.LayoutManager gridLayoutManager) {
        this.gridLayoutManager = (GridLayoutManager) gridLayoutManager;
    }

    @Override
    public void onScrolled(@NonNull final RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        int lastVisibleItemPosition = gridLayoutManager.findLastVisibleItemPosition();

        int totalItemCount = gridLayoutManager.getItemCount();

        if (totalItemCount < previousTotalItemCount) {
            this.currentPage = this.startingPageIndex;
            this.previousTotalItemCount = totalItemCount;
            if (totalItemCount == 0) {
                this.loading = true;
            }
        }

        if (loading && (totalItemCount > previousTotalItemCount)) {
            loading = false;
            previousTotalItemCount = totalItemCount;
        }

        if (!loading && (lastVisibleItemPosition + visibleThreshold) > totalItemCount) {
            currentPage++;
            onLoadMore(currentPage, totalItemCount, recyclerView);
            loading = true;
        }
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    abstract public void onLoadMore(final int currentPage,
                                    final int totalItemCount,
                                    @NonNull final View view);
}
