package com.guga;

import io.javalin.Context;
import io.javalin.Javalin;
import io.javalin.security.Role;

import static com.guga.AppWithRoles.MyRole.ANYONE;
import static com.guga.AppWithRoles.MyRole.ROLE_ONE;
import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.security.SecurityUtil.roles;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;


public class AppWithRoles {

    public static void main(String[] args) {
        new AppWithRoles().init();
    }

    private void init() {
        Javalin app = Javalin.create();
        app.accessManager((handler, ctx, permittedRoles) -> {
            MyRole userRole = (MyRole) getUserRole(ctx);
            if (permittedRoles.contains(userRole)) {
                handler.handle(ctx);
            } else {
                ctx.status(401).result("Unauthorized");
            }
        });
        app.routes(() -> {
            get("/un-secured",   ctx -> ctx.result("Hello"),   roles(ANYONE));
            get("/secured",      ctx -> ctx.result("Hello"),   roles(ROLE_ONE));
        });
        app.start();
    }

    Role getUserRole(Context ctx) {
        // determine user role based on request
        // typically done by inspecting headers
        final String myRole = ctx.req.getParameter("role");
        if (nonNull(myRole)) {
            return ROLE_ONE;
        }
        return ANYONE;
    }

    enum MyRole implements Role {
        ANYONE, ROLE_ONE, ROLE_TWO, ROLE_THREE;
    }
}
