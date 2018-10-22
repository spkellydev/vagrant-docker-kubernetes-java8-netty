package media.monmouth.server.controllers;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import io.netty.handler.codec.http.HttpResponseStatus;
import media.monmouth.server.exceptions.HttpJsonResponseException;
import media.monmouth.server.models.User;

import java.io.IOException;

/**
 * LoginController handles post requests for login route.
 */
public class LoginController extends Controller  {
    /**
     * @see Controller
     * @param json from Handler
     * @return Unmarshaled Model
     */
    public String handle(String json) {
        String response = null;
        try
        {   // Map our JSON to a Java object
            final User user = (User) this.mapFromJson(json, User.class);
            // set the response to client
            this.code = HttpResponseStatus.OK;
            response = this.mapToJson(user);
        }
        catch (JsonParseException | JsonMappingException e)
        {   // Jackson related errors
            response = recover(e);
        }
        catch (IOException e)
        {   // Implementation related error
            response = recover(e);
        }
        return response;
    }

    /**
     * @see Controller
     * @param e error
     * @return Unmarshaled exception
     */
    public String recover(Exception e) {
        HttpJsonResponseException jacksonError = this.handleError(e);
        try
        {
            return this.mapToJson(jacksonError);
        }
        catch (JsonProcessingException e1)
        {
            e1.printStackTrace();
        }
        return "failure";
    }

    // process error message
    private HttpJsonResponseException handleError(Exception e) {
        if(e instanceof JsonMappingException)
        {
            this.code = HttpResponseStatus.NOT_IMPLEMENTED;
            return new HttpJsonResponseException("Add setters and getters to Model.");
        }
        else if (e instanceof JsonParseException)
        {
            this.code = HttpResponseStatus.NOT_ACCEPTABLE;
            return new HttpJsonResponseException("Bad content type, try using application/json.");
        }
        else
        {
            this.code = HttpResponseStatus.INTERNAL_SERVER_ERROR;
            return new HttpJsonResponseException("Unknown error");
        }
    }
}
