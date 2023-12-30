package com.tarmiz.imentor.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.facebook.common.executors.UiThreadImmediateExecutorService;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.tarmiz.imentor.R;

import java.util.ArrayList;

public class SliderAdapter extends PagerAdapter
{
    private ArrayList<String> images;
    private LayoutInflater inflater;
    private Context context;
    private ImageRequest imageRequest;
    private ImagePipeline imagePipeline;
    private DataSource<CloseableReference<CloseableImage>> dataSource;
    private ImageView myImage;

    public SliderAdapter(Context context, ArrayList<String> images) {
        this.context = context;
        this.images = images;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup view, int position)
    {
        View myImageLayout = inflater.inflate(R.layout.slide, view, false);

        myImage = myImageLayout.findViewById(R.id.image);

        imageRequest = ImageRequest.fromUri(images.get(position));
        imagePipeline = Fresco.getImagePipeline();
        dataSource = imagePipeline.fetchDecodedImage(imageRequest, null);
        dataSource.subscribe(
                new BaseBitmapDataSubscriber() {
                    @Override
                    protected void onNewResultImpl(Bitmap bitmap) {
                        Drawable drawable = new BitmapDrawable(context.getResources(),bitmap);
                        myImage.setImageDrawable(drawable);
                    }

                    @Override
                    protected void onFailureImpl(DataSource<CloseableReference<CloseableImage>> dataSource) {
                        // In general, failing to fetch the image should not keep us from displaying the
                        // notification. We proceed without the bitmap.
                    }
                },
                UiThreadImmediateExecutorService.getInstance());

        view.addView(myImageLayout, 0);
        return myImageLayout;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }
}
