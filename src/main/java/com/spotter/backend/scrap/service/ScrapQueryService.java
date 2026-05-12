package com.spotter.backend.scrap.service;

import com.spotter.backend.scrap.dto.ScrapDTO;
import com.spotter.backend.scrap.repository.ScrapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScrapQueryService {

	private final ScrapRepository scrapRepository;

	public List<ScrapDTO.Response> list(Long userId, Integer categoryId) {
		return scrapRepository.findByUserIdWithFetchJoins(userId, categoryId)
			.stream()
			.map(ScrapMapper::toResponse)
			.toList();
	}
}
