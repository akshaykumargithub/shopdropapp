package shop.dropapp.ui.productsection.adapter;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Ced_ProductViewImagSlider_forswatches extends FragmentStatePagerAdapter
{
    Context c;
    CharSequence[] stringArray;
    ArrayList<String> images;
    ArrayList<LinkedHashMap<String,String>> img_params;
    public Ced_ProductViewImagSlider_forswatches(FragmentManager fm, Context context, ArrayList<String> img, ArrayList<LinkedHashMap<String,String>> img_params)
    {
        super(fm);
        c=context;
        images=img;
        this.img_params=img_params;
        stringArray = img.toArray(new String[img.size()]);
    }
    /*public Ced_ProductViewImagSlider(FragmentManager fm, Context context, @NonNull ArrayList<String> img)
    {
        super(fm);
        c=context;
        stringArray = img.toArray(new String[img.size()]);
    }*/
    @NonNull
    @Override
    public Fragment getItem(int position)
    {
        final Ced_ProductViewImageFragment_forswatches f1=new Ced_ProductViewImageFragment_forswatches();
        final Bundle bundle=new Bundle();
        bundle.putString("url", String.valueOf(stringArray[position]));
        bundle.putCharSequenceArray("stack", stringArray);
        bundle.putInt("position",position);
        f1.setArguments(bundle);
        return f1;
    }
    @Override
    public int getCount()
    {
        return stringArray.length;
    }

}


