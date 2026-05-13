package com.spotter.backend.scrap.service;

import com.spotter.backend.common.exception.BusinessException;
import com.spotter.backend.common.exception.ErrorCode;
import com.spotter.backend.scrap.dto.ScrapDTO;
import com.spotter.backend.scrap.entity.Scrap;
import com.spotter.backend.scrap.repository.ScrapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ScrapCommandService {

	private final ScrapRepository scrapRepository;

	public ScrapDTO.Response update(Long userId, Long scrapId, ScrapDTO.UpdateRequest request) {
		Scrap scrap = findScrapOwnedBy(userId, scrapId);

		if (request.sourceUrl() != null) {
			scrap.addSourceUrl(request.sourceUrl());
		}
		scrap.update(request.visitCount(), request.sourceType());

		return ScrapMapper.toResponse(scrap);
	}

	public void delete(Long userId, Long scrapId) {
		Scrap scrap = findScrapOwnedBy(userId, scrapId);
		scrapRepository.delete(scrap);
	}

	private Scrap findScrapOwnedBy(Long userId, Long scrapId) {
		Scrap scrap = scrapRepository.findByIdWithFetchJoins(scrapId)
			.orElseThrow(() -> new BusinessException(ErrorCode.SCRAP_NOT_FOUND));
		if (!scrap.getUser().getId().equals(userId)) {
			throw new BusinessException(ErrorCode.SCRAP_FORBIDDEN);
		}
		return scrap;
	}
}
