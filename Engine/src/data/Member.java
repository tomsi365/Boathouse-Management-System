package data;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import java.io.Serializable;
import java.util.*;

public class Member implements Serializable {

    public static final String NOTE_DELIMITER ="#";
    private static int lastIdRegistered = 0;

    //Searches a member , by email and password .
    public static Member search(final Set<Member> members, String email, String password) {
        for(Member member : members) {
            if (member.password.equals(password)&&member.email.equals(email))
                return member;
        }
        return null;
    }

    //Searches a member , by id .
    public static Member search(final Set<Member> members, int id) {
        for(Member member : members) {
            if (member.id == id)
                return member;
        }
        return null;
    }

    public enum Level {
        BEGINNER, NORMAL, EXPERT
    }

    private final int id;
    private final Date joiningDate;
    private List<String> notes;

    private String name;
    private int age;
    private Level level;
    private Date expiryDate;
    private String phone;
    private String email;
    private String password;
    private boolean isManger;
    private boolean isHasPrivateBoat;
    private int privateBoatSerialNumber;

    public Member(String name, int age, Level level, Date expiryDate, String phone, String email, String password, boolean isManger) {

        this.joiningDate = new Date();
        this.notes = new ArrayList<>();
        lastIdRegistered++;
        this.id = lastIdRegistered;

        this.name = name;
        this.password = password;
        this.age = age;
        this.phone = phone;
        this.email = email;
        this.level = level;
        this.isManger = isManger;
        this.isHasPrivateBoat=false;
        this.privateBoatSerialNumber=0;
        this.expiryDate=expiryDate;

        this.notofications=new ArrayList<>();
    }

    public Member(int id,String name, int age, Level level, Date expiryDate, Date joiningDate,String phone, String email, String password,int privateBoatSerialNumber, boolean isManger,boolean isHasPrivateBoat,List<String> notes) {
        this.joiningDate = joiningDate;
        this.notes = notes;
        if(id>lastIdRegistered)
            lastIdRegistered=id;
        this.id = id;
        this.name = name;
        this.password = password;
        this.age = age;
        this.phone = phone;
        this.email = email;
        this.level = level;
        this.isManger = isManger;
        this.isHasPrivateBoat=isHasPrivateBoat;
        this.privateBoatSerialNumber=privateBoatSerialNumber;
        this.expiryDate=expiryDate;

        this.notofications=new ArrayList<>();
    }

    //Getters .

    public int getId() {
        return id;
    }

    public Date getJoiningDate() {
        return joiningDate;
    }

    public boolean isHasPrivateBoat() {
        return isHasPrivateBoat;
    }

    public int getPrivateBoatSerialNumber() {
        return privateBoatSerialNumber;
    }

    public int getAge() {
        return age;
    }

    public Level getLevel() {
        return level;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public boolean isManger() {
        return isManger;
    }

    public String getName() { return name; }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    //Setters .

    public void setPrivateBoatSerialNumber(Set<Boat> boats,int privateBoatSerialNumber) {
        if(this.privateBoatSerialNumber>0){
            if(Boat.search(boats,this.privateBoatSerialNumber).setOwned(false)){
            this.isHasPrivateBoat=false;
            this.privateBoatSerialNumber=0;
            }
        }
        if(privateBoatSerialNumber>0) {
            if(Boat.search(boats,privateBoatSerialNumber).setOwned(true)){
            this.isHasPrivateBoat=true;
            this.privateBoatSerialNumber=privateBoatSerialNumber;
            }
        }
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public void setManger(boolean manger) {
        isManger = manger;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    //Note functions

    public void addNote(String note) {
        this.notes.add(note);
    }

    public void removeNote(int index) {
        this.notes.remove(index);
    }

    public void editNote(int index,String note) {
        this.notes.set(index,note);
    }

    public Object[] getNotes(){
        return this.notes.toArray();
    }

    //Object functions

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return Objects.equals(email, member.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }


    //Return representative array .
    public String[] toArray(){
        String arr [] = new String[13];

        arr[0]=String.valueOf(this.id);
        arr[1]=String.valueOf(this.name);
        arr[2]=String.valueOf(this.email);
        arr[3]=String.valueOf(this.password);
        arr[4]=String.valueOf(this.age);
        arr[5]=String.valueOf(this.phone);
        arr[6]=String.valueOf(this.level);
        arr[7]=String.valueOf(this.isHasPrivateBoat);
        arr[8]=String.valueOf(this.privateBoatSerialNumber);
        arr[9]=String.valueOf(this.joiningDate);

        //arr[10]=String.valueOf(this.expiryDate);
        GregorianCalendar cal1=new GregorianCalendar();
        cal1.setTime(this.expiryDate);
        try {
            arr[10]= String.valueOf(DatatypeFactory.newInstance().newXMLGregorianCalendar(cal1)).substring(0,10);
        }
        catch (DatatypeConfigurationException e) {

        }

        arr[11]=String.valueOf(this.isManger);

        arr[12]="";
        for(String note : this.notes){
            arr[12]+=String.valueOf(note);
            //arr[12]+=Member.NOTE_DELIMITER;
        }

        return arr;
    }

    //Return representative secure array .
    public String[] toSecureArray(){
        String arr [] = new String[2];
        arr[0]=String.valueOf(this.id);
        arr[1]=String.valueOf(this.name);
        return arr;
    }

    //Additional functions .

    private List<Notification> notofications;

    public void notificate(Notification notification){
        this.notofications.add(notification);
        Collections.sort(this.notofications);
    }

    public void read(){
        this.notofications.forEach(notification -> notification.setRead(true));
    }

    public void removeNotification(int id) {
        this.notofications.removeIf(notification -> notification.getId()==id);
    }

    public List<Notification> pull(){
        List<Notification> notifications=new ArrayList<>();
        notifications.addAll(this.notofications);
        return notifications;
    }
}
