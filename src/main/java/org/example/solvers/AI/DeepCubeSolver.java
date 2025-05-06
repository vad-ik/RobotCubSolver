package org.example.solvers.AI;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class DeepCubeSolver {
    public static String send(String state) {

        try {
            System.out.println("данные отправляются");
            // URL сервера
            URL url = URI.create("https://deepcube.igb.uci.edu/solve").toURL();

// Настраиваем прокси (например, HTTP-прокси на localhost:8080)
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("proxy.example.com", 8080));

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();//сюда прокси

            // Настройка запроса
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);

            // Данные для отправки

             String formData = "state=" + URLEncoder.encode(state, StandardCharsets.UTF_8);

            // Отправка данных
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = formData.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            System.out.println("данные отправлены");
            // Получение ответа
            int responseCode = connection.getResponseCode();

            System.out.println("данные получены");
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    return response.toString();
                }
            } else {
                System.out.println("Ошибка: " + responseCode);
                // Чтение тела ошибки
                try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8))) {
                    StringBuilder errorResponse = new StringBuilder();
                    String errorLine;
                    while ((errorLine = br.readLine()) != null) {
                        errorResponse.append(errorLine.trim());
                    }
                    System.out.println("Тело ошибки: " + errorResponse);
                }

            }
        } catch (Exception e) {
            throw new RuntimeException("ошибка при отправке данных на сервер"+e);
        }
        return null;
    }
}