package shop.dropapp.Ced_MageNative_Location;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import shop.dropapp.R;

public class Ced_NewLoader extends Dialog
{
    Context context_new;
    LinearLayout layout;
    View view;
    private ImageView iv;
    public Ced_NewLoader(Context context)
    {
        super(context, R.style.TransparentProgressDialog);
        context_new=context;
        WindowManager.LayoutParams wlmp = getWindow().getAttributes();
        Window window = this.getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        window.addFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);
        wlmp.gravity = Gravity.CENTER_HORIZONTAL;
        getWindow().setAttributes(wlmp);
        setTitle(null);
        setCancelable(true);
        setOnCancelListener(null);
        View view=View.inflate(context_new,R.layout.loader,null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        addContentView(view, params);
    }
    @Override
    public void show()
    {
        super.show();
    }
    @Override
    public void dismiss()
    {
        super.dismiss();
    }
}
