package com.naveed.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
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

import com.naveed.beans.Company;
//import com.naveed.beans.Company;
import com.naveed.beans.FinalStockList;
import com.naveed.beans.Stock;
import com.naveed.exceptions.StockException;
import com.naveed.intercomm.CompanyClient;
//import com.naveed.repository.CompanyRepository;
import com.naveed.repository.StockService;
import com.naveed.service.MapValidationErrorService;

import feign.FeignException;


@RestController
@RequestMapping("/stock")
public class StockController {
	
	@Autowired
	MapValidationErrorService mapValidationErrorService;
	
	@Autowired
	StockService stockService;
	
	@Autowired
	CompanyClient companyClient;
	
//	@Autowired
//	CompanyRepository companyRepo;
	
	@PostMapping("/add/{companyCode}")
	public ResponseEntity<?> addStock(@Valid @RequestBody Stock stock, @PathVariable String companyCode, BindingResult result){
		
		ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
        if(errorMap!=null) return errorMap;
		
		Company company = companyClient.getCompany(companyCode);
		Stock receivedStock;
		if(company == null) {
			throw new StockException("Company with code " + companyCode + " doesnt exist" );
		}else {
			
			stock.setCompanyCode(companyCode);
			company.setLatestStockPrice(stock.getStockPrice());
			
			receivedStock = stockService.save(stock);
			try {
				Company updatedCompany = companyClient.postLatestStock(company);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return new ResponseEntity<Stock>(receivedStock, HttpStatus.CREATED);
			}
			
		}
		return new ResponseEntity<Stock>(receivedStock, HttpStatus.CREATED);
	}
	
	
	@DeleteMapping("/deleteStock")
	public void deleteStock(@RequestBody Company company){
		
		Stock deleteStock = stockService.findStockbyCompanyCode(company.getCompanyCode());
		
		stockService.deleteStock(deleteStock);
		
		
		
	}
	
	
	

	
	@GetMapping("/stockServiceCheck")
	public String serviceCheck() {
		return "StockService is UP";
	}
	
	@GetMapping("/getFromCompanyCheck")
	public String stockClientCheck() {
		
		String data = companyClient.getFromCompany();
		return data;
	}
	
	@GetMapping("/gettingCheckedFromCompany")
	public String method() {
		return "You are getting this data from Stock Controller on requesting from Company Controller";
	}

	

}
