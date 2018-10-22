package media.monmouth.server.controllers;

import io.netty.handler.codec.http.HttpResponseStatus;

// TODO: Does this need to even be here now? There's no auth flow...
public class LogoutController extends Controller {
    public String handle(String json) {
        this.code = HttpResponseStatus.OK;
        return "{ \"status\": \"success, you were logged out.\" }";
    }

    public String recover(Exception e) {
        return e.getMessage();
    }
}
