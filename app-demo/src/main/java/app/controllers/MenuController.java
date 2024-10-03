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
        app.get("/list", ctx -> menus(ctx, connectionPool));
    }

    //private static void menus(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
//        User user = ctx.sessionAttribute("currentUser");
//
//        List<Lists> menu = ListMapper.getAllListsPerUser(user.getUserId(), connectionPool);
//        ArrayList<String> nameMenu = new ArrayList<>();
//        for (Lists name : menu){
//            nameMenu.add(name.getName());
//        }
//
//        System.out.println(nameMenu);
//
//
//        ctx.attribute("nameMenu", nameMenu);
//        ctx.render("list.html");
        private static void menus(Context ctx, ConnectionPool connectionPool){
            ArrayList<String> menu = new ArrayList<>();
            menu.add("forside");
            menu.add("andet");
            menu.add("trejde");

            ctx.attribute("menu", menu);
            ctx.render("task.html");

        }
    }

