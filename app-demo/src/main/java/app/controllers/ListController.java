package app.controllers;

import app.entities.Lists;
import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.ListMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;

public class ListController
{
    public static void addRoutes(Javalin app, ConnectionPool connectionPool)
    {
        app.post("addlist", ctx -> addList(ctx, connectionPool));
        app.post("deletelist", ctx -> deletelist(ctx, connectionPool));
        app.post("editlist", ctx -> editlist(ctx, connectionPool));
        app.post("updatelist", ctx -> updatelist(ctx, connectionPool));

    }

    private static void updatelist(Context ctx, ConnectionPool connectionPool)
    {
        User user = ctx.sessionAttribute("currentUser");
        try
        {
            int listId = Integer.parseInt(ctx.formParam("listId"));
            String listName = ctx.formParam("listname");
            ListMapper.update(listId, listName, connectionPool);
            List<Lists> listList = ListMapper.getAllListsPerUser(user.getUserId(), connectionPool);
            ctx.attribute("listList", listList);
            ctx.render("list.html");
        }
        catch (DatabaseException | NumberFormatException e)
        {
            ctx.attribute("message", e.getMessage());
            ctx.render("index.html");
        }
    }

    private static void editlist(Context ctx, ConnectionPool connectionPool)
    {
        User user = ctx.sessionAttribute("currentUser");
        try
        {
            int listId = Integer.parseInt(ctx.formParam("listId"));
            Lists list = ListMapper.getListById(listId, connectionPool);
            ctx.attribute("list", list);
            ctx.render("editlist.html");
        }
        catch (DatabaseException | NumberFormatException e)
        {
            ctx.attribute("message", e.getMessage());
            ctx.render("index.html");
        }
    }

    private static void deletelist(Context ctx, ConnectionPool connectionPool)
    {
        User user = ctx.sessionAttribute("currentUser");
        try
        {
            int listId = Integer.parseInt(ctx.formParam("listId"));
            ListMapper.delete(listId, connectionPool);
            List<Lists> listList = ListMapper.getAllListsPerUser(user.getUserId(), connectionPool);
            ctx.attribute("listList", listList);
            ctx.render("list.html");
        }
        catch (DatabaseException | NumberFormatException e)
        {
            ctx.attribute("message", e.getMessage());
            ctx.render("index.html");
        }
    }



    private static void addList(Context ctx, ConnectionPool connectionPool)
    {
        String listName = ctx.formParam("listname");

        User user = ctx.sessionAttribute("currentUser");
        try
        {
            if (listName.length() >= 1)
            {
                Lists newList = ListMapper.addList(user, listName, connectionPool);
            } else
            {
                ctx.attribute("message", "Et liste navn skal mindst være 1 tegn!");
            }
            List<Lists> listList = ListMapper.getAllListsPerUser(user.getUserId(), connectionPool);
            ctx.attribute("listList", listList);
            ctx.render("list.html");
        }
        catch (DatabaseException e)
        {
            ctx.attribute("message", "Noget gik galt. Prøv evt. igen");
            ctx.render("list.html");
        }

    }
}