package ru.evgentrigub.android.photogallery;

import android.app.Application;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ImageViewHolder extends RecyclerView.ViewHolder {

    private ImageView ivItem;;

    public ImageViewHolder(View itemView) {
        super(itemView);
        ivItem = itemView.findViewById(R.id.iv_item);
    }

    public static ImageViewHolder inflate(ViewGroup parent){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_item, parent, false);
        return new ImageViewHolder(view);
    }

    void setResource(GalleryItem item, Context context){
        Picasso.with(context)
                .load(item.getUrl())
                .noFade()
                .into(ivItem);
    }

}
