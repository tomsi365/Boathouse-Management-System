package data;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

public class Placement implements Serializable {
    //Searches a member , by email and password .
    public static Placement search(final Set<Placement> placements, int regId) {
        for(Placement placement:placements) {
            if (placement.getRegistration().getId()==regId)
                return placement;
        }
        return null;
    }

    private final Registration registration;
    private Boat boat;

    public Placement(Boat boat,Registration registration) {
        this.registration = registration;
        this.boat = boat;
    }

    //Getters .

    public Boat getBoat() {
        return boat;
    }

    public Registration getRegistration() {
        return registration;
    }

    //Setters .

    public void setBoat(Boat boat) {
        this.boat = boat;
    }

    //Object functions .

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Placement placement = (Placement) o;
        return Objects.equals(registration, placement.registration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(registration);
    }

    //Return representative array .
    public String[] toArray(){
        String arr [] = new String[10];

        arr[0]=String.valueOf(this.registration.getId());
        arr[1]=String.valueOf(this.registration.getMember().getId());
        arr[2]=String.valueOf(this.registration.getReqDay());
        arr[3]=String.valueOf(this.registration.getReqMonth());
        arr[4]=String.valueOf(this.registration.getReqYear());
        arr[5]=String.valueOf(this.registration.getWindow().getName());
        arr[6]=String.valueOf(this.registration.getRegistrationDate());

        arr[7]="";
        for(Object friend : this.registration.getFriends()){
            arr[7]+=String.valueOf(((Member)friend).getId());
            arr[7]+=Registration.FRIEND_DELIMITER;
        }

        arr[8]="";
        for(Object type : this.registration.getTypes()){
            arr[8]=String.valueOf(type);
            arr[8]+=Registration.TYPE_DELIMITER;
        }

        arr[9]=String.valueOf(this.boat.getName());

        return arr;
    }
}
