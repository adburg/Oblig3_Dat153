package com.example.oblig3_dat153.ui.gallery;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oblig3_dat153.R;

public class GalleryViewHolder extends RecyclerView.ViewHolder {

    ImageView imageView;
    Button button;
    TextView imageText;

    public GalleryViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.image_view);
        button = itemView.findViewById(R.id.delete_button);
        imageText = itemView.findViewById(R.id.image_text);
    }
}
