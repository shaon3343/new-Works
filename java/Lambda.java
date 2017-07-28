package lambda;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author: ashfak
 * @Date: 7/28/17
 */

public class Lambda {
    public static void main(String[] args) {
        List<Person> personList = new ArrayList<>();
        Person p = new Person();
        p.gender = Person.Sex.MALE;
        p.birthday = new Date();
        p.name = "abcd";
        p.age = 22;

        Person p1 = new Person();
        p1.gender = Person.Sex.FEMALE;
        p1.birthday = new Date();
        p1.name = "efgh";
        p1.age = 22;

        personList.add(p);
        personList.add(p1);

        printPersons(personList,new CheckPersonEligibleForSelectiveService());

        printPersons(personList,new CheckPerson() {
            public boolean test(Person p) {
                return p.getGender() == Person.Sex.FEMALE
                        && p.getAge() >= 18
                        && p.getAge() <= 25;
            }
        });
    }

    public static void printPersons(List<Person> roster, CheckPerson tester) {
        for (Person p : roster) {
            if (tester.test(p)) {
                p.printPerson();
            }
        }
    }


}

//(Functional interface ) instanceVar = (paramList) -> { action }