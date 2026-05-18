package com.example.employee.service;
import com.example.employee.model.Employee;
import com.example.employee.repository.EmployeeRepository;
import com.opencsv.CSVWriter;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import java.io.*;
import java.util.List;
@Service @RequiredArgsConstructor
public class ExportService {
    private final EmployeeRepository empRepo;
    public byte[] exportToExcel() throws IOException {
        List<Employee> list=empRepo.findAll();
        Workbook wb=new XSSFWorkbook();
        Sheet sheet=wb.createSheet("Employees");
        String[] headers={"ID","First Name","Last Name","Email","Department","Designation","Salary","Status"};
        Row hRow=sheet.createRow(0);
        for(int i=0;i<headers.length;i++) hRow.createCell(i).setCellValue(headers[i]);
        int rowNum=1;
        for(Employee e:list){
            Row row=sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(e.getId());
            row.createCell(1).setCellValue(e.getFirstName());
            row.createCell(2).setCellValue(e.getLastName());
            row.createCell(3).setCellValue(e.getEmail());
            row.createCell(4).setCellValue(e.getDepartment());
            row.createCell(5).setCellValue(e.getDesignation());
            row.createCell(6).setCellValue(e.getSalary());
            row.createCell(7).setCellValue(e.getStatus());
        }
        ByteArrayOutputStream out=new ByteArrayOutputStream();
        wb.write(out); wb.close();
        return out.toByteArray();
    }
    public byte[] exportToCsv() throws IOException {
        List<Employee> list=empRepo.findAll();
        ByteArrayOutputStream out=new ByteArrayOutputStream();
        try(CSVWriter writer=new CSVWriter(new OutputStreamWriter(out))){
            writer.writeNext(new String[]{"ID","First Name","Last Name","Email","Department","Designation","Salary","Status"});
            for(Employee e:list)
                writer.writeNext(new String[]{e.getId(),e.getFirstName(),e.getLastName(),
                    e.getEmail(),e.getDepartment(),e.getDesignation(),
                    String.valueOf(e.getSalary()),e.getStatus()});
        }
        return out.toByteArray();
    }
}
