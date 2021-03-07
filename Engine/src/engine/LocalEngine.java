package engine;

import data.*;
import exception.EngineException;
import io.ObjectUtilities;
import jaxb.generated.BoatType;
import jaxb.utilities.XMLUtilities;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class LocalEngine {

    public static final String MASTER_EMAIL="master@mail.com";
    public static final String MASTER_PASSWORD="1234";

    protected Set<Member> members;
    protected Set<Boat> boats;
    protected Set<Registration> registrations;
    protected Set<Window> windows;
    protected Set<Placement> placements;

    protected int id;
    protected boolean isManager;

    public LocalEngine () {
        this.members=XMLUtilities.loadMembers();
        this.boats=XMLUtilities.loadBoats();
        this.registrations= ObjectUtilities.readRegistrations();
        this.placements= ObjectUtilities.readPlacements();
        this.windows=XMLUtilities.loadWindows();
        Registration.calibrateRegsId(this.registrations);

        this.id=0;
        this.isManager=false;

        this.members.add(new Member("Tomer",25, Member.Level.EXPERT,new Date("10/10/2100"),"0501111111", MASTER_EMAIL,MASTER_PASSWORD,true));
    }

    //Engine Functions **********************************************************************************************

    //Connects the user .
    public void connect(String email,String password) throws EngineException {
        disconnect();
        Member member=Member.search(this.members,email,password);
        if(member==null)
            throw new EngineException("Member not found !");
        else{
            this.isManager=member.isManger();
            this.id=member.getId();
        }
    }

    //Disconnects the user .
    public void disconnect(){
        this.id=0;
        this.isManager=false;
    }

    //Returns how much members .
    public int getMembersSize(){
        return this.members.size();
    }

    //Returns how much windows .
    public int getWindowsSize(){
        return this.windows.size();
    }

    //Checks is some placement depends on this member .
    private boolean isAnyPlacementDependsOnMember(Member member){
        return this.placements
                .stream()
                .anyMatch(placement ->
                        placement.getRegistration().getMember().equals(member)||
                                placement.getRegistration().isFriend(member));
    }

    //Checks is some registration depends on this member .
    private boolean isAnyRegistrationDependsOnMember(Member member){
        return this.registrations
                .stream()
                .anyMatch(registration ->
                        registration.getMember().equals(member)||
                                registration.isFriend(member));
    }

    //Checks is some placement depends on this boat .
    protected boolean isAnyPlacementDependsOnBoat(Boat boat){
        return this.placements
                .stream()
                .anyMatch(placement -> placement.getBoat().equals(boat));
    }

    //Checks is some placement depends on this window .
    private boolean isAnyPlacementDependsOnWindow(Window window){
        return this.placements
                .stream()
                .anyMatch(placement -> placement.getRegistration().getWindow().equals(window));
    }

    //Checks is some registration depends on this window .
    private boolean isAnyRegistrationDependsOnWindow(Window window){
        return this.registrations
                .stream()
                .anyMatch(registration -> registration.getWindow().equals(window));
    }

    //Saves engine state .
    public void exit() {
        ObjectUtilities.writePlacements(this.placements);
        ObjectUtilities.writeRegistrations(this.registrations);
        jaxb.utilities.XMLUtilities.saveBoats(this.boats);
        jaxb.utilities.XMLUtilities.saveMembers(this.members);
        jaxb.utilities.XMLUtilities.saveWindows(this.windows);
    }

    //Member Functions **********************************************************************************************

    //Adds new member to system .
    public void addMember(String name, int age, Member.Level level, Date expiryDate, String phone, String email, String password, boolean isManger) throws EngineException {
        if(!this.isManager)
            throw new EngineException("Access Denied !");
        else{
            Member member=new Member(name,age,level,expiryDate,phone,email,password,isManger);
            if(!this.members.add(member))
                throw new EngineException("Member email is already existed !");
        }
    }

    //Edits member .
    public void editMember(int id,String name, int age, Member.Level level, Date expiryDate, String phone, String email, String password,boolean isManger,int privateSerialNumber) throws EngineException {
        if(!this.isManager)
            throw new EngineException("Access Denied !");

        Member member = Member.search(this.members, id);
        if(!isAnyPlacementDependsOnMember(member)) {
            this.members.remove(member);
            member.setPrivateBoatSerialNumber(this.boats, privateSerialNumber);
            member.setExpiryDate(expiryDate);
            member.setAge(age);
            member.setManger(isManger);
            member.setPassword(password);
            member.setPhone(phone);
            member.setName(name);
            member.setLevel(level);
            String oldEmail=member.getEmail();
            member.setEmail(email);
            if(this.members.contains(member)) {
                member.setEmail(oldEmail);
                this.members.add(member);
                throw new EngineException("Member email is already existed !");
            }
            else
                this.members.add(member);
        }
        else
            throw new EngineException("Some placement depends on member's level !");

    }

    //Edits member .
    public void editMember(String name, String phone, String email, String password) throws EngineException {
        if(this.id<=0)
            throw new EngineException("You Must connect first !");

        Member member = Member.search(this.members, this.id);
        if(!isAnyRegistrationDependsOnMember(member)){
            if(!isAnyPlacementDependsOnMember(member)) {
                this.members.remove(member);
                member.setPassword(password);
                member.setPhone(phone);
                member.setName(name);
                String oldEmail=member.getEmail();
                member.setEmail(email);
                if(this.members.contains(member)) {
                    member.setEmail(oldEmail);
                    this.members.add(member);
                    throw new EngineException("Member email is already existed !");
                }
                else
                    this.members.add(member);
            }
        }
        else
            throw new EngineException("Some registration/placement depends on member !");

    }

    //Deletes member .
    public void deleteMember(int id) throws EngineException {
        if(!this.isManager)
            throw new EngineException("Access Denied !");

        Member member=Member.search(this.members,id);
        if(!isAnyRegistrationDependsOnMember(member)){
            if(!isAnyPlacementDependsOnMember(member)) {
                this.members.remove(member);
                member.setPrivateBoatSerialNumber(this.boats, 0);
            }
        }
        else
            throw new EngineException("Some registration/placement depends on member !");

    }

    //Adds note to member .
    public void addNote(int id,String note) throws EngineException {
        if(!this.isManager)
            throw new EngineException("Access Denied !");
        Member member=Member.search(this.members,id);
        member.addNote(note);
    }

    //Removes note from member .
    public void removeNote(int id,int index) throws EngineException {
        if(!this.isManager)
            throw new EngineException("Access Denied !");
        Member member=Member.search(this.members,id);
        member.removeNote(index);
    }

    //Returns all member's notes .
    public String[] getNotes(int id) throws EngineException {
        if(!this.isManager)
            throw new EngineException("Access Denied !");
        else{
            Member member=Member.search(this.members,id);
            Object [] note_array=member.getNotes();
            String [] string_array = new String [note_array.length];
            for(int i=0;i<note_array.length;i++){
                string_array[i]=((String)note_array[i]);
            }
            return  string_array;
        }
    }

    //Edits member's note .
    public void editNote(int id,int index,String note) throws EngineException {
        if(!this.isManager)
            throw new EngineException("Access Denied !");
        Member member=Member.search(this.members,id);
        member.editNote(index,note);
    }

    //Returns all members .
    public String[][] getAllMembers() {
        Object [] member_array;
        String [][] string_array;
        if(!this.isManager) {
            member_array=this.members.toArray();
            string_array = new String [member_array.length][2];
            for(int i=0;i<member_array.length;i++){
                string_array[i]=((Member)member_array[i]).toSecureArray();
            }
        }
        else{
           member_array = this.members.toArray();
            string_array = new String [member_array.length][13];
            for(int i=0;i<member_array.length;i++){
                string_array[i]=((Member)member_array[i]).toArray();
            }
        }
        return  string_array;
    }

    //Boat Functions **********************************************************************************************

    //Adds new boat to system .
    public void addBoat(String name, Boat.BoatType type, boolean isWideBoat, boolean isSeaBoat, boolean isCoxswain, boolean isDisabled) throws EngineException {
        if(!this.isManager)
            throw new EngineException("Access Denied !");
        else{
            Boat boat =new Boat(name,type,isWideBoat,isSeaBoat,isCoxswain,isDisabled);
            if(!this.boats.add(boat))
                throw new EngineException("Boat name is already existed !");
        }
    }

    //Edits boat .
    public void editBoat (int serialNumber,String name, Boat.BoatType type, boolean isWideBoat, boolean isSeaBoat, boolean isCoxswain, boolean isDisabled) throws EngineException {
        if(!this.isManager)
            throw new EngineException("Access Denied !");

        Boat boat=Boat.search(this.boats,serialNumber);
        if(!isAnyPlacementDependsOnBoat(boat)) {
            this.boats.remove(boat);
            boat.setSeaBoat(isSeaBoat);
            boat.setDisabled(isDisabled);
            boat.setWideBoat(isWideBoat);
            boat.setType(type);
            boat.setCoxswain(isCoxswain);
            String oldName=boat.getName();
            boat.setName(name);
            if(this.boats.contains(boat)){
                boat.setName(oldName);
                this.boats.add(boat);
                throw new EngineException("Boat name is already existed !");
            }
            else
                this.boats.add(boat);
        }
        else
            throw new EngineException("Some placement depends on boat !");
    }

    //Deletes boat .
    public void deleteBoat(int serialNumber) throws EngineException {
        if(!this.isManager)
            throw new EngineException("Access Denied !");
        Boat boat=Boat.search(this.boats,serialNumber);
        if(!isAnyPlacementDependsOnBoat(boat))
            this.boats.remove(boat);
        else
            throw new EngineException("Some placement depends on boat !");
    }

    //Returns filtered boats .
    private String[][] getBoats(Object[]boat_array) {
        String [][] string_array;
        if(!this.isManager){
            string_array = new String [boat_array.length][2];
            for(int i=0;i<boat_array.length;i++){
                string_array[i]=((Boat)boat_array[i]).toSecureArray();
            }
        }
        else{
            string_array = new String [boat_array.length][8];
            for(int i=0;i<boat_array.length;i++){
                string_array[i]=((Boat)boat_array[i]).toArray();
            }
        }
        return  string_array;
    }

    //Returns non-owned boats .
    public String[][] getNonOwnedBoats() {
        Object [] boat_array=this.boats
                .stream()
                .filter(boat -> !boat.isOwned())
                .toArray();
        return  getBoats(boat_array);
    }

    //Returns matched boats .
    public String[][] getMatchedBoats(int regId) {
        Registration reg=Registration.search(this.registrations, regId);
        Object [] boat_array=this.boats
                .stream()
                .filter(boat -> isBoatMatchedToRegistration(reg,boat))
                .toArray();
        return  getBoats(boat_array);
    }

    //Returns all boats .
    public String[][] getAllBoats() {
        Object [] boat_array=this.boats
                .toArray();
        return  getBoats(boat_array);
    }

    //Window Functions **********************************************************************************************

    //Adds new window to system .
    public void addWindow(String name, Window.Time time, boolean isSeaBoat) throws EngineException {
        if(!this.isManager)
            throw new EngineException("Access Denied !");
        else{
            Window window=new Window(name,time,isSeaBoat);
            if(!this.windows.add(window))
                throw new EngineException("Window name/time is already existed !");
        }
    }

    //Edits window .
    public void editWindow(String name, Window.Time time, boolean isSeaBoat) throws EngineException {
        if(!this.isManager)
            throw new EngineException("Access Denied !");

        Window window=Window.search(this.windows,name);
        if(!isAnyRegistrationDependsOnWindow(window)){
            if(!isAnyPlacementDependsOnWindow(window)){
                this.windows.remove(window);
                String oldEnd= window.getEnd();
                String oldBegin=window.getBegin();
                window.setSeaBoat(isSeaBoat);
                window.setTime(time);
                if(this.windows.contains(window)){
                    window.setBegin(oldBegin);
                    window.setEnd(oldEnd);
                    this.windows.add(window);
                    throw new EngineException("Window name/time is already existed !");
                }
                else
                    this.windows.add(window);
            }
        }
        else
            throw new EngineException("Some registration/placement depends on window !");
    }

    //Deletes window .
    public void deleteWindow(String name) throws EngineException {
        if(!this.isManager)
            throw new EngineException("Access Denied !");

        Window window=Window.search(this.windows,name);
        if(!isAnyRegistrationDependsOnWindow(window)){
            if(!isAnyPlacementDependsOnWindow(window)) {
                this.windows.remove(window);
            }
        }
        else
            throw new EngineException("Some registration/placement depends on window !");
    }

    //Returns all windows .
    public String[][] getAllWindows() {
        Object [] window_array;
        String [][] string_array;
        if(!this.isManager) {
            window_array=this.windows.toArray();
            string_array = new String [window_array.length][1];
            for(int i=0;i<window_array.length;i++){
                string_array[i]=((Window)window_array[i]).toSecureArray();
            }
        }
        else{
            window_array=this.windows.toArray();
            string_array = new String [window_array.length][4];
            for(int i=0;i<window_array.length;i++){
                string_array[i]=((Window)window_array[i]).toArray();
            }
        }
        return  string_array;
    }

    //Registration Functions **********************************************************************************************

    //Adds new registration to system .
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
                }
        }
    }

    //Edits Registration .
    public void editRegistration (int registrationId, int reqDay, int reqMonth, int reqYear, String windowName) throws EngineException {
        Registration registration =Registration.search(this.registrations,registrationId);
        if(!this.isManager&&registration.getMember().getId()!=this.id&&!registration.isIdFriendExisted(this.id))
            throw new EngineException("Access Denied !");

        this.registrations.remove(registration);
        int oldDay=registration.getReqDay();
        int oldMonth=registration.getReqMonth();
        int oldYear=registration.getReqYear();
        Window oldWindow=registration.getWindow();

        registration.setReqDay(reqDay);
        registration.setReqMonth(reqMonth);
        registration.setReqYear(reqYear);
        Window window=Window.search(this.windows,windowName);
        registration.setWindow(window);

        if(this.registrations.contains(registration)){
            registration.setReqDay(oldDay);
            registration.setReqMonth(oldMonth);
            registration.setReqYear(oldYear);
            registration.setWindow(oldWindow);
            this.registrations.add(registration);
            throw new EngineException("Registration is already existed !");
        }
        else
            this.registrations.add(registration);
    }

    //Deletes Registration .
    public void deleteRegistration (int registrationId) throws EngineException {
        Registration registration =Registration.search(this.registrations,registrationId);
        if(!this.isManager&&registration.getMember().getId()!=this.id&&!registration.isIdFriendExisted(this.id))
            throw new EngineException("Access Denied !");
        this.registrations.remove(registration);
    }

    //Adds friend to registration .
    public void addFriend(int registrationId,int friend) throws EngineException {
        Registration registration =Registration.search(this.registrations,registrationId);
        Member member=Member.search(this.members,friend);
        if(!this.isManager&&registration.getMember().getId()!=this.id&&!registration.isIdFriendExisted(this.id))
            throw new EngineException("Access Denied !");

        registration.addFriend(member);
    }

    //Removes friend from registration .
    public void removeFriend(int registrationId,int friend) throws EngineException {
        Registration registration =Registration.search(this.registrations,registrationId);
        Member member=Member.search(this.members,friend);
        if(!this.isManager&&registration.getMember().getId()!=this.id&&!registration.isIdFriendExisted(this.id))
            throw new EngineException("Access Denied !");

        registration.removeFriend(member);
    }

    //Adds type to registration .
    public void addType(int registrationId, Boat.BoatType type) throws EngineException {
        Registration registration =Registration.search(this.registrations,registrationId);
        if(!this.isManager&&registration.getMember().getId()!=this.id&&!registration.isIdFriendExisted(this.id))
            throw new EngineException("Access Denied !");

        registration.addType(type);
    }

    //Remove type from registration .
    public void removeType(int registrationId, Boat.BoatType type) throws EngineException {
        Registration registration =Registration.search(this.registrations,registrationId);
        if(!this.isManager&&registration.getMember().getId()!=this.id&&!registration.isIdFriendExisted(this.id))
            throw new EngineException("Access Denied !");

        registration.removeType(type);
    }

    //Returns filtered registrations .
    protected String[][] getRegistrations(Object[]registration_array) {
        String [][] string_array;
        if(!this.isManager){
            string_array = new String [registration_array.length][1];
            for(int i=0;i<registration_array.length;i++){
                string_array[i]=((Registration)registration_array[i]).toSecureArray();
            }
        }
        else{
            string_array = new String [registration_array.length][9];
            for(int i=0;i<registration_array.length;i++){
                string_array[i]=((Registration)registration_array[i]).toArray();
            }
        }
        return  string_array;
    }

    //Returns unconfirmed registrations .
    public String[][] getUnconfirmedRegistrations() {
        Object [] registration_array=this.registrations
                .stream()
                .filter(registration -> !registration.isConfirmed())
                .toArray();
        return  getRegistrations(registration_array);
    }

    //Returns all registrations .
    public String[][] getAllRegistrations() {
        Object [] registration_array=this.registrations
                .toArray();
        return  getRegistrations(registration_array);
    }

    //Placement Functions **********************************************************************************************

    //Adds new placement to system .
    public void addPlacement(int registrationId,int boatSerialNumber) throws EngineException {
        if(!this.isManager)
            throw new EngineException("Access Denied !");
        Registration reg=Registration.search(this.registrations,registrationId);
        Boat boat=Boat.search(this.boats,boatSerialNumber);
        Placement placement=new Placement(boat,reg);
        reg.setConfirmed(true);
        this.placements.add(placement);
    }

    //Deletes placement from system .
    public void deletePlacement(int registrationId) throws EngineException {
        if(!this.isManager)
            throw new EngineException("Access Denied !");
        Placement placement=Placement.search(this.placements,registrationId);
        int regId=placement.getRegistration().getId();
        Registration.search(this.registrations,regId).setConfirmed(false);
        this.placements.remove(placement);
    }

    //Returns filtered placements .
    private String[][] getPlacements(Object [] placement_array) throws EngineException {
        if(!this.isManager)
            throw new EngineException("Access Denied !");
        else{
            String [][] string_array = new String [placement_array.length][10];
            for(int i=0;i<placement_array.length;i++){
                string_array[i]=((Placement)placement_array[i]).toArray();
            }
            return  string_array;
        }
    }

    //Returns all placements .
    public String[][] getAllPlacements() throws EngineException {
        Object [] placement_array=this.placements
                .toArray();
        return  getPlacements(placement_array);
}

    //Unique Functions **********************************************************************************************

    //duplicates registration .
    public void duplicateRegistration (int sourceRegId,int reqDay, int reqMonth, int reqYear) throws EngineException {
        if(!this.isManager)
            throw new EngineException("Access Denied !");
        else{
            Registration source=Registration.search(this.registrations,sourceRegId);
            Registration registration=new Registration(source,reqDay,reqMonth,reqYear);

            if(this.registrations.contains(registration))
                throw new EngineException("Duplication failure !");

            if(this.boats.stream().allMatch(boat -> !isBoatMatchedToRegistration(registration,boat)))
                throw new EngineException("Matching boat failure !");

            this.registrations.add(registration);

            if(registration.getMember().isHasPrivateBoat())
                if(isBoatMatchedToRegistration(registration,Boat.search(this.boats,registration.getMember().getPrivateBoatSerialNumber()))){
                    Placement placement=new Placement(Boat.search(this.boats,registration.getMember().getPrivateBoatSerialNumber()),registration);
                    this.placements.add(placement);
                    registration.setConfirmed(true);
                }
        }
    }

    //Units registrations .
    public void unitRegistrations (int sourceRegId,int destinationRegId) throws EngineException {
        if(!this.isManager)
            throw new EngineException("Access Denied !");

        Registration reg1=Registration.search(this.registrations,sourceRegId);
        Registration reg2=Registration.search(this.registrations,destinationRegId);

        String error="Uniting failure !";

        if(!reg1.equals(reg2)){
            if(!reg1.isConfirmed()&&!reg2.isConfirmed()){
                if(reg1.getWindow().equals(reg2.getWindow())){
                    if(reg1.getReqDay()==reg2.getReqDay()){
                        if(reg1.getReqMonth()==reg2.getReqMonth()){
                            if(reg1.getReqYear()==reg2.getReqYear()){
                                Set<Member> unitedFriends =new HashSet<>();
                                Object [] friends1=reg1.getFriends();
                                Object [] friends2=reg2.getFriends();
                                for(Object obj1:friends1)
                                    unitedFriends.add((Member)obj1);
                                for(Object obj2:friends2)
                                    unitedFriends.add((Member)obj2);
                                unitedFriends.add(reg1.getMember());
                                unitedFriends.add(reg2.getMember());
                                if(unitedFriends.size()<=8){
                                    for(Member member:unitedFriends)
                                        reg1.addFriend(member);
                                    this.registrations.remove(reg2);
                                }
                                else{
                                    throw new EngineException(error);
                                }
                            }
                            else{
                                throw new EngineException(error);
                            }
                        }
                        else{
                            throw new EngineException(error);
                        }
                    }
                    else{
                        throw new EngineException(error);
                    }
                }
                else{
                    throw new EngineException(error);
                }
            }
            else{
                throw new EngineException(error);
            }
        }
        else{
            throw new EngineException(error);
        }
    }

    //Checks if boat is matched to registration .
    protected boolean isBoatMatchedToRegistration(Registration reg, Boat boat) {
        for(Placement placement:this.placements){
            if(placement.getBoat().equals(boat))
                if(placement.getRegistration().getWindow().equals(reg.getWindow()))
                    if(placement.getRegistration().getReqDay()==reg.getReqDay())
                        if(placement.getRegistration().getReqMonth()==reg.getReqMonth())
                            if(placement.getRegistration().getReqYear()==reg.getReqYear())
                                return false;
        }

        if(reg.isConfirmed())
            return false;
        if(boat.isDisabled())
            return false;
        if(boat.isOwned()&&(boat.getSerialNumber()!=reg.getMember().getPrivateBoatSerialNumber()))
            return false;
        if(!reg.isTypeExisted(boat.getType()))
            return false;
        if(reg.isAnyLevel(Member.Level.EXPERT)&&boat.isWideBoat())
            return false;
        if(reg.isAnyLevel(Member.Level.BEGINNER)&&!boat.isWideBoat())
            return false;
        if(reg.getWindow().isSeaBoat()!=boat.isSeaBoat())
            return false;
        return true;
    }
}
