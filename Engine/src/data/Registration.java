package data;

import exception.EngineException;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Registration implements Serializable {

    public static final String FRIEND_DELIMITER ="#";
    public static final String TYPE_DELIMITER ="#";
    private static int lastIdRegistered = 0;

    public static void calibrateRegsId(Set<Registration> registrations){
        for(Registration registration:registrations){
            if(registration.getId()>lastIdRegistered)
                lastIdRegistered=registration.getId();
        }
    }

    //Searches a registration , by parameters .
    public static Registration search(Set<Registration> registrations,int id) {
        for(Registration registration : registrations){
            if(registration.id==id)
                return registration;
        }
        return null;
    }

    private final Date registrationDate;
    private final int id;
    private Set<Member> friends;
    private Set<Boat.BoatType> types;

    private final Member member;
    private int reqDay, reqMonth, reqYear;
    private Window window;
    private boolean isConfirmed;

    public Registration(Member member, int reqDay, int reqMonth, int reqYear, Window window, Boat.BoatType type) {
        friends=new HashSet<>();
        types=new HashSet<>();
        lastIdRegistered++;
        this.id = lastIdRegistered;

        types.add(type);
        this.registrationDate=new Date();

        this.member = member;
        this.reqDay = reqDay;
        this.reqMonth = reqMonth;
        this.reqYear = reqYear;
        this.window = window;
        this.isConfirmed=false;
    }

    public Registration(Registration source, int reqDay, int reqMonth, int reqYear) {
        friends=new HashSet<>(source.friends);
        types=new HashSet<>(source.types);
        lastIdRegistered++;
        this.id = lastIdRegistered;
        this.registrationDate=new Date();

        this.reqDay = reqDay;
        this.reqMonth = reqMonth;
        this.reqYear = reqYear;

        this.member = source.member;
        this.window = source.window;
        this.isConfirmed=false;

    }

    //Getters .

    public Member getMember() {
        return member;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public int getReqDay() {
        return reqDay;
    }

    public int getReqMonth() {
        return reqMonth;
    }

    public int getReqYear() {
        return reqYear;
    }

    public Window getWindow() {
        return window;
    }

    public boolean isConfirmed(){
        return this.isConfirmed;
    }

    public int getId() {
        return id;
    }

    //Setters .

    public void setReqDay(int reqDay) {
        this.reqDay = reqDay;
    }

    public void setReqMonth(int reqMonth) {
        this.reqMonth = reqMonth;
    }

    public void setReqYear(int reqYear) {
        this.reqYear = reqYear;
    }

    public void setWindow(Window window) {
        this.window = window;
    }

    public void setConfirmed(boolean b) {
        this.isConfirmed=b;
    }

    //Friend functions .

    private void fixTypes() {
        this.types.add(Boat.BoatType.SINGLE);
        if (this.friends.size() > 0) {
            this.types.add(Boat.BoatType.DOUBLE_ONE_PADDLE);
            this.types.add(Boat.BoatType.DOUBLE);
            this.types.remove(Boat.BoatType.SINGLE);
        }
        if (this.friends.size() > 1) {
            this.types.add(Boat.BoatType.QUARTET_ONE_PADDLE);
            this.types.add(Boat.BoatType.QUARTET);
            this.types.remove(Boat.BoatType.DOUBLE_ONE_PADDLE);
            this.types.remove(Boat.BoatType.DOUBLE);
            this.types.remove(Boat.BoatType.SINGLE);
        }
        if (this.friends.size() > 3) {
            this.types.add(Boat.BoatType.EIGHT_ONE_PADDLE);
            this.types.add(Boat.BoatType.EIGHT);
            this.types.remove(Boat.BoatType.QUARTET_ONE_PADDLE);
            this.types.remove(Boat.BoatType.QUARTET);
            this.types.remove(Boat.BoatType.DOUBLE_ONE_PADDLE);
            this.types.remove(Boat.BoatType.DOUBLE);
            this.types.remove(Boat.BoatType.SINGLE);
        }
    }

    public void addFriend(Member friend) throws EngineException {
        if(this.friends.size()<7&&!friend.equals(this.member)){
            this.friends.add(friend);
            fixTypes();
        }
        else
            throw new EngineException("Friends capacity is full !");
    }

    public void removeFriend(Member friend) {
        this.friends.remove(friend);
        fixTypes();
    }

    public Object[] getFriends() {
        return this.friends.toArray();
    }

    public boolean isAnyLevel(Member.Level level){
        return this.friends.stream().anyMatch(member1 -> member1.getLevel().equals(level))
                ||
                this.member.getLevel().equals(level);
    }

    public boolean isFriend(Member member){
        return this.friends.contains(member);
    }

    public boolean isIdFriendExisted(int id){
        return this.friends.stream().anyMatch(member1 -> member1.getId()==id);
    }

    //Type functions .

    public void addType(Boat.BoatType type) {
        this.types.add(type);
        fixTypes();
    }

    public void removeType(Boat.BoatType type) {
        this.types.remove(type);
        fixTypes();
    }

    public Object[] getTypes() {
        return this.types.toArray();
    }

    public boolean isTypeExisted(Boat.BoatType type){
        return this.types.stream().anyMatch(boatType -> boatType.equals(type));
    }


    //Object functions .


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Registration that = (Registration) o;
        return reqDay == that.reqDay &&
                reqMonth == that.reqMonth &&
                reqYear == that.reqYear &&
                Objects.equals(member, that.member) &&
                Objects.equals(window, that.window);
    }

    @Override
    public int hashCode() {
        return Objects.hash(member, reqDay, reqMonth, reqYear, window);
    }

    //Return representative array .
    public String[] toArray(){
        String arr [] = new String[9];

        arr[0]=String.valueOf(this.id);
        arr[1]=String.valueOf(this.member.getId());
        arr[2]=String.valueOf(this.reqDay);
        arr[3]=String.valueOf(this.reqMonth);
        arr[4]=String.valueOf(this.reqYear);
        arr[5]=String.valueOf(this.window.getName());
        arr[6]=String.valueOf(this.registrationDate);

        arr[7]="";
        for(Member friend : this.friends){
            arr[7]+=String.valueOf(friend.getId());
            arr[7]+=Registration.FRIEND_DELIMITER;
        }

        arr[8]="";
        for(Boat.BoatType type : this.types){
            arr[8]+=String.valueOf(type);
            arr[8]+=Registration.TYPE_DELIMITER;
        }

        return arr;
    }

    //Return representative secure array .
    public String[] toSecureArray(){
        String arr [] = new String[1];
        arr[0]=String.valueOf(this.id);
        return arr;
    }

}
