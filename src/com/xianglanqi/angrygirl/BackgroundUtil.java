package com.xianglanqi.angrygirl;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

public class BackgroundUtil {
    
    public static final int changeBackground(Context context, ImageView[] images) {
        String picPath = context.getSharedPreferences("user_setting", Context.MODE_PRIVATE).getString(
                "user_background_pic", null);
        return changeBackground(picPath, false, context, images);
    }

    public static final int changeBackground(String path, boolean save, Context context, ImageView[] images) {

        Log.d("hy", "changeBackgroud:" + path + " images:" + images.length);

        if (TextUtils.isEmpty(path)) {
            return -1;
        }
        if (null != images[0].getTag() && path.endsWith(images[0].getTag().toString())) {
            return 1;
        }

        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, opts);

        int width = convertDIP2PX(context, images[0].getLayoutParams().width);
        if(width <= 0) {
            width = context.getResources().getDisplayMetrics().widthPixels;
        }
        int height = convertDIP2PX(context, images[0].getLayoutParams().height);
        if (height <= 0) {
            height = context.getResources().getDisplayMetrics().heightPixels;
        }
        Log.d("hy", String.format("w:%d, h:%d", width, height));

        opts.inSampleSize = computeSampleSize(opts, -1, 1024 * 1024);
        opts.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(path, opts);
        // Bitmap bitmap = BitmapFactory.decodeFile(path);
        if (null == bitmap) {
            return -1;
        }
        if (save) {
            Editor editor = context.getSharedPreferences("user_setting", Context.MODE_PRIVATE).edit();
            editor.putString("user_background_pic", path);
            editor.commit();
        }
        for (ImageView imageView : images) {
            imageView.setTag(path);
            imageView.setImageBitmap(bitmap);
        }

        if (!bitmap.isRecycled()) {
            // bitmap.recycle();
        }
        
        return 0;
    }

    public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);

        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }

        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;

        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength),
                Math.floor(h / minSideLength));

        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }

        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

    public static int convertDIP2PX(Context context, int dip) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f * (dip >= 0 ? 1 : -1));
    }

    public static int convertPX2DIP(Context context, int px) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f * (px >= 0 ? 1 : -1));
    }

}
