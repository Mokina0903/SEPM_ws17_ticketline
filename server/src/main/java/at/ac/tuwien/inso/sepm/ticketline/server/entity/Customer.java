package at.ac.tuwien.inso.sepm.ticketline.server.entity;


import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDate;


@Entity
@Table(name="customer")
public class Customer {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_customer_id")
    @SequenceGenerator(name = "seq_customer_id", sequenceName = "seq_customer_id")
    private Long id;

    @Column(nullable = false, name = "name")
    @Size(max = 100)
    private String name;

    @Column(nullable = false, name ="surname")
    private String surname;

    @Column(nullable = false, name ="knr")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_customer_knr")
    @SequenceGenerator(initialValue=1000, allocationSize = 23, name = "seq_customer_knr", sequenceName = "seq_customer_knr")
    private Long knr;

    @Column( name = "email")
    @Size(max = 100)
    private String email;

    @Column(nullable = false, name = "birthDate")
    private LocalDate birthDate;

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surName) {
        this.surname = surName;
    }

    public Long getKnr() {
        return knr;
    }

    public void setKnr(Long knr) {
        this.knr = knr;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public static CustomerBuilder builder() {
        return new CustomerBuilder();
    }

    public int compareTo(Customer customer) {
        return this.getId().compareTo(customer.getId());
    }

    public static final class CustomerBuilder {
        private Long id;
        private LocalDate birthDate;
        private String name;
        private String mail;
        private Long knr;
        private String surname;


        private CustomerBuilder() {
        }

        public CustomerBuilder id(Long id){
            this.id = id;
            return this;
        }
        public CustomerBuilder knr (Long knr){
            this.knr= knr;
            return this;
        }

        public CustomerBuilder surname(String name){
            this.surname = name;
            return this;
        }

        public CustomerBuilder name(String name){
            this.name = name;
            return this;
        }
        public CustomerBuilder mail(String mail){
            this.mail = mail;
            return this;
        }
        public CustomerBuilder birthDate(LocalDate date){
            this.birthDate = date;
            return this;
        }

        public Customer build() {

            Customer customer = new Customer();
            customer.setId(id);
            customer.setName(name);
            customer.setSurname(surname);
            customer.setKnr(knr);
            customer.setEmail(mail);
           customer.setBirthDate(birthDate);
            return customer;
        }
    }


}
