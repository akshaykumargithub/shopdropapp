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
package shop.dropapp.ui.introsection.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import shop.dropapp.Ced_MageNative_FontSetting.Ced_FontSetting;
import shop.dropapp.R;

import java.util.Objects;

public class Ced_Illustration_3 extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.magenative_illustraion_3, null);

        ImageView image = v.findViewById(R.id.MageNative_image);
        TextView get_started = v.findViewById(R.id.get_started);
        TextView demotext1 = v.findViewById(R.id.demotext1);
        TextView demotext2 = v.findViewById(R.id.demotext2);
        TextView demotext3 = v.findViewById(R.id.demotext3);

        Ced_FontSetting ced_fontSetting = new Ced_FontSetting();
        ced_fontSetting.setFontforTextviews(get_started, "Celias-Regular.ttf", Objects.requireNonNull(getActivity()));
        ced_fontSetting.setFontforTextviews(demotext1, "Celias-Regular.ttf", getActivity());
        ced_fontSetting.setFontforTextviews(demotext2, "Celias-Regular.ttf", getActivity());
        ced_fontSetting.setFontforTextviews(demotext3, "Celias-Regular.ttf", getActivity());

        final Animation animationFadeIn = AnimationUtils.loadAnimation(getActivity(), R.anim.magenative_fade_in);
        image.startAnimation(animationFadeIn);

        return v;
    }
}