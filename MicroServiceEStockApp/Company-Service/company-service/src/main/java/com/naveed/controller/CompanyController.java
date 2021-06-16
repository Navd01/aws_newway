package com.naveed.controller;


import java.util.List;


import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.services.dynamodbv2.model.GetItemResult;
import com.naveed.beans.Company;
import com.naveed.repository.CompanyService;
import com.naveed.service.MapValidationErrorService;
import com.naveed.exceptions.CompanyCodeException;
import com.naveed.intercomm.StockClient;

@RestController
@RequestMapping("/company")
public class CompanyController {
	
	@Autowired
	CompanyService companyService;
	
	@Autowired
	MapValidationErrorService mapValidationErrorService;
	
	@Autowired
	StockClient stockClient;

	@PostMapping("/register")
	public ResponseEntity<?> newRegistration(@Valid @RequestBody Company registration , BindingResult result) {
		ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
        if(errorMap!=null) return errorMap;
        Company company = new Company();
		try {
		registration.setCompanyCode(registration.getCompanyCode().toUpperCase());
		 company = companyService.save(registration);
		
		}catch(Exception e) {
			
			
			throw new CompanyCodeException("Project Id Already Exists");
			
		}
		return new ResponseEntity<Company>(company, HttpStatus.CREATED);
		
	}
	
	@GetMapping("/info/{companyCode}")
	public ResponseEntity<?> getCompanyDetails(@PathVariable String companyCode){
		
		Company company = companyService.findByCompanyCode(companyCode);
		
		if(company == null) {
			throw new CompanyCodeException("Company with code " + companyCode + " doesnt exist" );
		}
		
		return new ResponseEntity<Company>(company , HttpStatus.OK);
	}
	
	@GetMapping("/getall")
	public List<Company> getAllCompanies(){
		
		List<Company> companyList =  companyService.getAllCompanies();
		return companyList;
	}
	

	@DeleteMapping("/deleteCompany")
	public void deleteCompany(@RequestBody Company company) {
		
		stockClient.deleteStock(company);
		companyService.deleteCompany(company);
	}
	
	@PostMapping("/update")
	public void updateCompany(Company company) {
		companyService.updateCompany(company);
	}
	
	
		
	
	
	@GetMapping("/companyServiceCheck")
	public String serviceCheck() {
		return "CompanyService is UP";
	}
	
	@GetMapping("/getFromStockCheck")
	public String stockClientCheck() {
		
		String data = stockClient.getFromStock();
		return data;
	}
	
	@GetMapping("/gettingCheckedFromStock")
	public String method() {
		return "Stock requested and Company responded";
	}
		
	
}
