package lambda;

import java.util.Date;

/**
 * @author: ashfak
 * @Date: 7/28/17
 */
public class Person {

    public enum Sex {
        MALE, FEMALE
    }

    String name;
    Date  birthday;

    public Sex getGender() {
        return gender;
    }

    Sex gender;
    String emailAddress;
    int age;
    public int getAge() {
       return age;
    }

    public void printPerson() {
        System.out.println(this.name);
    }
}