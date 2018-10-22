package media.monmouth.server.controllers;

import io.netty.handler.codec.http.HttpMethod;

public class Route {
   public String endpoint;
   public HttpMethod method;

   public Route(String endpoint, HttpMethod method) {
       this.method = method;
       this.endpoint = endpoint;
   }

    public String getEndpoint() {
        return endpoint;
    }

    public HttpMethod getMethod() {
        return method;
    }
}
