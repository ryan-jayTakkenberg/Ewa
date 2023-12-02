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
    private long reportId;
    private String body;
    private String date;
    private int senderId;
    private String senderName;
    private int receiverId;

    @ManyToOne
    private User app_user;

    public Report(long reportId, String body, String date, int senderId, String senderName, int receiverId) {
        this.reportId = reportId;
        this.body = body;
        this.date = date;
        this.senderId = senderId;
        this.senderName = senderName;
        this.receiverId = receiverId;
    }

    public Report() {

    }

    public long getReportId() {
        return reportId;
    }

    public void setReportId(long reportId) {
        this.reportId = reportId;
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

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }
}
