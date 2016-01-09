package com.srinath.hcfab.under25hack;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * Created by darshan on 31/10/15.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.Holder> {

    Context context;
    public static DisplayImageOptions defaultOptions;
    ImageLoaderConfiguration config;
    ImageLoader imageLoader;

    public RecyclerViewAdapter(Context context) {
        this.context = context;
        // for dynamically loading images
        defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc(true).cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
//                .showImageOnLoading(R.drawable.ic_stub) // resource or drawable
//                .showImageForEmptyUri(R.drawable.ic_empty) // resource or drawable
//                .showImageOnFail(R.drawable.ic_error) // resource or drawable
                .displayer(new SimpleBitmapDisplayer()).build();

        config = new ImageLoaderConfiguration.Builder(
                context)
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .discCacheSize(100 * 1024 * 1024).build();

        ImageLoader.getInstance().init(config);
        imageLoader = ImageLoader.getInstance();
    }


    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_song_card, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("position",position);
                context.startActivity(intent);
            }
        });

//        setData(holder,position);
    }

   /* private void setData(final Holder holder, int position) {

        String rentCost = context.getResources().getString(R.string.Rs) + fetchData.dataList.get(position).price;
        holder.priceTv.setText(rentCost);
        holder.ratingTv.setText(fetchData.dataList.get(position).rating);
        holder.addressTv.setText(fetchData.dataList.get(position).address);
        holder.categoryTv.setText(fetchData.dataList.get(position).category);
//        imageLoader.displayImage(fetchData.dataList.get(position).image, (ImageAware) holder.imageLayout, defaultOptions);
        imageLoader.loadImage("http://84.200.84.218/grab/imagesa/"+fetchData.dataList.get(position).image, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                // Do whatever you want with Bitmap
                Drawable drawable = new BitmapDrawable(loadedImage);
                holder.imageLayout.setBackground(drawable);
            }
        });
    }*/

    @Override
    public int getItemCount() {
        return 20;
    }

    public class Holder extends RecyclerView.ViewHolder {

        RelativeLayout imageLayout;
        TextView song_name,ratingTv,description,artist;

        public Holder(View itemView) {
            super(itemView);
            imageLayout = (RelativeLayout) itemView.findViewById(R.id.rel1);
            song_name = (TextView) itemView.findViewById(R.id.song_name);
            ratingTv = (TextView) itemView.findViewById(R.id.rating);
            description = (TextView) itemView.findViewById(R.id.description);
            artist = (TextView) itemView.findViewById(R.id.artist);
        }
    }
}
