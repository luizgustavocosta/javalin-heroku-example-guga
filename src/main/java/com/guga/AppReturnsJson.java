package com.guga;


import io.javalin.Javalin;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class AppReturnsJson {

    public static void main(String[] args) {
        Javalin app = Javalin.create();
        app.port(getHerokuAssignedPort());
        app.start();
        app.get("/", ctx -> ctx.json(
                new User("Luiz",
                        "Costa",
                        LocalDateTime.now(ZoneOffset.UTC))));
    }

    private static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 7000;
    }

    private static class User {
        private final String firstName;
        private final String lastName;
        private final LocalDateTime dob;

        private User(String firstName, String lastName, LocalDateTime dob) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.dob = dob;
        }

    }

}
