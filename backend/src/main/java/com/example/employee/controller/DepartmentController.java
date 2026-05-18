package com.example.employee.controller;
import com.example.employee.model.Department;
import com.example.employee.service.DepartmentService;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;
@RestController
@RequestMapping("/api/departments")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Tag(name = "Department Management", description = "APIs for managing departments")
public class DepartmentController {
    private final DepartmentService service;

    @GetMapping
    @Operation(summary = "Get all departments")
    public ResponseEntity<List<Department>> getAll(){
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/active")
    @Operation(summary = "Get active departments")
    public ResponseEntity<List<Department>> getActive(){
        return ResponseEntity.ok(service.getActive());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get department by ID")
    public ResponseEntity<Department> getById(@PathVariable String id){
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    @Operation(summary = "Create new department")
    public ResponseEntity<Department> create(@Valid @RequestBody Department dept){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dept));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update department")
    public ResponseEntity<Department> update(@PathVariable String id,@Valid @RequestBody Department dept){
        return ResponseEntity.ok(service.update(id,dept));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete department")
    public ResponseEntity<Map<String,String>> delete(@PathVariable String id){
        service.delete(id);
        return ResponseEntity.ok(Map.of("message","Department deleted successfully"));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String,String>> handleError(RuntimeException e){
        return ResponseEntity.badRequest().body(Map.of("error",e.getMessage()));
    }
}
