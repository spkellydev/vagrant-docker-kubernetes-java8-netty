package media.monmouth.server.exceptions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "status", "message" })
@JsonIgnoreProperties({ "stackTrace", "suppressed" })
public class HttpJsonResponseException extends Exception {
    private String status = "error";

    public HttpJsonResponseException(String s) {
        super(s);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
