import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PersonCollectionImpl implements PersonCollection {
    private Map<String, Person> peopleByEmail;
    private Map<String, TreeSet<Person>> peopleByEmailDomain;
    private Map<String, TreeSet<Person>> peopleByNameAndTown;

    private TreeMap<Integer, TreeSet<Person>> peopleByAge;
    private Map<String, TreeMap<Integer, TreeSet<Person>>> peopleByTownAndAge;

    public PersonCollectionImpl() {
        this.peopleByEmail = new HashMap<>();
        this.peopleByEmailDomain = new HashMap<>();
        this.peopleByNameAndTown = new HashMap<>();
        this.peopleByAge = new TreeMap<>();
        this.peopleByTownAndAge = new HashMap<>();
    }

    @Override
    public boolean add(String email, String name, int age, String town) {
        if (this.peopleByEmail.containsKey(email)) {
            return false;
        }
        Person person = new Person(email, name, age, town);
        this.peopleByEmail.put(person.getEmail(), person);

        addToIndices(person);
        return true;
    }

    private void addToIndices(Person person) {
        addToPeopleByEmailDomainIndex(person);
        addToPeopleByNameTownIndex(person);
        addToPeopleByAgeIndex(person);
        addToPeopleByTownAndAgeIndex(person);
    }

    private void addToPeopleByAgeIndex(Person person) {
        this.peopleByAge.putIfAbsent(person.getAge(), new TreeSet<>());
        this.peopleByAge.get(person.getAge()).add(person);
    }

    private void addToPeopleByNameTownIndex(Person person) {
        String key = getNameTownKey(person);
        this.peopleByNameAndTown.putIfAbsent(key, new TreeSet<>());
        this.peopleByNameAndTown.get(key).add(person);
    }

    private void addToPeopleByTownAndAgeIndex(Person person) {
        this.peopleByTownAndAge.putIfAbsent(person.getTown(), new TreeMap<>());
        this.peopleByTownAndAge.get(person.getTown()).putIfAbsent(person.getAge(), new TreeSet<>());
        this.peopleByTownAndAge.get(person.getTown()).get(person.getAge()).add(person);
    }

    private void addToPeopleByEmailDomainIndex(Person person) {
        String domain = getDomain(person.getEmail());
        this.peopleByEmailDomain.putIfAbsent(domain, new TreeSet<>());
        this.peopleByEmailDomain.get(domain).add(person);
    }

    private String getDomain(String email) {
        return email.substring(email.indexOf('@') + 1);
    }

    @Override
    public int getCount() {
        return this.peopleByEmail.size();
    }

    @Override
    public boolean delete(String email) {
        Person removedPerson = this.peopleByEmail.remove(email);
        if (removedPerson == null) {
            return false;
        }
        removeIndices(removedPerson);
        return true;
    }

    private void removeIndices(Person person) {
        removePeopleByEmailDomainIndex(person);
        removePeopleByNameAndTownIndex(person);
        removePeopleByAgeIndex(person);
        removePeopleByTownAndAgeIndex(person);
    }

    private void removePeopleByTownAndAgeIndex(Person person) {
        TreeMap<Integer, TreeSet<Person>> town = this.peopleByTownAndAge.get(person.getTown());
        if (town != null) {
            TreeSet<Person> people = town.get(person.getAge());
            if (people != null) {
                people.remove(person);
            }
        }
    }

    private void removePeopleByAgeIndex(Person person) {
        TreeSet<Person> people = this.peopleByAge.get(person.getAge());
        if (people != null) {
            people.remove(person);
        }
    }

    private void removePeopleByNameAndTownIndex(Person person) {
        this.peopleByNameAndTown.remove(getNameTownKey(person));
    }

    private void removePeopleByEmailDomainIndex(Person person) {
        TreeSet<Person> people = this.peopleByEmailDomain.get(getDomain(person.getEmail()));
        if (people != null) {
            people.remove(person);
        }
    }

    @Override
    public Person find(String email) {
        return this.peopleByEmail.get(email);
    }

    @Override
    public Iterable<Person> findAll(String emailDomain) {
        return getPeopleOrEmptyCollection(this.peopleByEmailDomain.get(emailDomain));
    }

    @Override
    public Iterable<Person> findAll(String name, String town) {
        return getPeopleOrEmptyCollection(this.peopleByNameAndTown.get(getNameTownKey(name, town)));
    }

    @Override
    public Iterable<Person> findAll(int startAge, int endAge) {
        ArrayList<Person> people = new ArrayList<>();
        this.peopleByAge
                .subMap(startAge, true, endAge, true)
                .entrySet()
                .forEach(pt -> people.addAll(pt.getValue()));
        return people;
    }

    @Override
    public Iterable<Person> findAll(int startAge, int endAge, String town) {
        ArrayList<Person> people = new ArrayList<>();
        TreeMap<Integer, TreeSet<Person>> towns = this.peopleByTownAndAge.get(town);
        if (towns != null) {
            towns.subMap(startAge, true, endAge, true)
                    .entrySet()
                    .forEach(age -> people.addAll(age.getValue()));
        }

        return people;
    }

    private Iterable<Person> getPeopleOrEmptyCollection(TreeSet<Person> people) {
        return people == null ? new ArrayList<>() : people;
    }

    private String getNameTownKey(Person person) {
        return getNameTownKey(person.getName(), person.getTown());
    }

    private String getNameTownKey(String name, String town) {
        return name + "\4" + town;
    }
}
