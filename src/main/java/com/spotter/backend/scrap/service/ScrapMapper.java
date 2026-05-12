package com.spotter.backend.scrap.service;

import com.spotter.backend.scrap.dto.ScrapDTO;
import com.spotter.backend.scrap.entity.Scrap;

import java.util.List;

final class ScrapMapper {

	private ScrapMapper() {}

	static ScrapDTO.Response toResponse(Scrap scrap) {
		return new ScrapDTO.Response(
			scrap.getId(),
			scrap.getUser().getId(),
			scrap.getLocation().getId(),
			scrap.getLocation().getName(),
			scrap.getLocation().getCategory().getId(),
			List.copyOf(scrap.getSourceUrls()),
			scrap.getSourceType(),
			scrap.getVisitCount(),
			scrap.getCreatedAt()
		);
	}
}
