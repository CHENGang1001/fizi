package com.gq.business.inforesources.mappers;

import java.util.List;

import com.gq.business.inforesources.model.GetOrganList;

public interface GetOrganListMapper {
	List<GetOrganList> getOrganList(String partid);
}
