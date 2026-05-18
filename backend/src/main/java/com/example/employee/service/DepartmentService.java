package com.example.employee.service;
import com.example.employee.model.Department;
import com.example.employee.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
@Service @RequiredArgsConstructor
public class DepartmentService {
    private final DepartmentRepository deptRepo;
    private final EmployeeRepository empRepo;
    public List<Department> getAll(){return deptRepo.findAll();}
    public Department getById(String id){
        return deptRepo.findById(id).orElseThrow(()->new RuntimeException("Department not found: "+id));
    }
    public Department create(Department dept){
        if(deptRepo.existsByName(dept.getName()))
            throw new RuntimeException("Department already exists: "+dept.getName());
        dept.setTotalEmployees(empRepo.findByDepartment(dept.getName()).size());
        return deptRepo.save(dept);
    }
    public Department update(String id,Department updated){
        Department d=getById(id);
        d.setName(updated.getName());
        d.setDescription(updated.getDescription());
        d.setManagerId(updated.getManagerId());
        d.setManagerName(updated.getManagerName());
        d.setStatus(updated.getStatus());
        d.setTotalEmployees(empRepo.findByDepartment(d.getName()).size());
        return deptRepo.save(d);
    }
    public void delete(String id){getById(id);deptRepo.deleteById(id);}
    public List<Department> getActive(){return deptRepo.findByStatus("ACTIVE");}
}
