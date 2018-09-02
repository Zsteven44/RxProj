package com.zsteven44.android.myrxjavaproject.imgurfragment.imgur;

import android.support.annotation.NonNull;
import android.support.v4.widget.CircularProgressDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zsteven44.android.myrxjavaproject.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class ImgurAdapter<T extends ImgurItem> extends RecyclerView.Adapter<ImgurAdapter.ImgurViewHolder> {

    private List<T> itemList;
    private int rowLayout;

    public ImgurAdapter(@NonNull final ArrayList<T> itemList,
                        final int rowLayout) {
        this.itemList = itemList;
        this.rowLayout = rowLayout;

    }

    public void addItem(@NonNull final T item,
                        final int position) {
        itemList.add(position, item);
        notifyItemInserted(position);
    }

    public void addItemList(@NonNull final List<T> images,
                            final boolean addingToList){
        if (!addingToList)itemList.clear();
        itemList.addAll(images);
        notifyDataSetChanged();
    }


    @Override
    public ImgurViewHolder onCreateViewHolder(@NonNull final ViewGroup parent,
                                              final int viewType) {
        View v= LayoutInflater
                .from(parent.getContext())
                .inflate(rowLayout, parent, false);
        return new ImgurViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ImgurViewHolder holder,
                                 final int position) {
        final T item = itemList.get(position);
        holder.title.setText(item.getTitle());
        holder.description.setText( item.getDescription());
        holder.downs.setText(String.valueOf(item.getDowns()));
        holder.ups.setText(String.valueOf(item.getUps()));
        String imageLink = null;
        if (item instanceof ImgurGallery) {
            if (((ImgurGallery) item).getCover() == null) {
                if (!((ImgurGallery) item).getIsAlbum()) {
                    imageLink = item.getLink();
                } else {
                    imageLink = "";
                }
            } else {
                imageLink = "https://i.imgur.com/"
                        .concat(((ImgurGallery) item)
                                .getCover())
                        .concat(".jpg");
            }

        } else if (item instanceof ImgurImage){
            Timber.d("Item is ImgurImage with link: %s", ((ImgurImage) item).getLink());
        }

        if (imageLink == null || imageLink.isEmpty()) {
            Glide.with(holder.image.getContext())
                    .load(R.drawable.ic_launcher_foreground)
                    .into(holder.image);
        } else {
            CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(holder.image.getContext());
            circularProgressDrawable.setStrokeWidth(5f);
            circularProgressDrawable.setCenterRadius(30f);
            circularProgressDrawable.start();
            Glide.with(holder.image.getContext())
                    .load(imageLink)
                    .placeholder(circularProgressDrawable)
                    .into(holder.image);
        }
    }

    @Override
    public int getItemCount() {
        return itemList == null ? 0 : itemList.size();
    }

    public static class ImgurViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.row_imgur_image)ImageView image;
        @BindView(R.id.row_imgur_title)TextView title;
        @BindView(R.id.row_imgur_description)TextView description;
        @BindView(R.id.row_imgur_downs)TextView downs;
        @BindView(R.id.row_imgur_ups)TextView ups;

        public ImgurViewHolder(@NonNull final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
