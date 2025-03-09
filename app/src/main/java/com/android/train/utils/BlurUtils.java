package com.android.train.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.view.View;
import android.widget.ImageView;

public class BlurUtils {

    public static Bitmap blurBitmap(Context context, Bitmap bitmap, float radius) {
        Bitmap outputBitmap = Bitmap.createBitmap(bitmap);
        RenderScript renderScript = RenderScript.create(context);

        final Allocation input = Allocation.createFromBitmap(renderScript, bitmap);
        final Allocation output = Allocation.createFromBitmap(renderScript, outputBitmap);

        ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(renderScript, input.getElement());
        script.setRadius(radius);
        script.setInput(input);
        script.forEach(output);

        output.copyTo(outputBitmap);
        renderScript.destroy();
        return outputBitmap;
    }

    public static Bitmap captureView(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    public static void applyBlurToView(Context context, View targetView, View backgroundView, ImageView blurImageView, float radius) {
        backgroundView.post(() -> {
            Bitmap originalBitmap = captureView(backgroundView);
            Bitmap blurredBitmap = blurBitmap(context, originalBitmap, radius);
            blurImageView.setImageBitmap(blurredBitmap);
            blurImageView.setVisibility(View.VISIBLE);
        });
    }
}


