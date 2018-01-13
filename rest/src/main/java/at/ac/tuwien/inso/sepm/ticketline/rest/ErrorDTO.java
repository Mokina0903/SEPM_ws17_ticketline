package at.ac.tuwien.inso.sepm.ticketline.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiModelProperty;

import java.io.IOException;


public class ErrorDTO extends Exception {
    @ApiModelProperty(required = true, name = "timestamp of error")
    private String timestamp;

    @ApiModelProperty(required = true, name = "status of error")
    private long status;

    @ApiModelProperty(required = true, name = "type of error")
    private String error;

    @ApiModelProperty(required = true, name = "message of error")
    private String message;

    @ApiModelProperty(required = true, name = "path of error")
    private String path;

    public ErrorDTO() {

    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return "ErrorDTO{" +
            ", status=" + status +
            ", error='" + error + '\'' +
            ", message='" + message + '\'' +
            '}';
    }

    public static ErrorDTO ErrorDTOBuilder(String message) {
        ObjectMapper mapper = new ObjectMapper();

        ErrorDTO errorDTO = null;
        try {
            errorDTO = mapper.readValue(message, ErrorDTO.class);
        } catch (IOException e) {
            //e.printStackTrace();
        }

        return errorDTO;
    }
}
