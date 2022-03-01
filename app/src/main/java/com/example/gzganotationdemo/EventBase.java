package com.example.gzganotationdemo;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注解多态，元注解，作用：  注解在 其他注解上
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EventBase {
    // view.setOnClickListener(new View.OnClickListener() {
    //            @Override
    //            public void onClick(View view) {
    //
    //            }
    //        });

    //1.setOnClickListener  订阅关系
    String listenerSetter();

    //2.new View.OnClickListener()  事件本身
    Class<?> listenerType();

    //3.事件处理程序  onClick方法名
    String callbackMethod();

}
