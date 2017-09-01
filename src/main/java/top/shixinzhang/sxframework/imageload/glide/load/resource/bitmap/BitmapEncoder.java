package top.shixinzhang.sxframework.imageload.glide.load.resource.bitmap;

import android.graphics.Bitmap;
import android.util.Log;

import top.shixinzhang.sxframework.imageload.glide.load.ResourceEncoder;
import top.shixinzhang.sxframework.imageload.glide.load.engine.Resource;
import top.shixinzhang.sxframework.imageload.glide.util.LogTime;
import top.shixinzhang.sxframework.imageload.glide.util.Util;

import java.io.OutputStream;

/**
 * An {@link top.shixinzhang.sxframework.imageload.glide.load.ResourceEncoder} that writes {@link Bitmap}s to
 * {@link OutputStream}s.
 *
 * <p>
 *     {@link Bitmap}s that return true from {@link Bitmap#hasAlpha()}} are written
 *     using {@link Bitmap.CompressFormat#PNG} to preserve alpha and all other bitmaps are written
 *     using {@link Bitmap.CompressFormat#JPEG}.
 * </p>
 *
 * @see Bitmap#compress(Bitmap.CompressFormat, int, OutputStream)
 */
public class BitmapEncoder implements ResourceEncoder<Bitmap> {
    private static final String TAG = "BitmapEncoder";
    private static final int DEFAULT_COMPRESSION_QUALITY = 90;
    private Bitmap.CompressFormat compressFormat;
    private int quality;

    public BitmapEncoder() {
        this(null, DEFAULT_COMPRESSION_QUALITY);
    }

    public BitmapEncoder(Bitmap.CompressFormat compressFormat, int quality) {
        this.compressFormat = compressFormat;
        this.quality = quality;
    }

    @Override
    public boolean encode(Resource<Bitmap> resource, OutputStream os) {
        final Bitmap bitmap = resource.get();

        long start = LogTime.getLogTime();
        Bitmap.CompressFormat format = getFormat(bitmap);
        bitmap.compress(format, quality, os);
        if (Log.isLoggable(TAG, Log.VERBOSE)) {
            Log.v(TAG, "Compressed with type: " + format + " of size " + Util.getBitmapByteSize(bitmap) + " in "
                    + LogTime.getElapsedMillis(start));
        }
        return true;
    }

    @Override
    public String getId() {
        return "BitmapEncoder.top.shixinzhang.sxframework.imageload.glide.load.resource.bitmap";
    }

    private Bitmap.CompressFormat getFormat(Bitmap bitmap) {
        if (compressFormat != null) {
            return compressFormat;
        } else if (bitmap.hasAlpha()) {
            return Bitmap.CompressFormat.PNG;
        } else {
            return Bitmap.CompressFormat.JPEG;
        }
    }

}
