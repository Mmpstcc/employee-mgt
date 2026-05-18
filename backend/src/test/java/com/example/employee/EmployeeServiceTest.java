package com.example.employee;

import com.example.employee.model.Employee;
import com.example.employee.repository.EmployeeRepository;
import com.example.employee.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository repository;

    @InjectMocks
    private EmployeeService service;

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
    void getAllEmployees_ShouldReturnList() {
        when(repository.findAll()).thenReturn(Arrays.asList(sampleEmployee));

        List<Employee> result = service.getAllEmployees();

        assertEquals(1, result.size());
        assertEquals("Rahul", result.get(0).getFirstName());
        verify(repository, times(1)).findAll();
    }

    @Test
    void getEmployeeById_ShouldReturnEmployee() {
        when(repository.findById("emp001")).thenReturn(Optional.of(sampleEmployee));

        Employee result = service.getEmployeeById("emp001");

        assertNotNull(result);
        assertEquals("rahul@company.com", result.getEmail());
    }

    @Test
    void getEmployeeById_WhenNotFound_ShouldThrow() {
        when(repository.findById("invalid")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.getEmployeeById("invalid"));
    }

    @Test
    void createEmployee_ShouldSaveAndReturn() {
        when(repository.findByEmail(sampleEmployee.getEmail())).thenReturn(Optional.empty());
        when(repository.save(sampleEmployee)).thenReturn(sampleEmployee);

        Employee result = service.createEmployee(sampleEmployee);

        assertNotNull(result);
        assertEquals("Rahul", result.getFirstName());
        verify(repository, times(1)).save(sampleEmployee);
    }

    @Test
    void createEmployee_DuplicateEmail_ShouldThrow() {
        when(repository.findByEmail(sampleEmployee.getEmail()))
                .thenReturn(Optional.of(sampleEmployee));

        assertThrows(RuntimeException.class, () -> service.createEmployee(sampleEmployee));
        verify(repository, never()).save(any());
    }

    @Test
    void deleteEmployee_ShouldDelete() {
        when(repository.findById("emp001")).thenReturn(Optional.of(sampleEmployee));
        doNothing().when(repository).deleteById("emp001");

        service.deleteEmployee("emp001");

        verify(repository, times(1)).deleteById("emp001");
    }

    @Test
    void getStats_ShouldReturnCorrectCounts() {
        when(repository.count()).thenReturn(2L);
        when(repository.findByStatus("ACTIVE")).thenReturn(Arrays.asList(sampleEmployee));
        when(repository.findByStatus("INACTIVE")).thenReturn(Arrays.asList());

        var stats = service.getStats();

        assertEquals(2L, stats.get("total"));
        assertEquals(1, ((List<?>) stats.get("active")).size());
    }
}
