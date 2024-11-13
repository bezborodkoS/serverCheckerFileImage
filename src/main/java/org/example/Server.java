package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;

public class Server {
    private static final int PORT = 8081;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("сервер запущен");

            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     DataInputStream dataInputStream = new DataInputStream(clientSocket.getInputStream());
                     DataOutputStream dataOutputStream = new DataOutputStream(clientSocket.getOutputStream())) {

                    System.out.println("подключение клиента");

                    // Прийом файлу
                    String fileName = dataInputStream.readUTF();
                    int fileSize = dataInputStream.readInt();
                    String content = dataInputStream.readUTF();

                    if (fileSize <= 1024) {
                        byte[] buffer = new byte[(int) fileSize];
                        dataInputStream.readFully(buffer);

                        //save
                        try (FileOutputStream fos = new FileOutputStream(fileName)) {
                            fos.write(buffer);
                        }

                        System.out.println("Файл сохранен: " + fileName);
                        //отправка обратно
                        //it's sends metadata too, but it's not necessary because you have this file
                        dataOutputStream.writeUTF("1");
                        dataOutputStream.writeLong(fileSize);
                        dataOutputStream.write(buffer);

                        Files.writeString(new File(fileName).toPath(), content);
                    } else {
                        System.out.println("файл слишком большой");
                        dataOutputStream.writeUTF("2");
                    }
                } catch (IOException e) {
                    System.out.println("Клиент " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Сервер " + e.getMessage());
        }
    }
}
