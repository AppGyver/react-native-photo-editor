package com.ahmedadeltito.photoeditor.widget;

import android.graphics.Canvas;

public interface CanvasSaveProxy {
    int save();

    boolean isFor(final Canvas canvas);
}
