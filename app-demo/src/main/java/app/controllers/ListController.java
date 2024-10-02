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
//        app.post("deletelist", ctx -> deletetask(ctx, connectionPool));
//        app.post("edittask", ctx -> edittask(ctx, connectionPool));
//        app.post("updatetask", ctx -> updatetask(ctx, connectionPool));

    }

//    private static void updatetask(Context ctx, ConnectionPool connectionPool)
//    {
//        User user = ctx.sessionAttribute("currentUser");
//        try
//        {
//            int taskId = Integer.parseInt(ctx.formParam("taskId"));
//            String taskName = ctx.formParam("taskname");
//            TaskMapper.update(taskId, taskName, connectionPool);
//            List<Task> taskList = TaskMapper.getAllTasksPerUser(user.getUserId(), connectionPool);
//            ctx.attribute("taskList", taskList);
//            ctx.render("task.html");
//        }
//        catch (DatabaseException | NumberFormatException e)
//        {
//            ctx.attribute("message", e.getMessage());
//            ctx.render("index.html");
//        }
//    }

//    private static void edittask(Context ctx, ConnectionPool connectionPool)
//    {
//        User user = ctx.sessionAttribute("currentUser");
//        try
//        {
//            int taskId = Integer.parseInt(ctx.formParam("taskId"));
//            Task task = TaskMapper.getTaskById(taskId, connectionPool);
//            ctx.attribute("task", task);
//            ctx.render("edittask.html");
//        }
//        catch (DatabaseException | NumberFormatException e)
//        {
//            ctx.attribute("message", e.getMessage());
//            ctx.render("index.html");
//        }
//    }
//
//    private static void deletetask(Context ctx, ConnectionPool connectionPool)
//    {
//        User user = ctx.sessionAttribute("currentUser");
//        try
//        {
//            int taskId = Integer.parseInt(ctx.formParam("taskId"));
//            TaskMapper.delete(taskId, connectionPool);
//            List<Task> taskList = TaskMapper.getAllTasksPerUser(user.getUserId(), connectionPool);
//            ctx.attribute("taskList", taskList);
//            ctx.render("task.html");
//        }
//        catch (DatabaseException | NumberFormatException e)
//        {
//            ctx.attribute("message", e.getMessage());
//            ctx.render("index.html");
//        }
//    }



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