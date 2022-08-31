package com.hipsy.spring.oracle.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
/* import org.springframework.web.bind.annotation.DeleteMapping; */
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
/* import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody; */
import org.springframework.web.bind.annotation.RequestMapping;
/* import org.springframework.web.bind.annotation.RequestParam; */
import org.springframework.web.bind.annotation.RestController;

import com.hipsy.spring.oracle.dto.EmployeeDTO;
import com.hipsy.spring.oracle.dto.EmployeeTotalTimeDTO;
import com.hipsy.spring.oracle.dto.EmployesByJobDTO;
import com.hipsy.spring.oracle.dto.EmployesMultiThreadDTO;
import com.hipsy.spring.oracle.model.Employee;
import com.hipsy.spring.oracle.model.EmployeeWorkedHours;
import com.hipsy.spring.oracle.model.Gender;
import com.hipsy.spring.oracle.model.Job;

import com.hipsy.spring.oracle.repository.GenderRepository;
import com.hipsy.spring.oracle.repository.JobRepository;
import com.hipsy.spring.oracle.services.RunnableObject;
import com.hipsy.spring.oracle.repository.EmployeeRepository;
import com.hipsy.spring.oracle.repository.EmployeeWorkedHoursRepository;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class PublicController {

	@Autowired
	GenderRepository gendersRepository;

	@Autowired
	JobRepository jobsRepository;
	
	@Autowired
	EmployeeRepository employeeRepository;

	@Autowired
	EmployeeWorkedHoursRepository employeeWorkedHoursRepository;

	/* Ejercicio 1 */
	@PostMapping("/add-employee")
	public ResponseEntity<Object> createEmployee(@RequestBody EmployeeDTO newEmployee) {
		try {
			/* 
			*   ▪ El nombre y apellido del empleado no existan registrados en base de datos.
			*/

			List<Employee> employeData = employeeRepository.findByNameAndLastName( 
				newEmployee.name,
				newEmployee.last_name );

			if ( employeData.size() > 0) {
				HashMap<String, Object> map = new HashMap<>();
				map.put("id", null);
				map.put("success", false);
				return ResponseEntity.status(HttpStatus.OK).body(map);
			}

			/* 
			 * ▪ El puesto asignado debe existir en la tabla de Jobs.
			 */
			Optional<Job> jobData = jobsRepository.findById( newEmployee.job_id );

			if (!jobData.isPresent()) {
				HashMap<String, Object> map = new HashMap<>();
				map.put("id", null);
				map.put("success", false);
				return ResponseEntity.status(HttpStatus.OK).body(map);
			}

			/* 
			 * ▪ Ser mayor de edad.
			 */
			//Converting String to Date
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date date = formatter.parse( newEmployee.birthdate );
			//Converting obtained Date object to LocalDate object
			Instant instant = date.toInstant();
			ZonedDateTime zone = instant.atZone(ZoneId.systemDefault());
			LocalDate givenDate = zone.toLocalDate();
			//Calculating the difference between given date to current date.
			Period period = Period.between(givenDate, LocalDate.now());
			int yearsOld = period.getYears();

			if( yearsOld < 18 ){

				HashMap<String, Object> map = new HashMap<>();
				map.put("id", null);
				map.put("success", false);
				
				return ResponseEntity.status(HttpStatus.OK).body(map);
			}


			Employee newEmployeeSaved = employeeRepository
				.save(new Employee(
					newEmployee.gender_id,
					newEmployee.job_id,
					newEmployee.name,
					newEmployee.last_name,
					date
				));

			HashMap<String, Object> map = new HashMap<>();
			map.put("id",  newEmployeeSaved.getId());
			map.put("success", true);
			
			return ResponseEntity.status(HttpStatus.OK).body(map);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.OK).body(Map.of(
				"id", null,
				"success", false
			));
		}
	}

	/* Ejercicio 2 */
	@PostMapping("/get-employes-by-job")
	public ResponseEntity<Object> getEmployesByJob(@RequestBody EmployesByJobDTO query ) {
		try {
			/* 
			 * b) Consultar los empleados por puesto
			 */
			List<Employee> employesData = employeeRepository.findAll();
			/* 
			 * c) Filtar los empleados obtenidos en b) con el puesto recibido en a) ocupando expresiones lambda con filtro
			 */
			List<Employee> employesDataFiltered = employesData
				.stream()
				.filter(c -> {
					return c.getJobId() == query.job_id;
				})
				.collect(Collectors.toList());
			/* 
			* d) De los empleados obtenidos en c) ordenarlos por appellido materno
			*/
			employesDataFiltered.sort(Comparator.comparing(employee -> employee.getLastName()));
			
			
			List<Object> allDataFiltered = new ArrayList<Object>();
			
			employesDataFiltered.forEach(employee ->{
				HashMap<String, Object> map = new HashMap<>();
				map.put("id", employee.getId());
				map.put("name", employee.getName());
				map.put("last_name", employee.getLastName());
				map.put("birthdate", employee.getBirthdate());


				Optional<Job> jobData = jobsRepository.findById( employee.getJobId() );
				Optional<Gender> genderData = gendersRepository.findById( employee.getGenderId() );
				
				map.put("job", jobData );
				map.put("gender", genderData );

				allDataFiltered.add( map );
			});
			/* 
			 * e) De los empleados obtenidos en d) agruparlos con expresion lambda por apellido materno
			 */
			
			Map group = employesDataFiltered.stream()
				.collect(Collectors.groupingBy(Employee::getLastName, Collectors.toList()));

			HashMap<String, Object> map = new HashMap<>();
			map.put("success", true);
			map.put("employees", allDataFiltered);
			map.put("groupedEmployees", group);
			
			return ResponseEntity.status(HttpStatus.OK).body(map);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.OK).body(Map.of(
				"employees", null,
				"success", false
			));
		}
	}


	/* Ejercicio 3 */
	/* 
	 * Realiza un web service que permita consultar la información recibida de una lista de empleados con proceso multi-hilos
	 */
	@PostMapping("/get-employes-by-multi-thread")
	public ResponseEntity<Object> getMultiThread(@RequestBody EmployesMultiThreadDTO query ) {
		try {
			ExecutorService executorService = Executors.newFixedThreadPool(5);
			List<Object> employeesDataShared = new ArrayList<Object>();

			for (int i = 0; i < query.employee_id.length; i++) {
				Runnable runnable = new RunnableObject(	
					this.employeeRepository,
					Long.valueOf(query.employee_id[i]),
					employeesDataShared
				);
				
				executorService.execute(runnable);
			}	
			executorService.shutdown();
			
			while (!executorService.isTerminated())	{
				// wait
			}

			HashMap<String, Object> map = new HashMap<>();
			map.put("employees", employeesDataShared);
			map.put("success", true);

			return ResponseEntity.status(HttpStatus.OK).body(map);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.OK).body(Map.of(
				"id", null,
				"success", false
			));
		}
	}

	/* Ejercicio 4 */
	/* 
	 *     Realiza un web service que permita consultar el total de horas trabajadas de un empleado por rango de fechas.
     *       ​ Se debe validar que el empleado exista y que la fecha de inicio sea menor a la fecha de fin.
	 */
	@PostMapping("/get-employee-worked-total-time")
	public ResponseEntity<Object> getEmployeeTotalTime(@RequestBody EmployeeTotalTimeDTO query ) {
		try {

			HashMap<String, Object> map = new HashMap<>();

			/* 
			 * Se debe validar que el empleado exista y que la fecha de inicio sea menor a la fecha de fin.
			 */
			Optional<Employee> userData = employeeRepository.findById( query.employee_id );

			if (!userData.isPresent()) {
				map.put("total_worked_hours", null);
				map.put("success", false);
				return ResponseEntity.status(HttpStatus.OK).body(map);
			}

			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date start_date = formatter.parse( query.start_date );
			Date end_date = formatter.parse( query.end_date );
			
			if ( end_date.getTime() < start_date.getTime() ) {
				map.put("total_worked_hours", null);
				map.put("success", false);
				return ResponseEntity.status(HttpStatus.OK).body(map);
			}

			List<Object> totalRegisters = 
				employeeWorkedHoursRepository.findEmployeeWorkedRegisters(
					query.employee_id,
					start_date,
					end_date
				);
			
			if( totalRegisters.get(0) != null){
				map.put("total_worked_hours", totalRegisters.get(0) );// Total de horas o null en caso de error
			}else{
				map.put("total_worked_hours", 0 );// Total de horas o null en caso de error
			}
			map.put("success", true);// true si se logró obtener los datos, false en caso de error

			return ResponseEntity.status(HttpStatus.OK).body(map);
		} catch (Exception e) {

			System.out.println(e);
			return ResponseEntity.status(HttpStatus.OK).body(Map.of(
				"total_worked_hours", null,
				"success", false
			));
		}
	}

	/* Ejercicio 5 */
	/* 
	 *       Realiza un web service que permita consultar cuanto se le pagó a un empleado en un rango de fechas.
     *      ​ Se debe validar que el empleado exista y que la fecha de inicio sea menor a la fecha de fin.
	 */
	@PostMapping("/get-employee-total-payment-in-time")
	public ResponseEntity<Object> getEmployeeTotalPaymentInTime(@RequestBody EmployeeTotalTimeDTO query ) {
		try {

			HashMap<String, Object> map = new HashMap<>();

			/* 
			 * Se debe validar que el empleado exista y que la fecha de inicio sea menor a la fecha de fin.
			 */
			Optional<Employee> userData = employeeRepository.findById( query.employee_id );

			if (!userData.isPresent()) {
				map.put("payment", null);
				map.put("success", false);
				return ResponseEntity.status(HttpStatus.OK).body(map);
			}

			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date start_date = formatter.parse( query.start_date );
			Date end_date = formatter.parse( query.end_date );
			
			if ( end_date.getTime() < start_date.getTime() ) {
				map.put("payment", null);
				map.put("success", false);
				return ResponseEntity.status(HttpStatus.OK).body(map);
			}

			List<Object> totalRegisters = 
				employeeWorkedHoursRepository.findEmployeeWorkedRegisters(
					query.employee_id,
					start_date,
					end_date
				);
			float payment = 0;

			if( totalRegisters.get(0) != null){
				float totalHours = Float.parseFloat(totalRegisters.get(0).toString() );
				

				Optional<Job> jobData = jobsRepository.findById( userData.get().getJobId() );
				

				if (!jobData.isPresent()) {
					map.put("payment", null);
					map.put("success", false);
					return ResponseEntity.status(HttpStatus.OK).body(map);
				}

				payment = jobData.get().getSalary();

				map.put("payment", (payment * totalHours) );// Cantidad pagada al empleado o null en caso de error
			}else{
				map.put("payment", payment );// Cantidad pagada al empleado o null en caso de error
			}
			map.put("success", true);// true si se logró obtener los datos, false en caso de error

			return ResponseEntity.status(HttpStatus.OK).body(map);
		} catch (Exception e) {

			System.out.println(e);
			return ResponseEntity.status(HttpStatus.OK).body(Map.of(
				"payment", null,
				"success", false
			));
		}
	}

}
