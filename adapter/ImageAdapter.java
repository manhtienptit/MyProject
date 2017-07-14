package com.seatgeek.placesautocompletedemo.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.seatgeek.placesautocompletedemo.Constants;
import com.seatgeek.placesautocompletedemo.R;
import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * Created by tranminhtue on 18/04/2017.
 */

public class ImageAdapter extends PagerAdapter {

    private static final String[] IMAGE_URLS = Constants.IMAGES;

    private LayoutInflater inflater;

    private DisplayImageOptions options;
    private Context ctx;

    public ImageAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        ctx = context;

        options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.ic_error)
                .resetViewBeforeLoading(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)
                .displayer(new FadeInBitmapDisplayer(300))
                .build();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return IMAGE_URLS.length;
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.item_pager_image, view, false);
        assert imageLayout != null;
        final ImageView imageView = (ImageView) imageLayout.findViewById(R.id.image);
        final ProgressBar spinner = (ProgressBar) imageLayout.findViewById(R.id.loading);

        Picasso.with(ctx)
                .load(IMAGE_URLS[position])
                .placeholder(R.color.gray_row_color)
                .error(R.drawable.ic_error)
                .resizeDimen(R.dimen.d_width,R.dimen.d_height)
                .centerInside()
                .tag(ctx)
                .into(imageView);

//        ImageLoader.getInstance().displayImage(IMAGE_URLS[position], imageView, options, new SimpleImageLoadingListener() {
//            @Override
//            public void onLoadingStarted(String imageUri, View view) {
//                spinner.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//                String message = null;
//                switch (failReason.getType()) {
//                    case IO_ERROR:
//                        message = "Input/Output error";
//                        break;
//                    case DECODING_ERROR:
//                        message = "Image can't be decoded";
//                        break;
//                    case NETWORK_DENIED:
//                        message = "Downloads are denied";
//                        break;
//                    case OUT_OF_MEMORY:
//                        message = "Out Of Memory error";
//                        break;
//                    case UNKNOWN:
//                        message = "Unknown error";
//                        break;
//                }
//                Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();
//
//                spinner.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                Glide
//                        .with(ctx)
//                        .load("http://anhnendep.net/wp-content/uploads/2016/02/vit-boi-roi-Psyduck.jpg")
//                        .fitCenter()
//                        .placeholder(R.drawable.ic_menu_location)
//                        .crossFade()
//                        .into(imageView);
//
//                spinner.setVisibility(View.GONE);
//            }
//        });

        view.addView(imageLayout, 0);
        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }
}
