package com.example.employee;

import com.example.employee.controller.EmployeeController;
import com.example.employee.model.Employee;
import com.example.employee.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService service;

    @Autowired
    private ObjectMapper objectMapper;

    private Employee sampleEmployee;

    @BeforeEach
    void setUp() {
        sampleEmployee = new Employee();
        sampleEmployee.setId("emp001");
        sampleEmployee.setFirstName("Rahul");
        sampleEmployee.setLastName("Sharma");
        sampleEmployee.setEmail("rahul@company.com");
        sampleEmployee.setDepartment("Engineering");
        sampleEmployee.setDesignation("Engineer");
        sampleEmployee.setSalary(75000.0);
        sampleEmployee.setStatus("ACTIVE");
    }

    @Test
    void getAllEmployees_ShouldReturn200() throws Exception {
        List<Employee> list = Arrays.asList(sampleEmployee);
        when(service.getAllEmployees()).thenReturn(list);

        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Rahul"))
                .andExpect(jsonPath("$[0].email").value("rahul@company.com"));
    }

    @Test
    void getEmployeeById_ShouldReturn200() throws Exception {
        when(service.getEmployeeById("emp001")).thenReturn(sampleEmployee);

        mockMvc.perform(get("/api/employees/emp001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Rahul"));
    }

    @Test
    void createEmployee_ShouldReturn201() throws Exception {
        when(service.createEmployee(any(Employee.class))).thenReturn(sampleEmployee);

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleEmployee)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("emp001"));
    }

    @Test
    void updateEmployee_ShouldReturn200() throws Exception {
        when(service.updateEmployee(eq("emp001"), any(Employee.class))).thenReturn(sampleEmployee);

        mockMvc.perform(put("/api/employees/emp001")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleEmployee)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.department").value("Engineering"));
    }

    @Test
    void deleteEmployee_ShouldReturn200() throws Exception {
        mockMvc.perform(delete("/api/employees/emp001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Employee deleted successfully"));
    }

    @Test
    void getStats_ShouldReturn200() throws Exception {
        when(service.getStats()).thenReturn(
            java.util.Map.of("total", 10L, "active", 8L, "inactive", 2L)
        );

        mockMvc.perform(get("/api/employees/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(10));
    }
}
