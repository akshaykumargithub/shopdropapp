/*
 * Copyright/**
 *          * CedCommerce
 *           *
 *           * NOTICE OF LICENSE
 *           *
 *           * This source file is subject to the End User License Agreement (EULA)
 *           * that is bundled with this package in the file LICENSE.txt.
 *           * It is also available through the world-wide-web at this URL:
 *           * http://cedcommerce.com/license-agreement.txt
 *           *
 *           * @category  Ced
 *           * @package   MageNative
 *           * @author    CedCommerce Core Team <connect@cedcommerce.com >
 *           * @copyright Copyright CEDCOMMERCE (http://cedcommerce.com/)
 *           * @license      http://cedcommerce.com/license-agreement.txt
 *
 */
package shop.dropapp.ui.productsection.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import shop.dropapp.R;
import shop.dropapp.databinding.MagenativeFragmentShowZoomImageBinding;
import shop.dropapp.utils.UpdateImage;


public class Ced_ShowZoomImageFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        String currenturl = getArguments().getString("current");

        MagenativeFragmentShowZoomImageBinding binding1 = DataBindingUtil.inflate(getLayoutInflater(), R.layout.magenative_fragment_show_zoom_image, null, false);
        binding1.MageNativeCross.setOnClickListener(v1 -> getActivity().onBackPressed());
        Log.e("frozen", "currenturl=== " + currenturl);
     /*   Glide.with(getActivity())
                .load(currenturl)
                .placeholder(R.drawable.tab)
                .error(R.drawable.tab)
                .into(binding1.MageNativeImage);*/

        UpdateImage.showImage(getContext(),currenturl,R.drawable.tab,binding1.MageNativeImage);
        return binding1.getRoot();
    }
}