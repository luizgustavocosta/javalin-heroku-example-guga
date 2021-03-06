package com.guga;

import io.javalin.Context;
import io.javalin.Javalin;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static io.javalin.apibuilder.ApiBuilder.*;

public class AppConfigureServer {

    public static void main(String[] args) {
        Javalin.create()
            .contextPath("/guga")
            .sse("/sse", sseClient -> {
                sseClient.sendEvent("Hello World for you at "+LocalDateTime.now());
                sseClient.onClose(() -> System.out.println("See you later"));
            })
            .enableAutogeneratedEtags()
            .routes(() -> {
                path("users", () -> {
                    get(UserController::getAllUsers);
                    post(UserController::createUser);
                    path(":id", () -> {
                        get(UserController::getUser);
                        patch(UserController::updateUser);
                        delete(UserController::deleteUser);
                    });
                });
            })
            .enableCorsForOrigin("*")
            .enableDebugLogging()
            .enableStaticFiles("/public")
            .start();
    }

    private static final class UserController {
        public static List<String> getAllUsers(Context context) {
          return Collections.emptyList();
        }
        public static void createUser(Context context) {}
        public static void getUser(Context context) {}
        public static void updateUser(Context context) {}
        public static void deleteUser(Context context) {}

    }
}
