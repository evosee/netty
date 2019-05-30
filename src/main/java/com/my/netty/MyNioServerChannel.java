package com.my.netty;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;

/**
 * @author chensai
 * @version 1.0
 * @date 2019/5/29 16:04
 */
public class MyNioServerChannel implements MyNioChannel {
    @Override
    public void handler(Selector selector,MyThreadGroup myThreadGroup) throws Exception {
        for(;;){
            int i = selector.select(1000);
            if(i==0) continue;
            System.out.println(1);
            Set<SelectionKey> sets = selector.selectedKeys();
            Iterator<SelectionKey> iterator = sets.iterator();
            while (iterator.hasNext()){
                SelectionKey selectionKey = iterator.next();
                if(selectionKey.isAcceptable()){
                    iterator.remove();
                    myThreadGroup.next().register(selectionKey);
                }
            }
        }

    }

}
