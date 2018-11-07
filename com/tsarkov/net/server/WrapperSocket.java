package com.tsarkov.net.server;


import java.io.*;
import java.net.Socket;
import java.util.Iterator;

public class WrapperSocket extends Thread {
    Socket socket;
    BufferedReader in;
    BufferedWriter out;

    WrapperSocket(Socket socket) {
        try {
            // Получение сокета и открытие потоков для работы с ним
            this.socket = socket;
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            // Запуск отдельной нити для работы с сокетом
            start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void run(){
        String msg;
        try {
            try {
                // Отправка новому подключению 10 последних сообщений чата
                getLastMesg();
                // Запуск цикла
                while (true) {
                    // Получение строки с  InputStream потока сокета
                    msg = in.readLine();
                    // Проверка условий выхода из цикла
                    if (msg == null || msg.equals("exit") ) break;
                    // Добавление строки в буфер сообщений
                    Server.cacheMsg.add(msg);
                    // Отправка сообщения всем подключенным сокетам
                    for (WrapperSocket wr : Server.listSockets) {
                        if (wr.equals(this) || wr.socket == null) continue;
                        wr.sendMsg(msg);
                    }

                }
            } catch (IOException | NullPointerException e ) {
                e.printStackTrace();
            } finally {
                // Удаление текущего экземпляра подключения из списка рассылки сообщений
                Iterator<WrapperSocket> it = Server.listSockets.iterator();
                while (it.hasNext()){
                    if (it.next().equals(this)) it.remove();
                }
                // Закрытие подключений
                in.close();
                out.close();
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    void sendMsg(String msg) throws IOException{
        out.write( msg + "\n");
        out.flush();
    }
    void getLastMesg() throws IOException {
        int i = Server.cacheMsg.size() <= 10 ? 0 : Server.cacheMsg.size() - 10;
        while (i < Server.cacheMsg.size()) {
            sendMsg(Server.cacheMsg.get(i));
            i++;
        }
    }
}