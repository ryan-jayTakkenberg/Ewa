package app.models;

import jakarta.persistence.*;
import java.util.Set;

@Entity
public class Report {

    /*
     * BACKEND: MODEL
     * Every model should have an @Entity annotation before the class.
     * Every model should also have an @Id annotation, every id must be unique!
     *
     * You can see all the tables in: 'localhost:8085/api/h2-console'
     * Remember to set the 'JDBC URL' to 'jdbc:h2:mem:testdb'
     */

    @Id
    @GeneratedValue
    private long report_id;
    private String body;
    private String date;
    private String sender;
    private String receiver_id;

    @ManyToOne
    private User app_user;

    public Report(long report_id, String body, String date, String sender, String receiver_id) {
        this.report_id = report_id;
        this.body = body;
        this.date = date;
        this.sender = sender;
        this.receiver_id = receiver_id;
    }

    public Report() {

    }

    public long getId() {
        return report_id;
    }

    public void setId(long report_id) {
        this.report_id = report_id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiverId() {
        return receiver_id;
    }

    public void setReceiverId(String receiver_id) {
        this.receiver_id = receiver_id;
    }
}
