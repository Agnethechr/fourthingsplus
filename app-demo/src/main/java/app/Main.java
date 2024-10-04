package app;

import app.config.SessionConfig;
import app.config.ThymeleafConfig;
import app.controllers.ListController;
import app.controllers.MenuController;
import app.controllers.TaskController;
import app.controllers.UserController;
import app.persistence.ConnectionPool;
import app.persistence.ListMapper;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;


public class Main {
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";
    private static final String URL = "jdbc:postgresql://localhost:5432/%s?currentSchema=public";
    private static final String DB = "fourthingsplus";

    private static final ConnectionPool connectionPool = ConnectionPool.getInstance(USER, PASSWORD, URL, DB);

    public static void main(String[] args) {


        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/public");
            config.jetty.modifyServletContextHandler(handler -> handler.setSessionHandler(SessionConfig.sessionConfig()));
            config.fileRenderer(new JavalinThymeleaf(ThymeleafConfig.templateEngine()));
        }).start(7070);

//              Den laver menuerne, men fjerner stylingen på login -> der skal laves noget
//        app.before(ctx -> {
//            // Excluder login-siden og andre sider, som ikke kræver login
//            String path = ctx.path();
//            if (path.equals("/login") || path.equals("/")) {
//                return; // Skip menus() for disse ruter
//            }
//            // Tjek om currentUser findes i session
//            if (ctx.sessionAttribute("currentUser") == null) {
//                ctx.redirect("/");
//            } else {
//                // Kald kun menus() hvis bruger er logget ind
//                MenuController.menus(ctx, connectionPool);
//            }
//        });


        app.get("/", ctx -> ctx.render("index.html"));

        UserController.addRoutes(app, connectionPool);
        ListController.addRoutes(app, connectionPool);
        TaskController.addRoutes(app, connectionPool);
        MenuController.addRoutes(app, connectionPool);
    }
}