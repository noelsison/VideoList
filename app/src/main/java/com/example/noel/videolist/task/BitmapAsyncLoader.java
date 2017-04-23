package com.example.noel.videolist.task;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.noel.videolist.R;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Noel on 4/23/2017.
 */

// TODO: Allow loading of multiple bitmaps?
public class BitmapAsyncLoader extends AsyncTask<String, Integer, Bitmap> {
    private static final String TAG = BitmapAsyncLoader.class.getName();

    private AssetManager assetManager;
    private BitmapAsyncLoaderListener bitmapAsyncLoaderListener;
    private boolean resizeRequired = false;
    private int outputWidth;
    private int outputHeight;

    public BitmapAsyncLoader(BitmapAsyncLoaderListener bitmapAsyncLoaderListener, AssetManager assetManager) {
        this.bitmapAsyncLoaderListener = bitmapAsyncLoaderListener;
        this.assetManager = assetManager;
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        Log.d(TAG, "SAMPLE SIZE: " + inSampleSize);

        return inSampleSize;
    }

    public void setOutputSize(float outputWidth, float outputHeight, float density) {
        resizeRequired = true;

        // TODO: Not sure what is actually happening right here
        // Google recommends using glide or picasso (which can be used to generate video stills)
        this.outputWidth = (int) (outputWidth / (density + 0.5f));
        this.outputHeight = (int) (outputHeight / (density + 0.5f));
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        String filename = params[0];
        Bitmap bitmap = null;
        try {
            InputStream inputStream = assetManager.open(filename);

            final BitmapFactory.Options options = new BitmapFactory.Options();
            if (resizeRequired) {
                // First decode with inJustDecodeBounds=true to check dimensions
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(inputStream, null, options);

                // Calculate inSampleSize
                options.inSampleSize = calculateInSampleSize(options, outputWidth, outputHeight);

                // Decode bitmap with inSampleSize set
                options.inJustDecodeBounds = false;

                // Reopen stream to read from the start
                inputStream.close();
                inputStream = assetManager.open(filename);
            }

            bitmap = BitmapFactory.decodeStream(inputStream, null, options);
            inputStream.close();

        } catch (IOException e) {
            Log.e(TAG, "Failed to open image: " + filename);
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        bitmapAsyncLoaderListener.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        bitmapAsyncLoaderListener.onPostExecute(bitmap);
    }

    // TODO: Rename these methods in case of name collision
    public interface BitmapAsyncLoaderListener {
        void onPostExecute(Bitmap bitmap);

        void onProgressUpdate(Integer... values);
    }
}