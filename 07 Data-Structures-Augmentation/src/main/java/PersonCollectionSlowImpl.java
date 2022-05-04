import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PersonCollectionSlowImpl implements PersonCollection {
    private List<Person> people;

    public PersonCollectionSlowImpl() {
        this.people = new ArrayList<>();
    }

    @Override
    public boolean add(String email, String name, int age, String town) {
        Person person = new Person(email, name, age, town);
        if (find(email) != null) return false;
        this.people.add(person);
        return true;
    }

    @Override
    public int getCount() {
        return this.people.size();
    }

    @Override
    public boolean delete(String email) {
        return this.people.removeIf(p -> p.getEmail().equals(email));
    }

    @Override
    public Person find(String email) {
        return people
                .stream()
                .filter(p -> p.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Iterable<Person> findAll(String emailDomain) {
        return filter(p -> p.getEmail().endsWith("@" + emailDomain), Comparator.comparing(Person::getEmail));
    }

    @Override
    public Iterable<Person> findAll(String name, String town) {
        return filter(p -> p.getName().endsWith(name) && p.getTown().equals(town), Comparator.comparing(Person::getEmail));
    }

    @Override
    public Iterable<Person> findAll(int startAge, int endAge) {
        return filter(p -> startAge <= p.getAge() && p.getAge() <= endAge,
                Comparator.comparing(Person::getAge).thenComparing(Person::getEmail));
    }

    @Override
    public Iterable<Person> findAll(int startAge, int endAge, String town) {
        return filter(p -> p.getTown().equals(town)
                        && startAge <= p.getAge()
                        && p.getAge() <= endAge,
                Comparator.comparing(Person::getAge).thenComparing(Person::getEmail));
    }

    private List<Person> filter(Predicate<Person> predicate, Comparator<Person> comparator) {
        return this.people
                .stream()
                .filter(predicate)
                .sorted(comparator)
                .collect(Collectors.toList());
    }
}
