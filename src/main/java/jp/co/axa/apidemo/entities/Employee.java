package jp.co.axa.apidemo.entities;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Employee entity class
 */

@Entity
@Table(name="EMPLOYEE")
@Data
public class Employee {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name="EMPLOYEE_NAME")
    @NotBlank(message = "Enter employee name")
    @Size(min = 3, max = 40)
    private String name;

    @Column(name="EMPLOYEE_SALARY")
    @Min(1)
    private Integer salary;

    @Column(name="DEPARTMENT")
    @NotBlank(message = "Enter employee department")
    @Size(min = 3, max = 20)
    private String department;

}
