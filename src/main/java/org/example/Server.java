package org.example;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

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

                    if (fileSize <= 1024) {
                        byte[] buffer = new byte[(int) fileSize];
                        dataInputStream.readFully(buffer);

                        //save
                        try (FileOutputStream fos = new FileOutputStream(fileName)) {
                            fos.write(buffer);
                        }

                        System.out.println("Файл сохранен: " + fileName);
                        //отправка обратно
                        dataOutputStream.writeUTF("Файл ополучен и отправлен");
                        dataOutputStream.writeLong(fileSize);
                        dataOutputStream.write(buffer);
                    } else {
                        System.out.println("файл слишком большой");
                        dataOutputStream.writeUTF("файл больше 1кб ");
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (IOException e) {
            System.out.println("Сервер " + e.getMessage());
        }
    }
}
