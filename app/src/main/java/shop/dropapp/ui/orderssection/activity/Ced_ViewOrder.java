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
package shop.dropapp.ui.orderssection.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import shop.dropapp.base.activity.Ced_NavigationActivity;
import shop.dropapp.R;
import com.google.android.gms.analytics.Tracker;
import shop.dropapp.base.activity.Ced_MainActivity;
import shop.dropapp.databinding.MagenativeActivityViewOrderBinding;
import shop.dropapp.ui.newhomesection.activity.Magenative_HomePageNewTheme;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class Ced_ViewOrder extends Ced_NavigationActivity {
    MagenativeActivityViewOrderBinding viewOrderBinding;
    private Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewOrderBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.magenative_activity_view_order, content, true);

       set_regular_font_fortext(viewOrderBinding.MageNativeOrderId);
       set_regular_font_forButton(viewOrderBinding.MageNativeContinuewithdefault);

        viewOrderBinding.MageNativeFinalorderid.setText("#" + getIntent().getStringExtra("order_id"));

        viewOrderBinding.MageNativeContinuewithdefault.setOnClickListener(v -> {
            Intent intent1 = new Intent(getApplicationContext(), Magenative_HomePageNewTheme.class);
            startActivity(intent1);
            finishAffinity();
            overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
        });

        Ced_MainActivity.latestcartcount = "0";
    }

    @Override
    public void onBackPressed() {
        Intent intent1 = new Intent(getApplicationContext(), Magenative_HomePageNewTheme.class);
        startActivity(intent1);
        finishAffinity();
        overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
    }

    @Override
    protected void onResume() {
        invalidateOptionsMenu();
        super.onResume();

    }

}
