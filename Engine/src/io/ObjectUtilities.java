package io;

import data.Notification;
import data.Placement;
import data.Registration;
import exception.EngineException;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ObjectUtilities {
    public static final String REGISTRATIONS_PATH="registrations.dat";
    public static final String PLACEMENTS_PATH="placements.dat";
    public static final String NOTIFICATIONS_PATH="notifications.dat";

    public static void writeRegistrations (Set<Registration> regs) {
        try {
            ObjectOutputStream out=new ObjectOutputStream(new FileOutputStream(new File(REGISTRATIONS_PATH)));
            for(Registration reg : regs)
                out.writeObject(reg);
            out.flush();
            out.close();
        }
        catch (IOException e) {
           //throw new EngineException(e.getMessage());
        }
    }

    public static void writePlacements (Set<Placement> placements) {
        try {
            ObjectOutputStream out=new ObjectOutputStream(new FileOutputStream(new File(PLACEMENTS_PATH)));
            for(Placement placement : placements)
                out.writeObject(placement);
            out.flush();
            out.close();
        }
        catch (IOException e) {
            //throw new EngineException(e.getMessage());
        }
    }

    public static Set<Registration> readRegistrations () {
        Set<Registration> regs=new HashSet<>();
        try {
            ObjectInputStream in=new ObjectInputStream(new FileInputStream(new File(REGISTRATIONS_PATH)));
            Registration reg= (Registration) in.readObject();
            while(reg!=null) {
                  regs.add(reg);
                  reg= (Registration) in.readObject();
            }
        }
        catch (IOException | ClassNotFoundException e) {
            return regs;
        }
        return regs;
    }

    public static Set<Placement> readPlacements () {
        Set<Placement> placements=new HashSet<>();
        try {
            ObjectInputStream in=new ObjectInputStream(new FileInputStream(new File(PLACEMENTS_PATH)));
            Placement placement= (Placement) in.readObject();
            while(placement!=null) {
                placements.add(placement);
                placement= (Placement) in.readObject();
            }
        }
        catch (IOException | ClassNotFoundException e) {
            return placements;
        }
        return placements;
    }

    //Additional functions for the website !!!

    public static void writeNotifications (List<Notification> notifications) {
        try {
            ObjectOutputStream out=new ObjectOutputStream(new FileOutputStream(new File(NOTIFICATIONS_PATH)));
            for(Notification notification : notifications)
                out.writeObject(notification);
            out.flush();
            out.close();
        }
        catch (IOException e) {
            //throw new EngineException(e.getMessage());
        }
    }

    public static List<Notification> readNotifications () {
        List<Notification> notifications=new ArrayList<>();
        try {
            ObjectInputStream in=new ObjectInputStream(new FileInputStream(new File(NOTIFICATIONS_PATH)));
            Notification notification= (Notification) in.readObject();
            while(notification!=null) {
                notifications.add(notification);
                notification= (Notification) in.readObject();
            }
        }
        catch (IOException | ClassNotFoundException e) {
            return notifications;
        }
        return notifications;
    }
}
