package com.example.gzganotationdemo;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 这个类用来代理 new View.OnClickListener()对象
 * 并执行这个对象身上的onClick方法
 */
public class ListenerInvocationHandler implements InvocationHandler {

    private Object activity;
    private Method activityMethod;

    public ListenerInvocationHandler(Object activity, Method activityMethod) {
        this.activity = activity;
        this.activityMethod = activityMethod;
    }

    /**
     *表示onClick的执行
     * 程序执行onClick方法，就会转到这里来
     * 因为框架中不直接执行onClick
     * 所以在框架中必然有个地方让invoke和onClick关联上
     */
    @Override
    public Object invoke(Object o, Method method, Object[] args) throws Throwable {
        //在这里去调用被注解了的click();
        // @OnClick({R.id.btn1,R.id.btn2})
        //  public void click(View view){}
        //利用反射 调用 activity.click()  或者是  activity.longClick()
        return activityMethod.invoke(activity,args);
    }
}
