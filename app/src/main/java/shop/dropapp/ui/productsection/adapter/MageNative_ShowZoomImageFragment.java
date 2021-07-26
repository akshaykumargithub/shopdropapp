package shop.dropapp.ui.productsection.adapter;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import shop.dropapp.R;
import shop.dropapp.databinding.FragmentShowZoomImageBinding;
import shop.dropapp.utils.UpdateImage;

public class MageNative_ShowZoomImageFragment extends Fragment
{

    //MageNative_ProductImageAdapter2 productImageAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        String currenturl = getArguments().getString("current");
        FragmentShowZoomImageBinding binding1 = DataBindingUtil.inflate(getLayoutInflater(), R.layout.fragment_show_zoom_image, null, false);
        binding1.MageNativeCross.setOnClickListener(v1 -> getActivity().onBackPressed());

        UpdateImage.showImage(getActivity(),currenturl,R.drawable.tab,binding1.MageNativeImage);

        /*Glide.with(getActivity())
                .load(currenturl)
                .placeholder(R.drawable.tab)
                .error(R.drawable.tab)
//                .override(320,320)
                .into(binding1.MageNativeImage);*/
        return  binding1.getRoot();
    }
}