package com.example.gzganotationdemo;

import android.view.View;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class InjectUtils {
    public static void inject(Object context){
        injectClick(context);
    }

    private static void injectClick(Object context) {
        Class<?> clazz = context.getClass();
        //获取到activity上的所有方法
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods ){
            //遍历activity上的所有方法，找到有带注解的方法
            //(一个方法上可能有多个注解),所以用数组
            Annotation[] annotations = method.getAnnotations();
            //遍历某个方法上的注解
            for (Annotation annotation : annotations) {
                //得到注解的具体类型
                Class<?> annotationClass = annotation.annotationType();
                EventBase eventBase = annotationClass.getAnnotation(EventBase.class);
                //判断注解是否是 EventBase类型的
                if (eventBase == null) {
                    //不是EventBase类型，就直接下一个
                    continue;
                }

                //是EventBase类型的话，继续走下面代码

                //最后要合成下面的代码
//              btn.setOnClickListener（new View.OnClickListener() {
////                    @Override
////                    public void onClick(View v) {
////
////                    }
////                });

                //1.setOnClickListener 订阅关系
//                String listenerSetter();
                String listenerSetter = eventBase.listenerSetter();

                //2.new View.OnClickListener()  事件本身
//                Class<?> listenerType();
                Class<?> listenerType=eventBase.listenerType();

                //3.事件处理程序
//                String callbackMethod();
                String callBackMethod=eventBase.callbackMethod();

                //得到上面三要素之后，就可以执行代码了
                Method valueMethod = null;
                try{
                    //annotationClass 为 EventBase.class
                    //因为 @EventBase注解在@OnClick 和 @OnLongClick 两个注解上
                    //所以这里的 “value”是@OnClick 和 @OnLongClick 两个注解里的参数
                    //int[] value()  --> 存的是控件的id
                    valueMethod = annotationClass.getDeclaredMethod("value");
                    //通过反射调用 value()拿到控件的id数组，， @OnClick.value()  或者是 @OnLongClick.value()
                    int[] viewId = (int[]) valueMethod.invoke(annotation);
                    for (int id : viewId){
                        //为了得到Button对象,使用findViewById
                        //clazz 就是传进来的activity,  拿到activity的findViewById方法
                        Method findViewById=clazz.getMethod("findViewById",int.class);
                        //通过反射调用  activity.findViewById(id) 来实例化控件view
                        View view=(View)findViewById.invoke(context,id);
                        if(view==null){
                            continue;
                        }
                        //context=activity        activity里面的 click() 或者是  longClick()=method
                        ListenerInvocationHandler listenerInvocationHandler=new ListenerInvocationHandler(context,method);

                        //listenerInvocationHandler.invoke() 相当于 调用 activity.click()  或者是  activity.longClick()

                        //new View.OnClickListener()
                        Object proxy= Proxy.newProxyInstance(listenerType.getClassLoader(),new Class[]{listenerType},listenerInvocationHandler);


                        //   拿到 view 的  setOnClickListener（new View.OnClickListener()）这个方法
                        Method onClickMethod=view.getClass().getMethod(listenerSetter,listenerType);

                        //反射调用
                        //onClickMethod就是： view 的  setOnClickListener（new View.OnClickListener()）这个方法

                        // 即onClickMethod.invoke(view,proxy) 就 实现了下面这个段代码
//                      view.setOnClickListener（new View.OnClickListener() {
////                        @Override
////                        public void onClick(View v) {
////
////                        }
////                     });
                        onClickMethod.invoke(view,proxy);
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
}
