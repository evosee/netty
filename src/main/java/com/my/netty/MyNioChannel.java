package com.my.netty;

import java.io.IOException;
import java.nio.channels.Selector;

/**
 * @author chensai
 * @version 1.0
 * @date 2019/5/29 15:58
 */
public interface MyNioChannel {
   void  handler(Selector selector,MyThreadGroup myThreadGroup) throws IOException, Exception;

}
