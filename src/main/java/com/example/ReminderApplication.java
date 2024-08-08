package com.example;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Timer;
import java.util.TimerTask;

public class ReminderApplication extends Application {
    private static final boolean[] isNotificationSent = new boolean[24];

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                LocalDateTime date = LocalDateTime.now(ZoneId.systemDefault());
                schedule(date);
            }
        }, 0, 300 * 1000); // every 5 minutes

        // Reset notification array at midnight
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                LocalDateTime date = LocalDateTime.now(ZoneId.systemDefault());
                if (date.getHour() == 0) {
                    resetNotifications();
                }
            }
        }, 0, 60 * 1000); // every 1 minute

        primaryStage.setTitle("Activity Reminder");
        primaryStage.show();
    }

    private void resetNotifications() {
        for (int i = 0; i < isNotificationSent.length; i++) {
            isNotificationSent[i] = false;
        }
    }

    private void schedule(LocalDateTime date) {
        int hour = date.getHour();

        if (hour == 13 && !isNotificationSent[hour]) {
            isNotificationSent[hour] = true;
            sendReminder("It's time for lunch and water!!");
        } else if (hour == 16 && !isNotificationSent[hour]) {
            isNotificationSent[hour] = true;
            sendReminder("It's time for snacks and water!!");
        } else if (hour == 18 && !isNotificationSent[hour]) {
            isNotificationSent[hour] = true;
            sendReminder("It's time to log off from work!!");
        } else if (hour >= 9 && hour < 18 && hour != 13 && hour != 16 && !isNotificationSent[hour]) {
            isNotificationSent[hour] = true;
            sendReminder("It's time to drink water!!");
        }
    }

    private void sendReminder(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Urgent Reminder");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
}
