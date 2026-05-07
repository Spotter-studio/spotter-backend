package com.spotter.backend.stageddata.repository;

import com.spotter.backend.stageddata.entity.StagedData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StagedDataRepository extends JpaRepository<StagedData, Long> {

	List<StagedData> findByUser_Id(Long userId);
}
