package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class FileServer {
    private static final int PORT = 8080;
    private static final int MAX_FILE_SIZE = 1024; // 1 KB

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Сервер запущений і чекає на підключення...");

            while (true) {
                try (Socket clientSocket = serverSocket.accept();

                     InputStream inputStream = clientSocket.getInputStream()) {

                    System.out.println("Клієнт підключився.");

                    // Приймаємо файл і зберігаємо його
                    try (FileOutputStream fileOutputStream = new FileOutputStream("received_file.txt")) {
                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            fileOutputStream.write(buffer, 0, bytesRead);
                        }
                    }
                    System.out.println("Файл отримано та збережено як received_file.txt.");
                } catch (IOException e) {
                    System.out.println("Помилка при отриманні файлу від клієнта: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}