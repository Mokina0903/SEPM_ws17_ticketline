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
    private String surName;

    @Column(nullable = false, name ="knr")

    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_customer_knr")
    @SequenceGenerator(initialValue=1000, name = "seq_customer_knr", sequenceName = "seq_customer_knr")
    private Long knr;

    @Column( name = "email")
    @Size(max = 100)
    private String email;

    @Column(nullable = false, name = "birthDate")
    private LocalDate birthDate;

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

    public LocalDate getBrithDate() {
        return birthDate;
    }

    public void setBrithDate(LocalDate brithDate) {
        this.birthDate = brithDate;
    }


    public static CustomerBuilder builder() {
        return new CustomerBuilder();
    }

    public static final class CustomerBuilder {
        private Long id;
        private LocalDate birthDate;
        private String name;
        private String mail;


        private CustomerBuilder() {
        }

        public CustomerBuilder id(Long id){
            this.id = id;
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
            customer.setEmail(mail);
           customer.setBrithDate(birthDate);
            return customer;
        }
    }


}
