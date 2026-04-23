package ro.mpp2025;

import ro.mpp2025.domain.*;
import ro.mpp2025.repository.AgeGroupRepository;
import ro.mpp2025.repository.EventRepository;
import ro.mpp2025.repository.UserRepository;
import ro.mpp2025.repository.database.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {

        Properties props=new Properties();
        try {
            props.load(new FileReader("db.config"));
        } catch (IOException e) {
            System.out.println("Cannot find db.config "+e);
        }

        AgeGroupRepository ageGroupRepo = new AgeGroupDBRepository(props);
        EventRepository eventRepo = new EventDBRepository(props);
        UserRepository userRepo = new UserDBRepository(props);
        ChildDBRepository childRepo = new ChildDBRepository(props);
        EnrollmentDBRepository enrollmentRepo = new EnrollmentDBRepository(props);

        System.out.println("•All age groups from db: ");
        for(AgeGroup ageGroup:ageGroupRepo.findAll())
            System.out.println(ageGroup);

        System.out.println("•All events from db: ");
        for(Event event:eventRepo.findAll())
            System.out.println(event);

        System.out.println("•All users from db: ");
        for(User user:userRepo.findAll())
            System.out.println(user);

        System.out.println("•All children from db: ");
        for(Child child:childRepo.findAll())
            System.out.println(child);

        System.out.println("•All enrollments from db: ");
        for(Enrollment enrollment:enrollmentRepo.findAll())
            System.out.println(enrollment);
    }
}
