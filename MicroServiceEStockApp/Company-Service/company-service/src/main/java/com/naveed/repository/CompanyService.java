package com.naveed.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.amazonaws.services.dynamodbv2.model.GetItemResult;
import com.amazonaws.services.dynamodbv2.model.UpdateItemRequest;
import com.naveed.beans.Company;


@Service
public class CompanyService{

	private final AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
			.withRegion(Regions.AP_SOUTH_1)
			.build();  
    private DynamoDBMapper mapper = new DynamoDBMapper(client);
	
    public Company save(Company company) {
//    	HashMap<String, AttributeValue> attribValue = new HashMap<String, AttributeValue>();
//    	attribValue.put("companyCode", new AttributeValue(company.getCompanyCode()));
//    	attribValue.put("companyName", new AttributeValue(company.getCompanyName()));
//    	attribValue.put("companyCEO", new AttributeValue(company.getCompanyCEO()));
//    	attribValue.put("companyTurnover", new AttributeValue(company.getCompanyTurnover()));
//    	attribValue.put("companyWebsite", new AttributeValue(company.getCompanyWebsite()));
//    	attribValue.put("stockExchange", new AttributeValue(company.getStockExchange()));
//    	attribValue.put("latestStockPrice", new AttributeValue(company.getLatestStockPrice()));
//  
//    	client.putItem("CompanyTable" , attribValue);
    	
    	mapper.save(company);
    	
    	return company;
    }
    
    public List<Company> getAllCompanies() {

        List<Company> mysfits = mapper.scan(Company.class, new DynamoDBScanExpression());
        List<Company> allCompanies = new ArrayList<Company>(mysfits);
        return allCompanies;
    }
    
    public void deleteCompany(Company company) {
    	
		mapper.delete(company);
	}

	public Company findByCompanyCode(String companyCode) {
		
		return mapper.load(Company.class, companyCode);
	}

	public void updateCompany(Company company) {
		mapper.save(company,buildExpression(company));
		
	}
	
	private DynamoDBSaveExpression buildExpression(Company company) {
		DynamoDBSaveExpression dynamoDBSaveExpression = new DynamoDBSaveExpression();
		Map<String , ExpectedAttributeValue> expectedMap = new HashMap<>();
		expectedMap.put("companyCode", new ExpectedAttributeValue(new AttributeValue().withS(company.getCompanyCode())));
		dynamoDBSaveExpression.setExpected(expectedMap);
		return dynamoDBSaveExpression;
	}
	
    
}
