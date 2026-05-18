package com.example.employee.service;
import com.example.employee.model.*;
import com.example.employee.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
@Service @RequiredArgsConstructor
public class PayrollService {
    private final PayrollRepository payrollRepo;
    private final EmployeeRepository empRepo;
    public Payroll generatePayroll(String employeeId,String month,int year){
        Employee emp=empRepo.findById(employeeId)
                .orElseThrow(()->new RuntimeException("Employee not found"));
        if(payrollRepo.findByEmployeeIdAndMonthAndYear(employeeId,month,year).isPresent())
            throw new RuntimeException("Payroll already generated for "+month+"/"+year);
        double basic=emp.getSalary();
        double hra=basic*0.40;
        double ta=basic*0.10;
        double da=basic*0.05;
        double gross=basic+hra+ta+da;
        double pf=basic*0.12;
        double tax=gross>50000?(gross-50000)*0.10:0;
        double totalDed=pf+tax;
        Payroll p=new Payroll();
        p.setEmployeeId(employeeId);
        p.setEmployeeName(emp.getFirstName()+" "+emp.getLastName());
        p.setDepartment(emp.getDepartment());
        p.setMonth(month); p.setYear(year);
        p.setBasicSalary(basic); p.setHra(hra); p.setTa(ta); p.setDa(da);
        p.setGrossSalary(gross); p.setPf(pf); p.setTax(tax);
        p.setTotalDeductions(totalDed); p.setNetSalary(gross-totalDed);
        return payrollRepo.save(p);
    }
    public List<Payroll> getByEmployee(String id){return payrollRepo.findByEmployeeId(id);}
    public List<Payroll> getByMonthYear(String m,int y){return payrollRepo.findByMonthAndYear(m,y);}
}
