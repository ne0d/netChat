package com.tsarkov.net.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;


public class InMsgClient extends Thread {
    Socket socket;
    BufferedReader in;

    InMsgClient(Socket socket){
        try {
            // Получение сокета и открытие входящего потока для работы с ним
            this.socket = socket;
            this.in =  new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // Запуск новой нити для работы с входящим потоком
            start();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void run()  {
        try {
            try {
                while (true) {
                    // Получение строки от сокета
                    String answerServ = in.readLine();
                    // Проверка условий выхода из цикла
                    if (answerServ == null) break;
                    // Отправка строки на консоль подключенного клиента
                    System.out.println(answerServ);
                }
            } catch (IOException | NullPointerException ignore) {
            } finally {
                // Закрываем входящий поток сокета, все остальные ресурсы сокета закрываются к классе Client
                in.close();
            }
        } catch (IOException ignore){}
    }
}
