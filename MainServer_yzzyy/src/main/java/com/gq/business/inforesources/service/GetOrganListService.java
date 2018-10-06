package com.gq.business.inforesources.service;

import java.util.List;

import com.gq.business.inforesources.model.GetOrganList;

public interface GetOrganListService {
	List<GetOrganList> getOrganList(String partid);
}
