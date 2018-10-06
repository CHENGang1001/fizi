package com.gq.business.inforesources.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gq.business.inforesources.mappers.GetOrganListMapper;
import com.gq.business.inforesources.model.GetOrganList;
import com.gq.business.inforesources.service.GetOrganListService;

@Service
public class GetOrganListServiceImpl implements GetOrganListService{
	
	@Autowired
	private GetOrganListMapper organListMapper;
	
	public List<GetOrganList> getOrganList(String partid){
		return this.organListMapper.getOrganList(partid);
	}
}
