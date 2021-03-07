package data;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

public class Boat implements Serializable {

    private static int lastSerialNumberRegistered = 0;

    //Searches a boat , by serial number .
    public static Boat search(final Set<Boat> boats, int serialNumber) {
        for(Boat boat : boats){
            if(boat.serialNumber==serialNumber)
                return boat;
        }
        return null;
    }

    public enum BoatType
    {
        SINGLE,DOUBLE,DOUBLE_ONE_PADDLE,QUARTET,QUARTET_ONE_PADDLE,EIGHT,EIGHT_ONE_PADDLE
    }

    private final int serialNumber;
    private boolean isOwned;

    private String name;
    private BoatType type;
    private boolean isWideBoat;
    private boolean isSeaBoat;
    private boolean isCoxswain;
    private boolean isDisabled;

    public Boat(String name, BoatType type, boolean isWideBoat, boolean isSeaBoat, boolean isCoxswain, boolean isDisabled) {
        lastSerialNumberRegistered++;
        this.serialNumber=lastSerialNumberRegistered;
        this.isOwned = false;

        this.name = name;
        this.type = type;
        this.isWideBoat = isWideBoat;
        this.isSeaBoat = isSeaBoat;
        this.isDisabled = isDisabled;
        this.isCoxswain=isCoxswain;

        if(type== BoatType.SINGLE)
            this.isCoxswain=false;
        if(type== BoatType.EIGHT_ONE_PADDLE||type== BoatType.EIGHT)
            this.isCoxswain=true;
    }

    public Boat(int serialNumber,String name, BoatType type, boolean isWideBoat, boolean isSeaBoat, boolean isCoxswain, boolean isDisabled,boolean isOwned) {
        if(serialNumber>lastSerialNumberRegistered)
            lastSerialNumberRegistered=serialNumber;
        this.serialNumber=serialNumber;
        this.name = name;
        this.type = type;
        this.isWideBoat = isWideBoat;
        this.isSeaBoat = isSeaBoat;
        this.isDisabled = isDisabled;
        this.isCoxswain=isCoxswain;
        this.isOwned=isOwned;
    }

    //Getters .

    public int getSerialNumber() {
        return serialNumber;
    }

    public boolean isOwned() {
        return isOwned;
    }

    public String getName() {
        return name;
    }

    public BoatType getType() {
        return type;
    }

    public boolean isWideBoat() {
        return isWideBoat;
    }

    public boolean isSeaBoat() {
        return isSeaBoat;
    }

    public boolean isCoxswain() {
        return isCoxswain;
    }

    public boolean isDisabled() {
        return isDisabled;
    }

    //Setters .

    public boolean setOwned(boolean owned) {
        if(!(owned&&this.isOwned)){
            this.isOwned = owned;
            return true;
        }
        return false;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(BoatType type) {
        if(!(((type== BoatType.EIGHT_ONE_PADDLE||type== BoatType.EIGHT)&&!this.isCoxswain)||(type== BoatType.SINGLE&&this.isCoxswain)))
            this.type = type;
    }

    public void setWideBoat(boolean wideBoat) {
        isWideBoat = wideBoat;
    }

    public void setSeaBoat(boolean seaBoat) {
        isSeaBoat = seaBoat;
    }

    public void setCoxswain(boolean coxswain) {
        if(!(((this.type== BoatType.EIGHT_ONE_PADDLE||this.type== BoatType.EIGHT)&&!coxswain)||(this.type== BoatType.SINGLE&&coxswain)))
            this.isCoxswain=coxswain;
    }

    public void setDisabled(boolean disabled) {
        isDisabled = disabled;
    }

    //Object Functions .

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Boat boat = (Boat) o;
        return Objects.equals(name, boat.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    //Return representative array .
    public String[] toArray(){
        String arr [] = new String[8];

        arr[0]=String.valueOf(this.serialNumber);
        arr[1]=String.valueOf(this.name);
        arr[2]=String.valueOf(this.type);
        arr[3]=String.valueOf(this.isOwned);
        arr[4]=String.valueOf(this.isCoxswain);
        arr[5]=String.valueOf(this.isSeaBoat);
        arr[6]=String.valueOf(this.isWideBoat);
        arr[7]=String.valueOf(this.isDisabled);

        return arr;
    }

    //Return representative secure array .
    public String[] toSecureArray(){
        String arr [] = new String[2];
        arr[0]=String.valueOf(this.serialNumber);
        arr[1]=String.valueOf(this.name);
        return arr;
    }
}
