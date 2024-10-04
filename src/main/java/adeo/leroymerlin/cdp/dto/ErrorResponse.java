package adeo.leroymerlin.cdp.dto;

public class ErrorResponse {

    public ErrorResponse(String message, String code) {
        this.message = message;
        this.code = code;
    }
    private String message;
    private String code;

    public String getMessage() {
        return message;
    }
    public String getCode() {
        return code;
    }
}
