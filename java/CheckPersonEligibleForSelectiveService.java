package lambda;

/**
 * @author: ashfak
 * @Date: 7/28/17
 */
public class CheckPersonEligibleForSelectiveService implements CheckPerson {
    public boolean test(Person p) {
        return p.gender == Person.Sex.MALE &&
                p.getAge() >= 18 &&
                p.getAge() <= 25;
    }
}
