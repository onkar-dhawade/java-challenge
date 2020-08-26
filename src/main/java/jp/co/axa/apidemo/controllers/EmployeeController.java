package jp.co.axa.apidemo.controllers;

import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.services.EmployeeService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import javax.validation.Valid;

/**
 * Controller class that maps API requests for Employee
 */
@RestController
@Slf4j
@RequestMapping("/api/v1")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * Get all employees
     * 
     * @return ResponseEntity<List<Employee>>
     */
    @GetMapping("/employees")
    public ResponseEntity<List<Employee>> getEmployees() {
        log.info("Inside getEmployees()");
        return ResponseEntity.ok(employeeService.retrieveEmployees());
    }

    /**
     * Get single employee as per employeeId
     * 
     * @param employeeId
     * @return ResponseEntity<Employee>
     */
    @GetMapping("/employees/{employeeId}")
    public ResponseEntity<Employee> getEmployee(@PathVariable(name = "employeeId") long employeeId) {
        log.info("Inside getEmployee()");
        return ResponseEntity.ok(employeeService.getEmployee(employeeId));

    }

    /**
     * Save employee details
     * 
     * @param employee
     * @return ResponseEntity<String>
     */
    @PostMapping("/employees")
    public ResponseEntity<String> saveEmployee(@Valid @RequestBody Employee employee) {
        log.info("Inside saveEmployee()");
        Employee emp = employeeService.saveEmployee(employee);
        return new ResponseEntity<String>("Employee with id: " + emp.getId() + " created successfully!",
                HttpStatus.CREATED);
    }

    /**
     * Delete single employee record as per employeeId
     * 
     * @param employeeId
     * @return ResponseEntity<String>
     */
    @DeleteMapping("/employees/{employeeId}")
    public ResponseEntity<String> deleteEmployee(@PathVariable(name = "employeeId") Long employeeId) {
        log.info("Inside deleteEmployee()");
        employeeService.deleteEmployee(employeeId);
        return ResponseEntity.ok("Employee with id: " + employeeId + " deleted successfully!");
    }

    /**
     * Updates details of employee as per employeeId
     * 
     * @param employee
     * @param employeeId
     * @return ResponseEntity<String>
     */
    @PutMapping("/employees/{employeeId}")
    public ResponseEntity<String> updateEmployee(@Valid @RequestBody Employee employee,
            @PathVariable(name = "employeeId") Long employeeId) {
        log.info("Inside updateEmployee()");
        employeeService.updateEmployee(employeeId, employee);
        return ResponseEntity.ok("Employee with id: " + employeeId + " updated successfully!");
    }
}
