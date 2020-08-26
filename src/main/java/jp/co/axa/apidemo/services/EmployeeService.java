package jp.co.axa.apidemo.services;

import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.error.EmployeeNotFoundException;
import jp.co.axa.apidemo.repositories.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class which offers CRUD operations of Employee
 */

@Slf4j
@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    /**
     * Retrieves all the employees
     * 
     * @return List<Employee>
     */
    public List<Employee> retrieveEmployees() {
        log.info("Inside retrieveEmployees()");
        return employeeRepository.findAll();
    }

    /**
     * Retrieve single employee as per employeeId
     * 
     * @param employeeId
     * @return Employee
     */
    public Employee getEmployee(Long employeeId) {
        log.info("Inside getEmployee()");
        Optional<Employee> employeeOptional = employeeRepository.findById(employeeId);
        return employeeOptional
                .orElseThrow(() -> new EmployeeNotFoundException(employeeId));

    }

    /**
     * Save employee details
     * 
     * @param employee
     * @return Employee
     */
    public Employee saveEmployee(Employee employee) {
        log.info("Inside saveEmployee()");
        return employeeRepository.save(employee);
    }

    /**
     * Delete employee record as per employeeId
     * 
     * @param employeeId
     */
    public void deleteEmployee(Long employeeId) {
        log.info("Inside deleteEmployee()");
        employeeRepository.delete(getEmployee(employeeId));
        log.info("Employee details removed successfully.");
    }

    /**
     * Update employee details
     * 
     * @param employeeId
     * @param employee
     */
    public void updateEmployee(Long employeeId, Employee employee) {
        log.info("Inside updateEmployee()");
        Employee temp = getEmployee(employeeId);
        temp.setName(employee.getName());
        temp.setSalary(employee.getSalary());
        temp.setDepartment(employee.getDepartment());
        employeeRepository.save(temp);
        log.info("Employee details updated successfully.");
    }
}