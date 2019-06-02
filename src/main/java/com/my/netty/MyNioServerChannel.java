package com.my.netty;

import io.netty.channel.nio.NioEventLoop;
import sun.nio.ch.SelectorImpl;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author chensai
 * @version 1.0
 * @date 2019/5/29 16:04
 */
public class MyNioServerChannel implements MyNioChannel {
    @Override
    public void handler(Selector selector, MyThreadGroup myThreadGroup) throws Exception {
        /*Class c = Class.forName("sun.nio.ch.SelectorImpl");
        Field field = c.getDeclaredField("publicSelectedKeys");
        Field field2 = c.getDeclaredField("publicKeys");
        field.setAccessible(true);
        field2.setAccessible(true);
        HashSet<SelectionKey> hashSet = new HashSet<>();
        field.set(selector, hashSet);
        field2.set(selector, hashSet);*/
        for (; ; ) {
            //hashSet.clear();
            /*Set<SelectionKey> s = (Set<SelectionKey>) field.get(selector);
            Set<SelectionKey> s2 = (Set<SelectionKey>) field2.get(selector);*/


          //  eventExecutors.
            int i = selector.select(1000);
            if (i == 0) continue;
            System.out.println(1);
            Set<SelectionKey> sets = selector.selectedKeys();
            Iterator<SelectionKey> iterator = sets.iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                if (selectionKey.isAcceptable()) {
                    iterator.remove();
                    myThreadGroup.next().register(selectionKey);
                }

            }
        }

    }

}
