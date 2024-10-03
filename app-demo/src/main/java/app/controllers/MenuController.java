package app.controllers;

import app.persistence.ConnectionPool;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.ArrayList;

public class MenuController {
    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.get("/task", ctx -> menus(ctx, connectionPool));
    }

    private static void menus(Context ctx, ConnectionPool connectionPool){
        ArrayList<String> menu = new ArrayList<>();
        menu.add("forside");
        menu.add("andet");
        menu.add("trejde");

        ctx.attribute("menu", menu);
        ctx.render("task.html");

    }
}
