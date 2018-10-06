package com.gq.business.inforesources.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gq.business.inforesources.mappers.GetPartListMapper;
import com.gq.business.inforesources.model.GetPartList;
import com.gq.business.inforesources.service.GetPartListService;

@Service
public class GetPartListServiceImpl implements GetPartListService{
	
    @Autowired
    private GetPartListMapper partListMapper;
    
    public List<GetPartList> getPartList(){
    	return this.partListMapper.getPartList();
    }
}
