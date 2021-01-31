package uk.co.nominet.stevemarks.postit.domain;


public class PostIt {

    private Long id;

    private String email;
    
    private String text;

    public PostIt() {
        //default constructor
    }

    public PostIt(Long id, String email, String text) {
        super();
        this.id = id;
        this.email = email;
        this.text = text;
    }

    @Override
    public String toString() {
        return "PostIt{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", text='" + text + '\'' +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
