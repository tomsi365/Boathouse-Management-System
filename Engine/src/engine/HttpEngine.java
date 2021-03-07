package engine;

import data.*;
import exception.EngineException;
import io.ObjectUtilities;

import java.util.Collections;
import java.util.Date;
import java.util.List;

public class HttpEngine extends LocalEngine{

    private List<Notification> notifications;

    public HttpEngine() {
        super();
        this.notifications= ObjectUtilities.readNotifications();
        Notification.calibrateNotificationsId(this.notifications);
        for(Member member:this.members){
            for(Notification notification:this.notifications){
                member.notificate(new Notification(notification));
            }
        }
    }

    public void exit() {
        super.exit();
        ObjectUtilities.writeNotifications(this.notifications);
    }

    public void init() throws EngineException {
        if(!this.isManager)
            throw new EngineException("Access Denied !");

        addBoat("49", Boat.BoatType.QUARTET,false,false,false,false);
        addBoat("Blank Space", Boat.BoatType.SINGLE,false,false,false,false);
        addBoat("Titanic", Boat.BoatType.SINGLE,false,true,false,false);
        addBoat("65", Boat.BoatType.DOUBLE,false,false,false,false);
        addBoat("FiveFive", Boat.BoatType.DOUBLE,true,false,false,false);
        addBoat("110", Boat.BoatType.EIGHT,false,false,true,false);

        addMember("Beyonce Knowels",40, Member.Level.BEGINNER,new Date("14/02/2004"),"0510000001","queen@beyonce.com","apeshit",false);
        addMember("Alex",46, Member.Level.EXPERT,new Date("14/02/2050"),"0520000002","alex@drc.org.il","4321",true);
        addMember("Taylor Swift",33, Member.Level.EXPERT,new Date("14/02/2004"),"0530000003","taylor@swift.com","1989",false);
        addMember("Dror",60, Member.Level.EXPERT,new Date("14/02/2050"),"0550000005","dror@drc.org.il","1234",true);
        addMember("Britney Spears",50, Member.Level.NORMAL,new Date("14/02/2004"),"0540000004","britney23@hotmail.com","password",false);

        addWindow("Rowing at 6", Window.Time.SIX_EIGHT_AM,false);
        addWindow("Group practice", Window.Time.EIGHT_TEN_PM,true);
    }

    //Notification functions .

    public void addNotification(Notification notification) throws EngineException {
        if(!this.isManager)
            throw new EngineException("Access Denied !");
        this.notifications.add(notification);
        Collections.sort(this.notifications);
        for(Member member:this.members){
            member.notificate(new Notification(notification));
        }
    }

    public void notificatePlacementConfirmed(Placement placement) {
        String message="Hello "+placement.getRegistration().getMember().getName()+" , registration No."+placement.getRegistration().getId()+" confirmed!";

        Member.search(this.members,placement.getRegistration().getMember().getId())
                .notificate(new Notification(message, Notification.Source.SYSTEM));

        for(Object obj:placement.getRegistration().getFriends()){
            String message1="Hello "+((Member)obj).getName()+" , registration No."+placement.getRegistration().getId()+" confirmed!";
            Member.search(this.members, ((Member)obj).getId())
                    .notificate(new Notification(message1, Notification.Source.SYSTEM));
        }
    }

    public void notificatePlacementCancelled(Placement placement) {
        String message="Hello "+placement.getRegistration().getMember().getName()+" , registration No."+placement.getRegistration().getId()+" cancelled!";

        Member.search(this.members,placement.getRegistration().getMember().getId())
                .notificate(new Notification(message, Notification.Source.SYSTEM));

        for(Object obj:placement.getRegistration().getFriends()){
            String message1="Hello "+((Member)obj).getName()+" , registration No."+placement.getRegistration().getId()+" cancelled!";
            Member.search(this.members, ((Member)obj).getId())
                    .notificate(new Notification(message1, Notification.Source.SYSTEM));
        }
    }

    public void notificateAdded(int memId,int regId){
        Member member=Member.search(this.members,memId);
        String message="Hello "+member.getName()+" , you were added to registration No."+regId+" !";
        member.notificate(new Notification(message, Notification.Source.SYSTEM));
    }

    public void notificateRemoved(int memId,int regId){
        Member member=Member.search(this.members,memId);
        String message="Hello "+member.getName()+" , you were removed from registration No."+regId+" !";
        member.notificate(new Notification(message, Notification.Source.SYSTEM));
    }

    public void read() {
        Member.search(this.members,this.id).read();
    }

    public boolean isThereNewNotifications(){
        return Member
                .search(this.members,this.id)
                .pull()
                .stream()
                .anyMatch(notification -> !notification.getRead());
    }

    public void deleteNotification(int id) throws EngineException {
        if(!this.isManager)
            throw new EngineException("Access Denied !");
        Notification notification=Notification.serach(this.notifications,id);
        for(Member member:this.members){
            member.removeNotification(notification.getId());
        }
        this.notifications.remove(notification);
    }

    private String[][] getNotifications(Object [] notification_array) {
        String [][] string_array = new String [notification_array.length][5];
        for(int i=0;i<notification_array.length;i++){
            string_array[i]=((Notification)notification_array[i]).toArray();
        }
        return  string_array;
    }

    public String[][] getAllManagerNotifications() {
        Object [] notification_array=this.notifications
                .toArray();
        return  getNotifications(notification_array);
    }

    public String[][] getAllCombinedNotifications() {
        Object [] notification_array=Member.search(this.members,this.id)
                .pull()
                .toArray();
        return  getNotifications(notification_array);
    }

    //Additional functions .

    private boolean isAnyMemberDependsOnBoat(Boat boat){
        return this.members
                .stream()
                .anyMatch(member -> member.getPrivateBoatSerialNumber()== boat.getSerialNumber());
    }

    public boolean isManagerConnected()
    {
        return this.isManager;
    }

    public void addNote(String email,String note) throws EngineException {
        if(!this.isManager)
            throw new EngineException("Access Denied !");
        for(Member member:this.members){
            if(member.getEmail().equals(email)){
                member.addNote(note);
            }
        }
    }

    public String[][] getSecureAllMembers() {
        Object [] member_array;
        String [][] string_array;

        member_array=this.members.toArray();
        string_array = new String [member_array.length][2];
        for(int i=0;i<member_array.length;i++){
            string_array[i]=((Member)member_array[i]).toSecureArray();
        }

        return  string_array;
    }

    public String[][] getAllFriends(int regId) throws EngineException {
        Object [] member_array;
        String [][] string_array;
        Registration reg=Registration.search(this.registrations,regId);
        if(!this.isManager&&reg.getMember().getId()!=this.id&&!reg.isIdFriendExisted(this.id))
            throw new EngineException("Access Denied !");

        member_array=reg.getFriends();
        string_array = new String [member_array.length][2];
        for(int i=0;i<member_array.length;i++){
            string_array[i]=((Member)member_array[i]).toSecureArray();
        }

        return  string_array;
    }

    public String[] getAllRegsTypes(int regId) throws EngineException {
        Object [] type_array;
        String [] string_array;
        Registration reg=Registration.search(this.registrations,regId);
        if(!this.isManager&&reg.getMember().getId()!=this.id&&!reg.isIdFriendExisted(this.id))
            throw new EngineException("Access Denied !");

        type_array=reg.getTypes();
        string_array = new String [type_array.length];
        for(int i=0;i<type_array.length;i++){
            string_array[i]= String.valueOf(((Boat.BoatType)type_array[i]));
        }

        return  string_array;
    }

    public String[][] getSecureAllWindows() {
        Object [] window_array;
        String [][] string_array;

        window_array=this.windows.toArray();
        string_array = new String [window_array.length][1];
        for(int i=0;i<window_array.length;i++){
            string_array[i]=((Window)window_array[i]).toSecureArray();
        }

        return  string_array;
    }

    public String[] getDetails() throws EngineException {
        if(this.id<=0)
            throw new EngineException("You Must connect first !");

        String [] details =new String [4];
        Member member=Member.search(this.members,this.id);
        details[0]=member.getName();
        details[1]=member.getPhone();
        details[2]=member.getEmail();
        details[3]=member.getPassword();
        return details;
    }

    //Override functions .

    @Override
    protected String[][] getRegistrations(Object[]registration_array) {
        String [][] string_array;
        string_array = new String [registration_array.length][9];
        for(int i=0;i<registration_array.length;i++){
            string_array[i]=((Registration)registration_array[i]).toArray();
        }
        return  string_array;
    }

    @Override
    public String[][] getUnconfirmedRegistrations() {
        Object [] registration_array=this.registrations
                .stream()
                .filter(registration -> !registration.isConfirmed())
                .filter(registration -> registration.getMember().getId()==this.id||registration.isIdFriendExisted(this.id)||this.isManager)
                .toArray();
        return  getRegistrations(registration_array);
    }

    @Override
    public void deleteBoat(int serialNumber) throws EngineException {
        if(!this.isManager)
            throw new EngineException("Access Denied !");
        Boat boat=Boat.search(this.boats,serialNumber);
        if(!isAnyPlacementDependsOnBoat(boat))
            if(!isAnyMemberDependsOnBoat(boat))
                this.boats.remove(boat);
            else
                throw new EngineException("Some member depends on boat !");
        else
            throw new EngineException("Some placement depends on boat !");
    }

    @Override
    public void addPlacement(int registrationId,int boatSerialNumber) throws EngineException {
        if(!this.isManager)
            throw new EngineException("Access Denied !");
        Registration reg=Registration.search(this.registrations,registrationId);
        Boat boat=Boat.search(this.boats,boatSerialNumber);
        Placement placement=new Placement(boat,reg);
        reg.setConfirmed(true);
        this.placements.add(placement);
        notificatePlacementConfirmed(placement);
    }

    @Override
    public void deletePlacement(int registrationId) throws EngineException {
        if(!this.isManager)
            throw new EngineException("Access Denied !");
        Placement placement=Placement.search(this.placements,registrationId);
        int regId=placement.getRegistration().getId();
        Registration.search(this.registrations,regId).setConfirmed(false);
        this.placements.remove(placement);
        notificatePlacementCancelled(placement);
    }

    @Override
    public void addRegistration(int memberId, int reqDay, int reqMonth, int reqYear, String windowName, Boat.BoatType type) throws EngineException {
        if(!(this.isManager||this.id==memberId))
            throw new EngineException("Access Denied !");
        else{
            Member member=Member.search(this.members,memberId);
            Window window=Window.search(this.windows,windowName);
            Registration registration=new Registration(member,reqDay,reqMonth,reqYear,window,type);

            if(this.registrations.contains(registration))
                throw new EngineException("Registration is already existed !");

            if(this.boats.stream().allMatch(boat -> !isBoatMatchedToRegistration(registration,boat)))
                throw new EngineException("Matching boat failure !");

            this.registrations.add(registration);

            if(registration.getMember().isHasPrivateBoat())
                if(isBoatMatchedToRegistration(registration,Boat.search(this.boats,registration.getMember().getPrivateBoatSerialNumber()))){
                    Placement placement=new Placement(Boat.search(this.boats,registration.getMember().getPrivateBoatSerialNumber()),registration);
                    this.placements.add(placement);
                    registration.setConfirmed(true);
                    notificatePlacementConfirmed(placement);
                }
        }
    }

    @Override
    public void addFriend(int registrationId,int friend) throws EngineException {
        super.addFriend(registrationId,friend);
        notificateAdded(friend,registrationId);
    }

    @Override
    public void removeFriend(int registrationId,int friend) throws EngineException {
        super.removeFriend(registrationId,friend);
        notificateRemoved(friend,registrationId);
    }
}
