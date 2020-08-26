package jp.co.axa.apidemo.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.error.EmployeeNotFoundException;
import jp.co.axa.apidemo.services.EmployeeService;

@RunWith(SpringRunner.class)
@WebMvcTest(value = EmployeeController.class)
public class EmployeeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    private static final Employee mockEmployee = new Employee();

    {
        mockEmployee.setDepartment("mockDept");
        mockEmployee.setName("mockName");
        mockEmployee.setSalary(100);
        mockEmployee.setId(1L);
    }

    /**
     * Positive test case - return list of all employees
     * 
     * @throws Exception
     */
    @Test
    public void getEmployees() throws Exception {
        Mockito.when(employeeService.retrieveEmployees()).thenReturn(Arrays.asList(mockEmployee));
        RequestBuilder rBuilder = MockMvcRequestBuilders.get("/api/v1/employees");
        MvcResult result = mockMvc.perform(rBuilder).andReturn();
        String expectedStr = "[{id:1,name:mockName,department:mockDept,salary:100}]";
        String actualStr = result.getResponse().getContentAsString();
        JSONAssert.assertEquals(expectedStr, actualStr, false);
        Assert.assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
    }

    /**
     * Positive test case - return single employee as per employeeId passed
     * 
     * @throws Exception
     */
    @Test
    public void getEmployee() throws Exception {
        Mockito.when(employeeService.getEmployee(anyLong())).thenReturn(mockEmployee);
        RequestBuilder rBuilder = MockMvcRequestBuilders.get("/api/v1/employees/1");
        MvcResult result = mockMvc.perform(rBuilder).andReturn();
        String expectedStr = "{id:1,name:mockName,department:mockDept,salary:100}";
        String actualStr = result.getResponse().getContentAsString();
        JSONAssert.assertEquals(expectedStr, actualStr, false);
        Assert.assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
    }

    /**
     * Negative test case - throws employee not found exception
     * 
     * @throws Exception
     */
    @Test
    public void getEmployee_employeeNotFound() throws Exception {
        Mockito.when(employeeService.getEmployee(anyLong()))
                .thenThrow(new EmployeeNotFoundException(11L));
        RequestBuilder rBuilder = MockMvcRequestBuilders.get("/api/v1/employees/11");
        MvcResult result = mockMvc.perform(rBuilder).andReturn();
        String expectedStr = "{\"errorMessage\":\"Could not find employee with id: 11\",\"status\":\"NOT_FOUND\"}";
        String actualStr = result.getResponse().getContentAsString();
        JSONAssert.assertEquals(expectedStr, actualStr, false);
        Assert.assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
    }

    /**
     * Positive test case - save employee details
     * 
     * @throws Exception
     */
    @Test
    public void saveEmployee() throws Exception {
        Mockito.when(employeeService.saveEmployee(mockEmployee)).thenReturn(mockEmployee);
        RequestBuilder rBuilder = MockMvcRequestBuilders.post("/api/v1/employees").accept(MediaType.APPLICATION_JSON)
                .content("{\"id\":1,\"name\":\"mockName\",\"salary\":100,\"department\":\"mockDept\"}")
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(rBuilder).andReturn();
        String expectedStr = "Employee with id: 1 created successfully!";
        String actualStr = result.getResponse().getContentAsString();
        Assert.assertEquals(expectedStr, actualStr);
        Assert.assertEquals(HttpStatus.CREATED.value(), result.getResponse().getStatus());
    }

    /**
     * Positive test case - delete employee details
     * 
     * @throws Exception
     */
    @Test
    public void deleteEmployee() throws Exception {
        doNothing().when(employeeService).deleteEmployee(anyLong());
        RequestBuilder rBuilder = MockMvcRequestBuilders.delete("/api/v1/employees/1");
        MvcResult result = mockMvc.perform(rBuilder).andReturn();
        String expectedStr = "Employee with id: 1 deleted successfully!";
        String actualStr = result.getResponse().getContentAsString();
        Assert.assertEquals(expectedStr, actualStr);
        Assert.assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
    }

    /**
     * Negative test case - unable to delete employee details as employee not found
     * 
     * @throws Exception
     */
    @Test
    public void deleteEmployee_employeeNotFound() throws Exception {
        doThrow(new EmployeeNotFoundException(11L)).when(employeeService).deleteEmployee(11L);
        RequestBuilder rBuilder = MockMvcRequestBuilders.delete("/api/v1/employees/11");
        MvcResult result = mockMvc.perform(rBuilder).andReturn();
        String expectedStr = "{\"errorMessage\":\"Could not find employee with id: 11\",\"status\":\"NOT_FOUND\"}";
        String actualStr = result.getResponse().getContentAsString();
        JSONAssert.assertEquals(expectedStr, actualStr, false);
        Assert.assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
    }

    /**
     * Positive test case - update employee details
     * 
     * @throws Exception
     */
    @Test
    public void updateEmployee() throws Exception {
        doNothing().when(employeeService).updateEmployee(anyLong(), any());
        RequestBuilder rBuilder = MockMvcRequestBuilders.put("/api/v1/employees/1")
                .content("{\"name\":\"mockUpdateName\",\"department\":\"mockUpdateDept\",\"salary\":1000}")
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(rBuilder).andReturn();
        String expectedStr = "Employee with id: 1 updated successfully!";
        String actualStr = result.getResponse().getContentAsString();
        Assert.assertEquals(expectedStr, actualStr);
        Assert.assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
    }

    /**
     * Negative test case - cannot update employee details as employee not found
     * 
     * @throws Exception
     */
    @Test
    public void updateEmployee_employeeNotFound() throws Exception {
        doThrow(new EmployeeNotFoundException(11L)).when(employeeService).updateEmployee(anyLong(),
                any());
        RequestBuilder rBuilder = MockMvcRequestBuilders.put("/api/v1/employees/11")
                .content("{\"name\":\"mockUpdateName\",\"department\":\"mockUpdateDept\",\"salary\":1000}")
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(rBuilder).andReturn();
        String expectedStr = "{\"errorMessage\":\"Could not find employee with id: 11\",\"status\":\"NOT_FOUND\"}";
        String actualStr = result.getResponse().getContentAsString();
        JSONAssert.assertEquals(expectedStr, actualStr, false);
        Assert.assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
    }

    /**
     * Negative test case - Incorrect JSON passed to put request (missing closing
     * quotes " for 'name')
     * 
     * @throws Exception
     */
    @Test
    public void updateEmployee_incorrectJSON() throws Exception {
        doNothing().when(employeeService).updateEmployee(anyLong(), any());
        RequestBuilder rBuilder = MockMvcRequestBuilders.put("/api/v1/employees/11")
                .content("{\"name:\"mockUpdateName\",\"department\":\"mockUpdateDept\",\"salary\":1000}")
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(rBuilder).andReturn();
        Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
    }

    /**
     * Negative test case - Incorrect parameter passed to put request (salary should
     * be greater than 0)
     * 
     * @throws Exception
     */
    @Test
    public void updateEmployee_invalidParamSalary() throws Exception {
        doNothing().when(employeeService).updateEmployee(anyLong(), any());
        RequestBuilder rBuilder = MockMvcRequestBuilders.put("/api/v1/employees/11")
                .content("{\"name\":\"mockName\",\"salary\":0,\"department\":\"mockDept\"}")
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(rBuilder).andReturn();
        Assert.assertEquals(HttpStatus.NOT_ACCEPTABLE.value(), result.getResponse().getStatus());
    }

    /**
     * Negative test case - Incorrect Media type passed to put request (only
     * application/JSON is allowed)
     * 
     * @throws Exception
     */
    @Test
    public void updateEmployee_incorrectMediaType() throws Exception {
        doNothing().when(employeeService).updateEmployee(anyLong(), any());
        RequestBuilder rBuilder = MockMvcRequestBuilders.put("/api/v1/employees/11")
                .content("{\"name\":\"mockName\",\"salary\":1000,\"department\":\"mockDept\"}")
                .contentType(MediaType.TEXT_PLAIN);
        MvcResult result = mockMvc.perform(rBuilder).andReturn();
        Assert.assertEquals(HttpStatus.NOT_ACCEPTABLE.value(), result.getResponse().getStatus());
    }
}