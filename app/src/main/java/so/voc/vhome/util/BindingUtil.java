package so.voc.vhome.util;

import android.databinding.BindingAdapter;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Fun：
 *
 * @Author：Linus_Xie
 * @Date：2019/6/25 16:10
 */
public class BindingUtil {

    @BindingAdapter({"imageUrl"})
    public static void loadImage(ImageView imageView, String imageUrl){
        if (imageUrl != null){
            Picasso.with(imageView.getContext()).load(imageUrl).into(imageView);
        }
    }

    @BindingAdapter({"showLoading", "message"})
    public static void showLoading(View view, boolean showLoading, String message) {
        if (showLoading) {
            // showLoading
        } else {
            // cancelLoading
        }
    }

    @BindingAdapter("showLoading")
    public static void showLoading(View view, boolean showLoading) {
        if (showLoading) {
            // showLoading
        } else {
            // cancelLoading
        }
    }
}
