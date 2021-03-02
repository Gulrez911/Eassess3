package com.assessment.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.assessment.data.RecruitCandidateProfile;

public interface RecruitCandidateProfileRepository extends JpaRepository<RecruitCandidateProfile, Long> {

	List<RecruitCandidateProfile> findByJobDescriptionIdAndRecruiterEmailAndCompanyId(Long jobDescriptionId, String recruiterEmail, String companyId);
	
	
	
	@Query("SELECT r FROM RecruitCandidateProfile r WHERE r.jobDescriptionId=:jobDescriptionId and r.email=:email and r.companyId=:companyId")
	RecruitCandidateProfile findByPrimaryKey(@Param("jobDescriptionId") Long jobDescriptionId, @Param("email")  String email, @Param("companyId")  String companyId);
	
	
	List<RecruitCandidateProfile> findByJobDescriptionIdAndCompanyId(Long jobDescriptionId, String companyId);
}
