package com.tsarkov.net.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    static ArrayList<WrapperSocket> listSockets = new ArrayList<>();
    static  ArrayList<String> cacheMsg = new ArrayList<>();
    ServerSocket server;
    Socket socket;

    Server() {
        try {
            try {
                // Запуск сервера чата
                server = new ServerSocket(5656);
                while (true) {
                    // Ожидание нового подключения
                    socket = server.accept();
                    // Добавления подключения в список "рассылки сообщений"
                    listSockets.add(new WrapperSocket(socket));
                    continue;
                }
            } catch (IOException e) {
                e.printStackTrace();

            } finally {
                server.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public static void main(String[] args){
        new Server();
    }

}

