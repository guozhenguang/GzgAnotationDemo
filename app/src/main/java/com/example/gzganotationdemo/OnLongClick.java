package com.example.gzganotationdemo;

import android.view.View;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@EventBase(listenerSetter = "setOnLongClickListener"
        ,listenerType = View.OnLongClickListener.class
        ,callbackMethod = "onLongClick")
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OnLongClick {
    int[] value() default -1;
}
