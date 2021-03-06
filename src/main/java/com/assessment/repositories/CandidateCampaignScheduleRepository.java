package com.assessment.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.assessment.data.CandidateCampaignSchedule;

public interface CandidateCampaignScheduleRepository extends JpaRepository<CandidateCampaignSchedule, Long>{

//	CandidateCampaignSchedule findByEmailAndCompanyIdAnd
	CandidateCampaignSchedule findByEmailAndCampaignNameAndCompanyId(String email, String campaignName, String companyId);
}
