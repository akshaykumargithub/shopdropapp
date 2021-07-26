package shop.dropapp.utils;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.squareup.picasso.Picasso;

import java.io.File;

import shop.dropapp.R;

public class UpdateImage {
    public static void showImage(Context context, String ImageUrl, int placeholder, ImageView imageView) {
        Glide.with(context)
                .load(ImageUrl)
                .dontTransform()
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(placeholder)
                .error(placeholder)
                .into(imageView);

    /*    Picasso.get()
                .load(ImageUrl)
                .placeholder(placeholder)
                .into(imageView);*/

   //     Log.i("90448484", "showImage: " + ImageUrl);

    }
}
