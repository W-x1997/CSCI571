package com.wx.movie.tools;


import android.text.TextPaint;
import android.text.style.UnderlineSpan;


public class NoUnderlineSpan extends UnderlineSpan {

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setColor(ds.linkColor);
        ds.setUnderlineText(false);
    }
}
