package com.zsteven44.android.myrxjavaproject.imgur;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.zsteven44.android.myrxjavaproject.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class ImgurAdapter<T extends ImgurItem> extends RecyclerView.Adapter<ImgurAdapter.ImgurViewHolder> {

    private List<T> itemList = new ArrayList<>();
    private int rowLayout;

    public ImgurAdapter(ArrayList<T> itemList,
                        int rowLayout) {
        this.itemList = itemList;
        this.rowLayout = rowLayout;

    }

    public void addItem(T item, int position) {
        itemList.add(position, item);
        notifyItemInserted(position);
    }

    public void addItemList(List<T> images){
        itemList.clear();
        itemList.addAll(images);
        notifyDataSetChanged();
    }

    @Override
    public ImgurViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater
                .from(parent.getContext())
                .inflate(rowLayout, parent, false);
        return new ImgurViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ImgurViewHolder holder, int position) {
        Timber.d("Running onBindViewHolder for item: %s", position);
        final T item = itemList.get(position);
        holder.title.setText(item.getTitle());
        holder.description.setText( item.getDescription());
        holder.downs.setText(String.valueOf(item.getDowns()));
        holder.ups.setText(String.valueOf(item.getUps()));
        Timber.d(item.toString());
        String imageLink = null;
        if (item instanceof ImgurGallery) {
            if (((ImgurGallery) item).getCover() == null) {
                if (!((ImgurGallery) item).getIsAlbum()) {
                    imageLink = item.getLink();
                } else {
                    imageLink = "";
                }
                Timber.d("Item is ImgurGallery with NULL cover link: %s", imageLink);
            } else {
                imageLink = "https://i.imgur.com/"
                        .concat(((ImgurGallery) item)
                                .getCover())
                        .concat(".jpg");
                Timber.d("Item is ImgurGallery with cover link: %s", imageLink);
            }

        } else if (item instanceof ImgurImage){
            Timber.d("Item is ImgurImage with link: %s", ((ImgurImage) item).getLink());
        }
//        if (imageLink != null && !imageLink.isEmpty()) {
        if (imageLink.isEmpty() || imageLink == null){
            Picasso.get()
                    .load(R.drawable.ic_launcher_foreground)
                    .resize(450, 450)
                    .into(holder.image);
        } else {
            Picasso.get()
                    .load(imageLink)
                    .resize(450, 450)
                    .into(holder.image, new Callback(){
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {
                            Timber.e("Picasso image Error: %s", e);
                        }
                    });
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

        public ImgurViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
