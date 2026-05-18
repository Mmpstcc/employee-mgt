package com.example.employee.service;
import com.example.employee.model.*;
import com.example.employee.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.*;
import java.util.*;
@Service @RequiredArgsConstructor
public class AttendanceService {
    private final AttendanceRepository attendanceRepo;
    private final EmployeeRepository empRepo;
    public Attendance checkIn(String employeeId){
        Employee emp=empRepo.findById(employeeId)
                .orElseThrow(()->new RuntimeException("Employee not found"));
        LocalDate today=LocalDate.now();
        if(attendanceRepo.findByEmployeeIdAndDate(employeeId,today).isPresent())
            throw new RuntimeException("Already checked in today");
        Attendance a=new Attendance();
        a.setEmployeeId(employeeId);
        a.setEmployeeName(emp.getFirstName()+" "+emp.getLastName());
        a.setDate(today); a.setCheckIn(LocalTime.now());
        a.setStatus(LocalTime.now().isAfter(LocalTime.of(9,0))?"LATE":"PRESENT");
        return attendanceRepo.save(a);
    }
    public Attendance checkOut(String employeeId){
        Attendance a=attendanceRepo.findByEmployeeIdAndDate(employeeId,LocalDate.now())
                .orElseThrow(()->new RuntimeException("No check-in found for today"));
        a.setCheckOut(LocalTime.now());
        double hours=Duration.between(a.getCheckIn(),a.getCheckOut()).toMinutes()/60.0;
        a.setHoursWorked(Math.round(hours*100.0)/100.0);
        if(hours<4) a.setStatus("HALF_DAY");
        return attendanceRepo.save(a);
    }
    public List<Attendance> getByEmployee(String empId){return attendanceRepo.findByEmployeeId(empId);}
    public List<Attendance> getByDate(LocalDate date){return attendanceRepo.findByDate(date);}
    public Map<String,Object> getMonthlyReport(String empId,int month,int year){
        LocalDate start=LocalDate.of(year,month,1);
        LocalDate end=start.withDayOfMonth(start.lengthOfMonth());
        List<Attendance> list=attendanceRepo.findByEmployeeIdAndDateBetween(empId,start,end);
        Map<String,Object> report=new HashMap<>();
        report.put("present",list.stream().filter(a->a.getStatus().equals("PRESENT")).count());
        report.put("absent",list.stream().filter(a->a.getStatus().equals("ABSENT")).count());
        report.put("late",list.stream().filter(a->a.getStatus().equals("LATE")).count());
        report.put("totalHours",list.stream().mapToDouble(Attendance::getHoursWorked).sum());
        report.put("records",list);
        return report;
    }
}
