package jackszm.androiddevtweets;

import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.net.URI;

public class ImageLoader {

    public void load(ImageView imageView, URI imageUri) {
        Picasso.with(imageView.getContext())
                .load(imageUri.toString())
                .into(imageView);
    }
}
