package data;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

public class Window implements Serializable {

    //Searches a window , by name .
    public static Window search(final Set<Window> windows, String name) {
        for(Window window : windows) {
            if (window.name.equals(name))
                return window;
        }
        return null;
    }

    private final String name;
    private String begin;
    private String end;
    private boolean isSeaBoat;

    public enum Time {
        EIGHT_TEN_PM,TEN_TWELVE_PM,TWELVE_TWO_AM,TWO_FOUR_AM,FOUR_SIX_AM,SIX_EIGHT_AM
    }

    public Window(String name,Time time,boolean isSeaBoat) {
        this.name = name;
        setTime(time);
        this.isSeaBoat=isSeaBoat;
    }

    //Getters .

    public String getName() {
        return name;
    }

    public String getBegin() {
        return begin;
    }

    public String getEnd() {
        return end;
    }

    public boolean isSeaBoat() {
        return this.isSeaBoat;
    }

    //Setters .

   public void setTime(Time time) {
        if(time== Time.EIGHT_TEN_PM){
            this.begin ="08";this.end="10";}
       if(time== Time.TEN_TWELVE_PM){
           this.begin ="10";this.end="12";}
       if(time== Time.TWELVE_TWO_AM){
           this.begin ="12";this.end="14";}
       if(time== Time.TWO_FOUR_AM){
           this.begin ="14";this.end="16";}
       if(time== Time.FOUR_SIX_AM) {
           this.begin ="16";this.end="18";}
       if(time== Time.SIX_EIGHT_AM){
           this.begin ="18";this.end="20";}
   }

    public void setSeaBoat(boolean isSeaBoat) {
        this.isSeaBoat = isSeaBoat;
    }

    public void setBegin(String begin) {
        this.begin = begin;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    //Object functions .
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Window window = (Window) o;
        return (begin == window.begin &&
                end == window.end) ||
                Objects.equals(name, window.name);
    }

    @Override
    public int hashCode() {
        return 0;
    }

    //Return representative array .
    public String[] toArray(){
        String arr [] = new String[4];

        arr[0]=String.valueOf(this.name);
        arr[1]=String.valueOf(this.begin);
        arr[2]=String.valueOf(this.end);
        arr[3]=String.valueOf(this.isSeaBoat);

        return arr;
    }

    //Return representative secure array .
    public String[] toSecureArray(){
        String arr [] = new String[1];
        arr[0]=String.valueOf(this.name);
        return arr;
    }
}
