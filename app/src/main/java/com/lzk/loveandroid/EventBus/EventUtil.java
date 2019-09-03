package com.lzk.loveandroid.EventBus;

import org.greenrobot.eventbus.EventBus;

/**
 * @author LiaoZhongKai
 * @date 2019/9/3.
 */
public class EventUtil {

    public static void register(Object subscriber) {
        EventBus.getDefault().register(subscriber);
    }

    public static void unregister(Object subscriber) {
        EventBus.getDefault().unregister(subscriber);
    }

    public static void sendEvent(Event event) {
        EventBus.getDefault().post(event);
    }

    public static void sendStickyEvent(Event event) {
        EventBus.getDefault().postSticky(event);
    }

}
