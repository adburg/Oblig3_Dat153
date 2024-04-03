package com.example.oblig3_dat153.ui.gallery;

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

    // Fyller data inn i xml filen
    private LayoutInflater inflater;

    // Lytter for eventer
    private OnItemClickListener listener;

    // Interface for click events
    public interface OnItemClickListener {
        void onDeleteClick(PhotoEntry image);
    }

    public GalleryAdapter(Context context, List<PhotoEntry> galleryItems, OnItemClickListener listener) {
       this.galleryItems = galleryItems;
       this.inflater = LayoutInflater.from(context);
       this.listener = listener;
    }

    /**
     * Denne metoden oppretter ViewHodleren og fyller den med data. Dette betyr at den fyller gallery item  emd data.
     * Denne metoden kalles på hver gang RecyclerView trenger å opprette et nytt item.
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     *               an adapter position.
     * @param viewType The view type of the new View.
     *
     * @return
     */
    @NonNull
    @Override
    public GalleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_gallery, parent, false);
        return new GalleryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryViewHolder holder, int position) {
        PhotoEntry imageEntry = galleryItems.get(position);
        Glide.with(holder.imageView.getContext()).load(imageEntry.getUrl()).into(holder.imageView);
        holder.imageText.setText(imageEntry.getName());

        // Setter opp onClick
        holder.imageButton.setOnClickListener(v -> listener.onDeleteClick(imageEntry));
    }

    @Override
    public int getItemCount() {
        return galleryItems.size();
    }
}
