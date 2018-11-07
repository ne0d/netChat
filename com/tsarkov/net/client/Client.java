package com.tsarkov.net.client;

import java.io.*;
import java.net.Socket;


public class Client {
    static Socket socket;
    static InMsgClient in;
    static PrintWriter out;
    static BufferedReader key;
    static String name;

    public static  void main(String[] args) {
        try {
            try {
                // Открытие сокета и попытка связи с сервером
                socket = new Socket("localhost", 5656);
                // Создание экземпляра класса для приема входящих сообщений сокета
                in = new InMsgClient(socket);
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),true);
                key = new BufferedReader(new InputStreamReader(System.in));
                // Таймаут для загрузки буфера последних сообщений  чата
                Thread.sleep(500);
                System.out.println("Введите ваше имя: ");
                name = key.readLine();
                System.out.println("Введите ваше сообщение:  ");
                while(true) {
                    String msg = key.readLine();
                    // Проверка строки на условия выхода
                    if (msg.equals("exit") || (msg == null)) {
                        out.write( "Собеседник " + name + " вышел из чата." + "\n");
                        out.write( msg + "\n");
                        out.flush();
                        break;
                    }
                    // Отправка сообщения другим клиентам сервера
                    sendMsg(msg);
                }

            }finally {
                out.close();
                key.close();
                socket.close();
            }
        } catch (IOException | InterruptedException e ){
            System.out.println("Error client");
            e.printStackTrace();
        }
    }
    static void sendMsg(String msg) throws IOException{
        out.write( name + " : " + msg + "\n");
        out.flush();
    }
}