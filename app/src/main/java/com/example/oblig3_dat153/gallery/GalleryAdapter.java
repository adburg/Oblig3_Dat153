package com.example.oblig3_dat153.gallery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.oblig3_dat153.PhotoEntry;
import com.example.oblig3_dat153.R;

import java.util.List;

/**
 * Adaptern gir RecyclerView funksjonalitet
 */
public class GalleryAdapter extends RecyclerView.Adapter<GalleryViewHolder> {

    private List<PhotoEntry> galleryItems;
    private LayoutInflater inflater;
    private OnItemClickListener listener;

    // Interface for click events, used for creating a delete function in GalleryActivity
    public interface OnItemClickListener {
        void onDeleteClick(PhotoEntry image);
    }

    public GalleryAdapter(Context context, List<PhotoEntry> galleryItems, OnItemClickListener listener) {
       this.galleryItems = galleryItems;
       this.inflater = LayoutInflater.from(context);
       this.listener = listener;
    }

    // Sets the PhotoEntries
    public void setGalleryItems(List<PhotoEntry> galleryItems) {
        this.galleryItems = galleryItems;
        notifyDataSetChanged(); // Notify any registered observers that the data set has changed.
    }

    // Creates a single instance of a gallery item
    @NonNull
    @Override
    public GalleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_gallery, parent, false);
        return new GalleryViewHolder(view);
    }

    // Puts data in a single instance of a gallery item
    // We use a third party library called Glide for inserting images
    @Override
    public void onBindViewHolder(@NonNull GalleryViewHolder holder, int position) {
        PhotoEntry imageEntry = galleryItems.get(position);
        Glide.with(holder.imageView.getContext()).load(imageEntry.getUrl()).into(holder.imageView);
        holder.imageText.setText(imageEntry.getName());

        // Setter opp onClick
        holder.button.setOnClickListener(v -> listener.onDeleteClick(imageEntry));
    }

    @Override
    public int getItemCount() {
        return galleryItems.size();
    }
}
