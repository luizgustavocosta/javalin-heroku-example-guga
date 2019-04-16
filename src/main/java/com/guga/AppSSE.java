package com.guga;

import io.javalin.Javalin;

import java.time.LocalDateTime;

import static io.javalin.apibuilder.ApiBuilder.*;
import static io.javalin.apibuilder.ApiBuilder.delete;

public class AppSSE {

    public static void main(String[] args) {
        final Javalin app = Javalin.create();
        app.routes(() -> {
            path("mypath", () -> {
                get("", ctx -> ctx.json("Hello world"));
                post("", ctx -> ctx.result("Created"));
                path(":id", () -> {
                    get("", ctx -> {
                    });
                    patch("", ctx -> {
                    });
                    delete("", ctx -> {
                    });
                });
            });
        })
        .enableDebugLogging()
        .enableStaticFiles("/public")
        .enableRouteOverview("");
        app.sse("/sse", client -> {
            int index = 0;
            while (index<10) {
                client.sendEvent("Something on screen "+LocalDateTime.now());
                index++;
            }
            client.onClose(() -> client.sendEvent("Bye bye"));
        });
        app.start();
    }
}
