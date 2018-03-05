package com.example.administrator.securityguards.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Administrator on 2018/2/2/002.
 */

public class MyMarqueeTextView extends TextView {

    //使用java代码来生成控件
    public MyMarqueeTextView(Context context) {
        super(context);
    }


    //由系统调用，用在xml
    public MyMarqueeTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    //
    public MyMarqueeTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //获取焦点,当返回true时表示焦点一直在，由系统调用
    @Override
    public boolean isFocused() {
        return true;
    }
}
