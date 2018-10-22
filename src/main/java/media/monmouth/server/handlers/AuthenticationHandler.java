package media.monmouth.server.handlers;

import io.netty.handler.codec.http.*;
import media.monmouth.server.controllers.Controller;
import media.monmouth.server.controllers.LoginController;
import media.monmouth.server.controllers.LogoutController;
import media.monmouth.server.controllers.Route;

import java.util.HashMap;
import java.util.Map;

public class AuthenticationHandler extends Handler {
    public static final Map<Route, Controller> routes = setRoutes();

    private static Map<Route, Controller> setRoutes() {
        Map<Route, Controller> endpoints = new HashMap<>();
        endpoints.put(new Route("/login", HttpMethod.POST), new LoginController());
        endpoints.put(new Route("/logout", HttpMethod.POST), new LogoutController());
        return endpoints;
    }


    public AuthenticationHandler(Map<Route, Controller> routes) {
        super(routes);
    }
}