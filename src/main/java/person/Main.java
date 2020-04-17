package person;

import com.github.javafaker.Faker;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class Main {

    private static final Faker faker = new Faker();

    private static Address randomAddress(){

        Address address = new Address();
        address.setCountry(faker.address().country());
        address.setState(faker.address().state());
        address.setCity(faker.address().city());
        address.setStreetAddress(faker.address().streetAddress());
        address.setZip(faker.address().zipCode());

        return address;
    }

    private static Person randomPerson(){

        Person person = new Person();

        person.setName(faker.name().name());

        person.setGender(faker.options().option(Person.Gender.class));

        person.setAddress(randomAddress());

        person.setProfession(faker.company().profession());

        person.setEmail(faker.internet().emailAddress());

        Date date = new Date();
        date = faker.date().birthday();
        java.time.LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        person.setDob(localDate);

        return person;

    }

    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-example");

    private static void createRandomPeople(int num) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            for (int i=0;i<num;i++) {
                em.persist(randomPerson());
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    private static List<Person> getPeople() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT l FROM Person l ORDER BY l.id",Person.class).getResultList();
        } finally {
            em.close();
        }
    }

    public static void main(String[] args) {

        createRandomPeople(10);

        getPeople().forEach(System.out::println);

        emf.close();
    }
}
