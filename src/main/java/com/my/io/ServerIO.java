package com.my.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

/**
 * @author chensai
 * @version 1.0
 * @date 2020/6/8 10:13
 *
 *
 */
public class ServerIO {
    ExecutorService executor = Executors.newFixedThreadPool(4);
    public void server() throws Exception {
        try (ServerSocket serverSocket = new ServerSocket(9898)) {

            while (true){
                Socket socket  = serverSocket.accept();
                executor.execute(()->{
                    try {
                        InputStream inputStream = socket.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                        //线程会阻塞在这里 如果大量任务过来阻塞在这里线程会卡死
                        reader.lines().collect(Collectors.toList()).forEach(e -> {
                            System.out.println(e);
                        });
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        }
    }

    public static void main(String[] args) throws Exception {
        new ServerIO().server();
    }
}
