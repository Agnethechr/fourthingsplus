package app.controllers;

import app.entities.Task;
import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.TaskMapper;
import app.persistence.UserMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;

public class UserController {
    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.post("login", ctx -> login(ctx, connectionPool));
        app.get("logout", ctx -> logout(ctx));
        app.get("forside", ctx -> forside(ctx));
        app.get("update", ctx -> update(ctx));
        app.post("update-brugernavn", ctx -> updateUserName(ctx, connectionPool));
        app.post("update-password", ctx -> updatePassword(ctx, connectionPool));
        app.get("createuser", ctx -> ctx.render("createuser.html"));
        app.post("createuser", ctx -> createUser(ctx, connectionPool));
    }


    private static void createUser(Context ctx, ConnectionPool connectionPool) {
        // Hent form parametre
        String username = ctx.formParam("username");
        String password1 = ctx.formParam("password1");
        String password2 = ctx.formParam("password2");

        if (password1.equals(password2)) {
            try {
                UserMapper.createuser(username, password1, connectionPool);
                ctx.attribute("message", "Du er hermed oprettet med brugernavn: " + username +
                        ". Nu skal du logge på.");
                ctx.render("index.html");
            } catch (DatabaseException e) {
                ctx.attribute("message", "Dit brugernavn findes allerede. Prøv igen, eller log ind");
                ctx.render("createuser.html");
            }
        } else {
            ctx.attribute("message", "Dine to passwords matcher ikke! Prøv igen");
            ctx.render("createuser.html");
        }

    }

    private static void updateUserName(Context ctx, ConnectionPool connectionPool) {
        User user = ctx.sessionAttribute("currentUser");
        String message;
        try {

            String username = ctx.formParam("brugernavn");
            UserMapper.updateUserName(username, user.getUserId(),connectionPool);

            ctx.attribute("message", "Du har nu ændret dit brugernavn");

            ctx.render("update.html");
        } catch (DatabaseException | NumberFormatException e) {

            ctx.attribute("message", e.getMessage());
            ctx.render("update.html");
        }
    }

    private static void updatePassword(Context ctx, ConnectionPool connectionPool) {
        User user = ctx.sessionAttribute("currentUser");
        String message;
        try {

            String password = ctx.formParam("password");
            UserMapper.updatePassword(password, user.getUserId(),connectionPool);

            ctx.attribute("message", "Du har nu ændret dit password");

            ctx.render("update.html");
        } catch (DatabaseException | NumberFormatException e) {

            ctx.attribute("message", e.getMessage());
            ctx.render("update.html");
        }
    }

    private static void forside(Context ctx) {
        ctx.render("/task.html");
    }

    private static void update(Context ctx) {
        ctx.render("/update.html");
    }

    private static void logout(Context ctx) {
        ctx.req().getSession().invalidate();
        ctx.redirect("/");
    }


    public static void login(Context ctx, ConnectionPool connectionPool) {
        // Hent form parametre
        String username = ctx.formParam("username");
        String password = ctx.formParam("password");

        // Check om bruger findes i DB med de angivne username + password
        try {
            User user = UserMapper.login(username, password, connectionPool);
            ctx.sessionAttribute("currentUser", user);
            // Hvis ja, send videre til list siden
            List<Lists> listList = ListMapper.getAllListsPerUser(user.getUserId(), connectionPool);
            System.out.println(listList);
            ctx.attribute("listList", listList);
            ctx.attribute("pbtest","Dette er pb testen!");
            ctx.render("list.html");
        }
        catch (DatabaseException e)
        {
            // Hvis nej, send tilbage til login side med fejl besked
            ctx.attribute("message", e.getMessage());
            ctx.render("index.html");
        }

    }
}