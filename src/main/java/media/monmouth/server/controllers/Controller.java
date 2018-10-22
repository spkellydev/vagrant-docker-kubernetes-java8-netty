package media.monmouth.server.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.handler.codec.http.HttpResponseStatus;
import media.monmouth.server.models.Model;

import java.io.IOException;

/**
 * Controller abstract base class provides structure and functionality for route controllers
 */
public abstract class Controller {
    private final ObjectMapper mapper = new ObjectMapper();
    HttpResponseStatus code;

    /**
     * Receives json from client request and processes that data, to eventually return
     * a response to the client in the form of an unmarshaled Model
     * @see this.mapFromJson
     * @param json request
     * @return json response
     */
    abstract public String handle(String json);

    /**
     * Recover from fatal exception
     * @param e Exception
     * @return Exception Json response
     */
    abstract public String recover(Exception e);

    public HttpResponseStatus getCode() {
        return code;
    }

    /**
     * Marshals json to model
     * @param requestJson Json request
     * @param model any Class extending Model
     * @return Object marshaled request
     * @throws IOException see Jackson
     */
    protected Object mapFromJson(String requestJson, Class<? extends Model> model) throws IOException {
        return mapper.readValue(requestJson, model);
    }

    /**
     * Unc
     * @param c
     * @return
     * @throws JsonProcessingException
     */
    protected String mapToJson(Object c) throws JsonProcessingException {
        return mapper.writeValueAsString(c);
    }
}
