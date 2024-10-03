package app.controllers;

import app.entities.Lists;
import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.ListMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.ArrayList;
import java.util.List;

public class MenuController {
    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.get("/task", ctx -> menus(ctx, connectionPool));
    }

    private static void menus(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        User user = ctx.sessionAttribute("currentUser");

        List<Lists> menu = new ArrayList<>();
        menu = ListMapper.getAllListsPerUser(user.getUserId(), connectionPool);


        ctx.attribute("menu", menu);
        ctx.render("task.html");

    }
}
