package com.example.employee.service;
import com.example.employee.model.*;
import com.example.employee.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.temporal.ChronoUnit;
import java.util.List;
@Service @RequiredArgsConstructor
public class LeaveService {
    private final LeaveRepository leaveRepo;
    private final EmployeeRepository empRepo;
    public LeaveRequest applyLeave(LeaveRequest req){
        Employee emp=empRepo.findById(req.getEmployeeId())
                .orElseThrow(()->new RuntimeException("Employee not found"));
        long days=ChronoUnit.DAYS.between(req.getStartDate(),req.getEndDate())+1;
        req.setTotalDays((int)days);
        req.setEmployeeName(emp.getFirstName()+" "+emp.getLastName());
        return leaveRepo.save(req);
    }
    public LeaveRequest approveLeave(String id,String approvedBy){
        LeaveRequest req=leaveRepo.findById(id)
                .orElseThrow(()->new RuntimeException("Leave not found"));
        req.setStatus("APPROVED");
        req.setApprovedBy(approvedBy);
        return leaveRepo.save(req);
    }
    public LeaveRequest rejectLeave(String id,String reason){
        LeaveRequest req=leaveRepo.findById(id)
                .orElseThrow(()->new RuntimeException("Leave not found"));
        req.setStatus("REJECTED");
        req.setRejectionReason(reason);
        return leaveRepo.save(req);
    }
    public List<LeaveRequest> getByEmployee(String empId){return leaveRepo.findByEmployeeId(empId);}
    public List<LeaveRequest> getPending(){return leaveRepo.findByStatus("PENDING");}
    public List<LeaveRequest> getAll(){return leaveRepo.findAll();}
}
