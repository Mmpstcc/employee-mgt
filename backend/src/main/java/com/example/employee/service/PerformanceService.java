package com.example.employee.service;
import com.example.employee.model.*;
import com.example.employee.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.*;
@Service @RequiredArgsConstructor
public class PerformanceService {
    private final PerformanceRepository perfRepo;
    private final EmployeeRepository empRepo;
    public Performance createReview(Performance p){
        Employee emp=empRepo.findById(p.getEmployeeId())
                .orElseThrow(()->new RuntimeException("Employee not found"));
        p.setEmployeeName(emp.getFirstName()+" "+emp.getLastName());
        double overall=(p.getTechnicalSkills()+p.getCommunication()+
                p.getTeamwork()+p.getLeadership()+p.getPunctuality())/5.0;
        p.setOverallRating(Math.round(overall*100.0)/100.0);
        p.setStatus("COMPLETED");
        return perfRepo.save(p);
    }
    public List<Performance> getByEmployee(String empId){return perfRepo.findByEmployeeId(empId);}
    public List<Performance> getByYear(int year){return perfRepo.findByYear(year);}
    public List<Performance> getAll(){return perfRepo.findAll();}
    public void delete(String id){perfRepo.deleteById(id);}
    public Map<String,Object> getSummary(String empId){
        List<Performance> reviews=perfRepo.findByEmployeeId(empId);
        OptionalDouble avg=reviews.stream().mapToDouble(Performance::getOverallRating).average();
        Map<String,Object> s=new HashMap<>();
        s.put("totalReviews",reviews.size());
        s.put("averageRating",avg.isPresent()?Math.round(avg.getAsDouble()*100.0)/100.0:0);
        s.put("reviews",reviews);
        return s;
    }
}
