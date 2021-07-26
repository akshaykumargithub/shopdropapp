package shop.dropapp.ui.productsection.adapter;


import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import shop.dropapp.R;
import shop.dropapp.base.activity.Ced_MainActivity;
import shop.dropapp.databinding.MagenativeImageBinding;
import shop.dropapp.utils.UpdateImage;

public class Ced_ProductViewImageFragment_forswatches extends Fragment
{
    String url;
    ImageView image;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState)
    {
        MagenativeImageBinding binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.magenative_image, null, false);
        try
        {
            ActivityManager.MemoryInfo memoryInfo = getAvailableMemory();
            if(!memoryInfo.lowMemory)
            {
                ImageView image= binding.MageNativeImage;
                final String url = getArguments().getString("url");
                final CharSequence stack[] = getArguments().getCharSequenceArray("stack");
                final int  position = getArguments().getInt("position");
                image.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Intent intent = new Intent(getActivity(), MageNative_ZoomImagePagerActivity.class);
                        intent.putExtra("IMAGEURL", stack);
                        intent.putExtra("POS", position);
                        startActivity(intent);
                    }
                });
                try
                {
                    if (!memoryInfo.lowMemory)
                    {
                       /* Glide.with(getActivity())
                                .load(url)
                                .dontTransform()
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(image);*/
                        UpdateImage.showImage(getActivity(),url,R.drawable.placeholder,image);
                        //new DownloadTask(image).execute(url);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
        catch (Exception e)
        {
            Intent main=new Intent(getActivity(), Ced_MainActivity.class);
            main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getActivity().startActivity(main);
            getActivity().overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
        }
        return binding.getRoot() ;
    }

    @NonNull
    private ActivityManager.MemoryInfo getAvailableMemory()
    {
        ActivityManager activityManager = (ActivityManager) getActivity().getSystemService(getActivity().ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager. getMemoryClass();
        return memoryInfo;
    }
}
