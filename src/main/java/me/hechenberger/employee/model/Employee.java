package me.hechenberger.employee.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

/**
 * Representation for an employee in a company.
 * <p>
 * The employee should have an valid unique email address
 */
@Entity
@ApiModel(description = "All details about an employee")
public class Employee implements Cloneable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @ApiModelProperty(notes = "Unique uuid to identify a user")
  private Long id;
  @Email(message = "Please provide a valid Email")
  @Column(unique = true, nullable = false)
  @ApiModelProperty(notes = "An employee's email address. This is unique in the system")
  private String email;
  @NotNull
  @ApiModelProperty(notes = "Firstname of an Employee")
  private String firstName;
  @NotNull
  @ApiModelProperty(notes = "Lastname of an Employee")
  private String lastName;
  @NotNull
  @ApiModelProperty(notes = "Birthday of an Employee")
  private Date birthday;
  @Column(nullable = true)
  @ApiModelProperty(notes = "List of hobbies (for example, \"soccer\", \"music\", etc)")
  private String[] hobbies;

  public Employee() {
  }

  public Employee(String email, String firstName, String lastName, Date birthday) {
    this(email, firstName, lastName, birthday, new String[]{});
  }


  public Employee(String email, String firstName, String lastName, Date birthday, String[] hobbies) {
    this.email = email;
    this.firstName = firstName;
    this.lastName = lastName;
    this.birthday = birthday;
    this.hobbies = hobbies;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public Date getBirthday() {
    return birthday;
  }

  public void setBirthday(Date birthday) {
    this.birthday = birthday;
  }

  public String[] getHobbies() {
    return hobbies;
  }

  public void setHobbies(String[] hobbies) {
    this.hobbies = hobbies;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Employee employee = (Employee) o;
    return getId() == employee.getId() &&
            Objects.equals(getEmail(), employee.getEmail()) &&
            Objects.equals(getFirstName(), employee.getFirstName()) &&
            Objects.equals(getLastName(), employee.getLastName()) &&
            Objects.equals(getBirthday(), employee.getBirthday()) &&
            Arrays.equals(getHobbies(), employee.getHobbies());
  }

  @Override
  public int hashCode() {
    int result = Objects.hash(getId(), getEmail(), getFirstName(), getLastName(), getBirthday());
    result = 31 * result + Arrays.hashCode(getHobbies());
    return result;
  }

  @Override
  public Object clone() {
    String[] deepHobbyCopy = Arrays.copyOf(hobbies, hobbies.length);
    Employee clone = new Employee(getEmail(), getFirstName(), getLastName(), getBirthday(), deepHobbyCopy);
    return clone;
  }
}


