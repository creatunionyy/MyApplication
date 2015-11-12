package com.library.core.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public abstract class MaskedImage extends ImageView {

    private static final Xfermode DST_IN;
    private Bitmap mask;
    private Paint paint;

    static {
        DST_IN = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
    }

    public MaskedImage(Context paramContext) {
        super(paramContext);
    }

    public MaskedImage(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    public MaskedImage(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
    }

    public abstract Bitmap createMask();

    protected void onDraw(Canvas canvas) {
        Drawable localDrawable = getDrawable();
        if (localDrawable == null)
            return;
        try {
            if (paint == null) {
                paint = new Paint();
                paint.setFilterBitmap(false);
                paint.setXfermode(DST_IN);
            }
            int saveFlags = Canvas.MATRIX_SAVE_FLAG | Canvas.CLIP_SAVE_FLAG |
                    Canvas.HAS_ALPHA_LAYER_SAVE_FLAG | Canvas.FULL_COLOR_LAYER_SAVE_FLAG |
                    Canvas.CLIP_TO_LAYER_SAVE_FLAG;
            int layer = canvas.saveLayer(0.0F, 0.0F, getWidth(), getHeight(), null, saveFlags);
            super.onDraw(canvas);
            if ((mask == null) || (mask.isRecycled())) {
                mask = createMask();
            }
            canvas.drawBitmap(mask, 0.0F, 0.0F, paint);
            canvas.restoreToCount(layer);
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
