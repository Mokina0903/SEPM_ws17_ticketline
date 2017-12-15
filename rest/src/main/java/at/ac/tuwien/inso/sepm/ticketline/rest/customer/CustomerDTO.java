package at.ac.tuwien.inso.sepm.ticketline.rest.customer;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDate;

@ApiModel(value = "CustomerDTO", description = "Data Transfer Object of the customer entity")
public class CustomerDTO {

    @ApiModelProperty(readOnly = true, name = "The automatically generated database id")
    private Long id;

    @ApiModelProperty(readOnly = true, name = "The automatically generated database knr, which is not equal to id")
    private Long knr;

    @ApiModelProperty(required = true, name = "The name of the customer")
    private String name;

    @ApiModelProperty(required = true, name = "The name of the customer")
    private String surname;

    @ApiModelProperty(required = true, name = "The email of the customer")
    private String email;

    @ApiModelProperty(required = true, name = "The birthdate of the customer")
    private LocalDate birthDate;


    public Long getKnr() {
        return knr;
    }

    public void setKnr(Long knr) {
        this.knr = knr;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
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


    public static CustomerDTOBuilder builder() {
        return new CustomerDTOBuilder();
    }

    public static final class CustomerDTOBuilder {
        private Long id;
        private LocalDate birthDate;
        private String name;
        private String mail;
        private Long knr;
        private String surname;


        public CustomerDTOBuilder() {
        }

        public CustomerDTOBuilder id(Long id){
            this.id = id;
            return this;
        }
        public CustomerDTOBuilder knr (Long knr){
            this.knr= knr;
            return this;
        }

        public CustomerDTOBuilder surname(String name){
            this.surname = name;
            return this;
        }
        public CustomerDTOBuilder name(String name){
            this.name = name;
            return this;
        }
        public CustomerDTOBuilder mail(String mail){
            this.mail = mail;
            return this;
        }
        public CustomerDTOBuilder birthDate(LocalDate date){
            this.birthDate = date;
            return this;
        }

        public CustomerDTO build() {

            CustomerDTO customer = new CustomerDTO();
            customer.setId(id);
            customer.setName(name);
            customer.setEmail(mail);
            customer.setSurname(surname);
            customer.setKnr(knr);
            customer.setBirthDate(birthDate);
            return customer;
        }
    }
}
