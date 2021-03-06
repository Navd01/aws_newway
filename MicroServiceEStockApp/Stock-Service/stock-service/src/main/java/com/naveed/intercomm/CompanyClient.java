package com.naveed.intercomm;


import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.naveed.beans.Company;

@FeignClient(name="company-service" ,url="http://localhost:8100")
@RibbonClient(name="company-service")


public interface CompanyClient {
	@RequestMapping(method = RequestMethod.GET, value = "/api/v1.0/market/company/info/{companyCode}")
    Company getCompany(@PathVariable("companyCode") String companyCode);

	
    
	@RequestMapping(method = RequestMethod.POST, value = "/api/v1.0/market/company/register")
    Company postLatestStock(Company company);
	
	@RequestMapping(method = RequestMethod.GET, value = "/api/v1.0/market/company/gettingCheckedFromStock")
	String getFromCompany();
}




