package com.guga;

import io.javalin.Context;
import io.javalin.Javalin;
import io.javalin.core.util.FileUtil;
import io.reactivex.Flowable;

public class AppCRUD {

    public static void main(String[] args) {
        final Javalin app = Javalin.create();
        app
                .get("/users", UserController::getAll)
                .enableStaticFiles("/public")
                .post("/users/", UserController::create)
                .get("/users/:user-id", UserController::getOne)
                .patch("/users/:user-id", UserController::update)
                .delete("/users/:user-id", UserController::delete)
                .post("/upload", ctx -> {
                            try {
                                ctx.uploadedFiles("upload").forEach(
                                        file -> {
                                    FileUtil.streamToFile(file.getContent(), "upload/" + file.getName());
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                )
                .start();
    }

    private static class UserController {
        public static Flowable<String> getAll(Context context) {
            return Flowable.just("Any user");
        }

        public static void create(Context context) {

        }

        public static String getOne(Context context) {
            return "I'm get one";
        }

        public static void update(Context context) {

        }

        public static void delete(Context context) {

        }
    }
}
