package com.naveed.beans;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Range;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;





@DynamoDBTable(tableName="CompanyTable")
public class Company {
	
	
	@NotBlank(message="Company Code is Required")
	@Size(min=4, max=5, message = "Please use 4 to 5 characters")
	private String companyCode;
	
	@NotBlank(message="Company Name is Required")
	private String companyName;
	
	@NotBlank(message="Company CEO is Required")
	private String companyCEO;
	
	@NotNull(message="Company Turnover is Required")
	@Range(min=1_00_00_000, message="TurnOver should be greater than 10 Crore")
	private String companyTurnover;
	
	@NotBlank(message="Company Website is Required")
	@Pattern(regexp = "^(https?:\\/\\/)?(www.)?([a-zA-Z0-9]+).[a-zA-Z0-9]*.[a-z]{3}.?([a-z]+)?$" , message="This field should be a website. Ex : https://www.google.com")
	private String companyWebsite;
	
	@NotBlank(message="Stock Exchange is Required")
	private String stockExchange;
	
	private String latestStockPrice;
	
	public Company() {
		
	}
	
	public Company(String companyCode, String companyName, String companyCEO, String companyTurnover,
			String companyWebsite, String stockExchange, String latestStockPrice) {
		
		
		this.companyCode = companyCode;
		this.companyName = companyName;
		this.companyCEO = companyCEO;
		this.companyTurnover = companyTurnover;
		this.companyWebsite = companyWebsite;
		this.stockExchange = stockExchange;
		this.latestStockPrice = latestStockPrice;
	}
	
	
	@DynamoDBHashKey(attributeName="companyCode")
	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	@DynamoDBAttribute(attributeName="companyName")
	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	
	@DynamoDBAttribute(attributeName="companyCEO")
	public String getCompanyCEO() {
		return companyCEO;
	}

	public void setCompanyCEO(String companyCEO) {
		this.companyCEO = companyCEO;
	}
	
	
	@DynamoDBAttribute(attributeName="companyTurnover")
	public String getCompanyTurnover() {
		return companyTurnover;
	}

	public void setCompanyTurnover(String companyTurnover) {
		this.companyTurnover = companyTurnover;
	}
	
	@DynamoDBAttribute(attributeName="companyWebsite")
	public String getCompanyWebsite() {
		return companyWebsite;
	}

	public void setCompanyWebsite(String companyWebsite) {
		this.companyWebsite = companyWebsite;
	}
	
	@DynamoDBAttribute(attributeName="stockExchange")
	public String getStockExchange() {
		return stockExchange;
	}

	public void setStockExchange(String stockExchange) {
		this.stockExchange = stockExchange;
	}
	
	@DynamoDBAttribute(attributeName="latestStockPrice")
	public String getLatestStockPrice() {
		return latestStockPrice;
	}

	public void setLatestStockPrice(String latestStockPrice) {
		this.latestStockPrice = latestStockPrice;
	}
	
	
	

}
