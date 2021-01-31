package uk.co.nominet.stevemarks.postit.domain;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "postit")
public class PostIt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "text")
    @Size(min = 1, max = 200, message
            = "Text must be between 1 and 200 characters")
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
