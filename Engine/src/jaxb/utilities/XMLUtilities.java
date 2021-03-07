package jaxb.utilities;

import exception.EngineException;
import data.Boat;
import data.Member;
import data.Window;
import jaxb.generated.*;

import javax.xml.bind.*;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import java.io.File;
import java.util.*;

public class XMLUtilities {

    public static final String MEMBERS_PATH="members.xml";
    public static final String BOATS_PATH="boats.xml";
    public static final String ACTIVITIES_PATH="activities.xml";

    public static Set<Member> loadMembers() {
        JAXBContext jaxbContext;
        String xmlFile = MEMBERS_PATH;
        Set<Member> newMembers=new HashSet<>();

        try
        {
            //Get JAXBContext
            jaxbContext = JAXBContext.newInstance(Members.class);

            //Create Unmarshaller
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            //Unmarshal xml file
            Members members = (Members) jaxbUnmarshaller.unmarshal(new File(xmlFile));

            for(jaxb.generated.Member member : members.getMember()){
               String name=member.getName();
               String password=member.getPassword();
               String phone=member.getPhone();
               int age=member.getAge();
               String email=member.getEmail();
               Date join=member.getJoined().toGregorianCalendar().getTime();
               Date expiry=member.getMembershipExpiration().toGregorianCalendar().getTime();
               int id=Integer.parseInt(member.getId());

               int privateSerialNumber=0;
               boolean isHasPrivateBoat=false;
               if(member.isHasPrivateBoat()!=null){
                   privateSerialNumber=Integer.parseInt(member.getPrivateBoatId());
                   isHasPrivateBoat=member.isHasPrivateBoat();
               }

               boolean isManager=false;
               if(member.isManager()!=null)
                   isManager=member.isManager();

               Member.Level level = null;
               if(member.getLevel()== RowingLevel.ADVANCED)
                   level= Member.Level.EXPERT;
               if(member.getLevel()== RowingLevel.INTERMEDIATE)
                    level= Member.Level.NORMAL;
               if(member.getLevel()== RowingLevel.BEGINNER)
                    level= Member.Level.BEGINNER;

               List<String> notes=new ArrayList<>();
               if(member.getComments()!=null){
                   String [] comments = member.getComments().split(Member.NOTE_DELIMITER);
                   for(String comment : comments){
                       notes.add(comment);
                   }
               }

               newMembers.add(new Member(id,name,age,level,expiry,join,phone,email,password,privateSerialNumber,isManager,isHasPrivateBoat,notes));
            }
        }
        catch (JAXBException e)
        {
            return newMembers;
        }
        return newMembers;
    }

    public static void saveMembers(Set<Member> oldMembers) {
        JAXBContext jaxbContext;
        String xmlFile = MEMBERS_PATH;
        Members members = new Members();

        try
        {
            //Get JAXBContext
            jaxbContext = JAXBContext.newInstance(Members.class);

            //Create Marshaller
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            for(Member member : oldMembers){
                jaxb.generated.Member newMember=new jaxb.generated.Member();

                newMember.setEmail(member.getEmail());
                newMember.setName(member.getName());
                newMember.setAge(member.getAge());
                newMember.setPhone(member.getPhone());
                newMember.setId(""+member.getId());
                newMember.setPassword(member.getPassword());
                newMember.setManager(member.isManger());
                newMember.setHasPrivateBoat(member.isHasPrivateBoat());
                newMember.setPrivateBoatId(""+member.getPrivateBoatSerialNumber());

                GregorianCalendar cal1=new GregorianCalendar();
                cal1.setTime(member.getExpiryDate());
                newMember.setMembershipExpiration(DatatypeFactory.newInstance().newXMLGregorianCalendar(cal1));

                GregorianCalendar cal2=new GregorianCalendar();
                cal2.setTime(member.getJoiningDate());
                newMember.setJoined(DatatypeFactory.newInstance().newXMLGregorianCalendar(cal2));

                if(member.getLevel()== Member.Level.BEGINNER)
                    newMember.setLevel(RowingLevel.BEGINNER);
                if(member.getLevel()== Member.Level.EXPERT)
                    newMember.setLevel(RowingLevel.ADVANCED);
                if(member.getLevel()== Member.Level.NORMAL)
                    newMember.setLevel(RowingLevel.INTERMEDIATE);

                String comments="";
                for(Object note: member.getNotes()){
                    comments+=(String)note;
                    comments+=Member.NOTE_DELIMITER;
                }
                if(!comments.equals(""))
                    newMember.setComments(comments);

                members.getMember().add(newMember);
            }

            //Marshal to xml file
            jaxbMarshaller.marshal(members,new File(xmlFile));
        }
        catch (JAXBException | DatatypeConfigurationException e)
        {
            //throw new EngineException(e.getMessage());
        }
    }

    public static Set<Boat> loadBoats() {
        JAXBContext jaxbContext;
        String xmlFile = BOATS_PATH;
        Set<Boat> newBoats=new HashSet<>();

        try
        {
            //Get JAXBContext
            jaxbContext = JAXBContext.newInstance(Boats.class);

            //Create Unmarshaller
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            //Unmarshal xml file
            Boats boats = (Boats) jaxbUnmarshaller.unmarshal(new File(xmlFile));

            for(jaxb.generated.Boat boat : boats.getBoat()){
                String name=boat.getName();
                int serialNumber=Integer.parseInt(boat.getId());

                boolean isOwned=false;
                boolean isDisabled=false;
                boolean isWide=false;
                boolean isCoxswain=false;
                boolean isSeaBoat=false;
                if(boat.isPrivate()!=null)
                    isOwned=boat.isPrivate();
                if(boat.isOutOfOrder()!=null)
                    isDisabled=boat.isOutOfOrder();
                if(boat.isWide()!=null)
                    isWide=boat.isWide();
                if(boat.isHasCoxswain()!=null)
                    isCoxswain=boat.isHasCoxswain();
                if(boat.isCostal()!=null)
                    isSeaBoat=boat.isCostal();

                Boat.BoatType type=null;
                if(boat.getType()== BoatType.SINGLE)
                    type= Boat.BoatType.SINGLE;
                if(boat.getType()== BoatType.DOUBLE||boat.getType()== BoatType.PAIR)
                    type= Boat.BoatType.DOUBLE;
                if(boat.getType()== BoatType.COXED_DOUBLE||boat.getType()== BoatType.COXED_PAIR)
                    type= Boat.BoatType.DOUBLE_ONE_PADDLE;
                if(boat.getType()== BoatType.QUAD||boat.getType()== BoatType.FOUR)
                    type= Boat.BoatType.QUARTET;
                if(boat.getType()== BoatType.COXED_QUAD||boat.getType()== BoatType.COXED_FOUR)
                    type= Boat.BoatType.QUARTET_ONE_PADDLE;
                if(boat.getType()== BoatType.EIGHT)
                    type= Boat.BoatType.EIGHT;
                if(boat.getType()== BoatType.OCTUPLE)
                    type= Boat.BoatType.EIGHT_ONE_PADDLE;

                newBoats.add(new Boat(serialNumber,name,type,isWide,isSeaBoat,isCoxswain,isDisabled,isOwned));
            }
        }
        catch (JAXBException e)
        {
            return newBoats;
        }
        return newBoats;
    }

    public static void saveBoats(Set<Boat> oldBoats) {
        JAXBContext jaxbContext;
        String xmlFile = BOATS_PATH;
        Boats boats=new Boats();

        try
        {
            //Get JAXBContext
            jaxbContext = JAXBContext.newInstance(Boats.class);

            //Create Marshaller
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            for(Boat boat : oldBoats){
                jaxb.generated.Boat newBoat = new jaxb.generated.Boat();

                newBoat.setCostal(boat.isSeaBoat());
                newBoat.setHasCoxswain(boat.isCoxswain());
                newBoat.setOutOfOrder(boat.isDisabled());
                newBoat.setWide(boat.isWideBoat());
                newBoat.setPrivate(boat.isOwned());
                newBoat.setId(""+boat.getSerialNumber());
                newBoat.setName(boat.getName());

                if(boat.getType()== Boat.BoatType.SINGLE)
                    newBoat.setType(BoatType.SINGLE);
                if(boat.getType()== Boat.BoatType.DOUBLE)
                    newBoat.setType(BoatType.DOUBLE);
                if(boat.getType()== Boat.BoatType.DOUBLE_ONE_PADDLE)
                    newBoat.setType(BoatType.COXED_DOUBLE);
                if(boat.getType()== Boat.BoatType.QUARTET)
                    newBoat.setType(BoatType.QUAD);
                if(boat.getType()== Boat.BoatType.QUARTET_ONE_PADDLE)
                    newBoat.setType(BoatType.COXED_QUAD);
                if(boat.getType()== Boat.BoatType.EIGHT)
                    newBoat.setType(BoatType.EIGHT);
                if(boat.getType()== Boat.BoatType.EIGHT_ONE_PADDLE)
                    newBoat.setType(BoatType.OCTUPLE);

                boats.getBoat().add(newBoat);
            }

            //Marshal to xml file
            jaxbMarshaller.marshal(boats,new File(xmlFile));
        }
        catch (JAXBException e)
        {
            //throw new EngineException(e.getMessage());
        }
    }

    public static Set<Window> loadWindows() {
        JAXBContext jaxbContext;
        String xmlFile = ACTIVITIES_PATH;
        Set<Window> newActivities=new HashSet<>();

        try
        {
            //Get JAXBContext
            jaxbContext = JAXBContext.newInstance(Activities.class);

            //Create Unmarshaller
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            //Unmarshal xml file
            Activities activities = (Activities) jaxbUnmarshaller.unmarshal(new File(xmlFile));

            for(jaxb.generated.Timeframe timeframe : activities.getTimeframe()){
                String name=timeframe.getName();

                Window.Time time=null;
                if((timeframe.getStartTime().charAt(0)=='0'&&timeframe.getStartTime().charAt(1)=='8')||(timeframe.getStartTime().charAt(0)=='0'&&timeframe.getStartTime().charAt(1)=='9'))
                    time= Window.Time.EIGHT_TEN_PM;
                else{
                    if((timeframe.getStartTime().charAt(0)=='1'&&timeframe.getStartTime().charAt(1)=='0')||(timeframe.getStartTime().charAt(0)=='1'&&timeframe.getStartTime().charAt(1)=='1'))
                        time= Window.Time.TEN_TWELVE_PM;
                    else{
                        if((timeframe.getStartTime().charAt(0)=='1'&&timeframe.getStartTime().charAt(1)=='2')||(timeframe.getStartTime().charAt(0)=='1'&&timeframe.getStartTime().charAt(1)=='3'))
                            time= Window.Time.TWELVE_TWO_AM;
                        else{
                            if((timeframe.getStartTime().charAt(0)=='1'&&timeframe.getStartTime().charAt(1)=='4')||(timeframe.getStartTime().charAt(0)=='1'&&timeframe.getStartTime().charAt(1)=='5'))
                                time= Window.Time.TWO_FOUR_AM;
                            else{
                                if((timeframe.getStartTime().charAt(0)=='1'&&timeframe.getStartTime().charAt(1)=='6')||(timeframe.getStartTime().charAt(0)=='1'&&timeframe.getStartTime().charAt(1)=='7'))
                                    time= Window.Time.FOUR_SIX_AM;
                                else{
                                    time= Window.Time.SIX_EIGHT_AM;
                                }
                            }
                        }
                    }
                }

                boolean isSeaBoat=false;
                if(timeframe.getBoatType()!=null){
                    if(timeframe.getBoatType()==BoatType.SINGLE)
                        isSeaBoat=false;
                    else
                        isSeaBoat=true;
                }

                newActivities.add(new Window(name,time,isSeaBoat));
            }
        }
        catch (JAXBException e)
        {
            return newActivities;
        }
        return newActivities;
    }

    public static void saveWindows(Set<Window> oldWindows) {
        JAXBContext jaxbContext;
        String xmlFile = ACTIVITIES_PATH;
        Activities activities=new Activities();

        try
        {
            //Get JAXBContext
            jaxbContext = JAXBContext.newInstance(Activities.class);

            //Create Marshaller
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            for(Window window : oldWindows){
                Timeframe timeframe=new Timeframe();

                timeframe.setName(window.getName());
                timeframe.setStartTime(window.getBegin()+":00");
                timeframe.setEndTime(window.getEnd()+":00");

                if(window.isSeaBoat())
                    timeframe.setBoatType(BoatType.DOUBLE);
                else
                    timeframe.setBoatType(BoatType.SINGLE);

                activities.getTimeframe().add(timeframe);
            }

            //Marshal to xml file
            jaxbMarshaller.marshal(activities,new File(xmlFile));
        }
        catch (JAXBException e)
        {
            //throw new EngineException(e.getMessage());
        }
    }
}
