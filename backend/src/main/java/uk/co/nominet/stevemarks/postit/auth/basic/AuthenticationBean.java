package uk.co.nominet.stevemarks.postit.auth.basic;

public class AuthenticationBean {

    private String message;

    public AuthenticationBean() {
    }

    public AuthenticationBean(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return String.format("HelloWorldBean [message=%s]", message);
    }

}
