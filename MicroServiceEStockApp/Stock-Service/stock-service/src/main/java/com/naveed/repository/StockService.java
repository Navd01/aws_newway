package com.naveed.repository;


import java.util.HashMap;

import org.springframework.stereotype.Service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.naveed.beans.Stock;

@Service
public class StockService  {
	
	private final AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
			.withRegion(Regions.AP_SOUTH_1)
			.build();  
    
	private DynamoDBMapper mapper = new DynamoDBMapper(client);
	
    public Stock save(Stock stock) {
    	mapper.save(stock);
    	
    	return stock;
    }

	public Stock findStockbyCompanyCode(String companyCode) {
		// TODO Auto-generated method stub
		return mapper.load(Stock.class , companyCode);
	}

	public void deleteStock(Stock deleteStock) {
		mapper.delete(deleteStock);
		
	}

}
