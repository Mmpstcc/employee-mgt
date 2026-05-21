package com.example.employee;

import com.example.employee.controller.EmployeeController;
import com.example.employee.model.Employee;
import com.example.employee.service.EmployeeService;
import com.example.employee.service.ExportService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import java.util.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.example.employee.security.JwtUtil;
import com.example.employee.security.CustomUserDetailsService;

@WebMvcTest(EmployeeController.class)
@AutoConfigureMockMvc(addFilters = false)
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService service;

    @MockBean
    private ExportService exportService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

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
    @WithMockUser
    void getAllEmployees_ShouldReturn200() throws Exception {
        when(service.getAllEmployees()).thenReturn(Arrays.asList(sampleEmployee));
        mockMvc.perform(get("/api/employees"))
               .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void getEmployeeById_ShouldReturn200() throws Exception {
        when(service.getEmployeeById("emp001")).thenReturn(sampleEmployee);

        mockMvc.perform(get("/api/employees/emp001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Rahul"));
    }

    @Test
    @WithMockUser
    void createEmployee_ShouldReturn201() throws Exception {
        when(service.createEmployee(any(Employee.class))).thenReturn(sampleEmployee);

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleEmployee)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser
    void deleteEmployee_ShouldReturn200() throws Exception {
        mockMvc.perform(delete("/api/employees/emp001"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void getStats_ShouldReturn200() throws Exception {
        when(service.getStats()).thenReturn(
            Map.of("total", 10L, "active", 8L, "inactive", 2L)
        );

        mockMvc.perform(get("/api/employees/stats"))
                .andExpect(status().isOk());
    }
}
