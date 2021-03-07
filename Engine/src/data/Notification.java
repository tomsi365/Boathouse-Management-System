package data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Notification implements Comparable, Serializable {

    public static Notification serach(List<Notification> notifications, int id) {
        for(Notification notification:notifications) {
            if (notification.id == id)
                return notification;
        }
        return null;
    }

    public static void calibrateNotificationsId(List<Notification> notifications){
        for(Notification notification:notifications){
            if(notification.getId()>lastIdRegistered)
                lastIdRegistered=notification.getId();
        }
    }

    private static int lastIdRegistered = 0;

    public enum Source {
        MANAGER,SYSTEM
    }

    private final String message;
    private final Date date;
    private final int id;
    private final Source source;
    private Boolean read;


    public Notification(String message,Source source) {
        this.message = message;
        this.date = new Date();
        this.id = 0;
        this.source=source;
        this.read=false;
    }

    public Notification(Notification notification) {
        this.message = notification.getMessage();
        this.date = notification.getDate();
        this.id = notification.getId();
        this.source=notification.getSource();
        this.read=false;
    }

    public Notification(String message) {
        this.message = message;
        this.date = new Date();
        lastIdRegistered++;
        this.id = lastIdRegistered;
        this.source=Source.MANAGER;
        this.read=false;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public int compareTo(Object o) {
        return -1*this.date.compareTo(
                ((Notification)o).getDate()
        );
    }

    public int getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public Source getSource() {
        return source;
    }

    public void setRead(Boolean read) {
        this.read = read;
    }

    public Boolean getRead() {
        return read;
    }

    public String[] toArray(){
        String [] arr=new String[5];
        arr[0]= String.valueOf(id);
        arr[1]= String.valueOf(date);
        arr[2]=message;
        arr[3]= String.valueOf(source);
        arr[4]= String.valueOf(read);
        return arr;
    }
}
