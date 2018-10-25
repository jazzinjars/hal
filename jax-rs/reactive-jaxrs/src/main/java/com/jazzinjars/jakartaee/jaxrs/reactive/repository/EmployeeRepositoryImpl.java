package com.jazzinjars.jakartaee.jaxrs.reactive.repository;

import com.jazzinjars.jakartaee.jaxrs.reactive.exceptions.EmployeeAlreadyExists;
import com.jazzinjars.jakartaee.jaxrs.reactive.exceptions.EmployeeNotFound;
import com.jazzinjars.jakartaee.jaxrs.reactive.model.Employee;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class EmployeeRepositoryImpl implements EmployeeRepository {

    private List<Employee> employeeList;

    public EmployeeRepositoryImpl() {
        employeeList = new ArrayList<>();
        employeeList.add(new Employee(1, "Arya"));
        employeeList.add(new Employee(2, "Sansa"));
        employeeList.add(new Employee(3, "Cersei"));
    }

    @Override
    public List<Employee> getAllEmployees() {
	return employeeList;
    }

    @Override
    public Employee getEmployee(int id) {
        for(Employee employee : employeeList) {
            if(employee.getId() == id)
                return employee;
        }
	throw new EmployeeNotFound();
    }

    @Override
    public void updateEmployee(Employee employee, int id) {
        for (Employee emp : employeeList) {
            if (emp.getId() == id) {
                emp.setId(employee.getId());
                emp.setName(employee.getName());
                return;
            }
        }
        throw new EmployeeNotFound();
    }

    @Override
    public void deleteEmployee(int id) {
        for (Employee emp : employeeList) {
            if (emp.getId() == id) {
                employeeList.remove(emp);
                return;
            }
        }
        throw new EmployeeNotFound();
    }

    @Override
    public void addEmployee(Employee employee) {
        for (Employee emp : employeeList) {
            if (emp.getId() == employee.getId()) {
                throw new EmployeeAlreadyExists();
            }
        }
        employeeList.add(employee);

    }
}
