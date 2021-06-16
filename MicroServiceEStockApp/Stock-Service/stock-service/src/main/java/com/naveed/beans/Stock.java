package com.naveed.beans;

import java.time.LocalDate;



import javax.validation.constraints.Digits;

import javax.validation.constraints.NotNull;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.fasterxml.jackson.annotation.JsonIgnore;




@DynamoDBTable(tableName="StockTable")
public class Stock {
	
	
	@NotNull(message = "Stock Price Cannot be null")
	@Digits(integer= 9, fraction=9 , message="Stock Price should be a fraction")
	private String stockPrice;
	
	

	@JsonIgnore
	private String companyCode;
	
	
	
	@DynamoDBAttribute(attributeName="stockPrice")
	public String getStockPrice() {
		return stockPrice;
	}


	public void setStockPrice(String stockPrice) {
		this.stockPrice = stockPrice;
	}

	@DynamoDBHashKey(attributeName="companyCode")
	public String getCompanyCode() {
		return companyCode;
	}


	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

}
