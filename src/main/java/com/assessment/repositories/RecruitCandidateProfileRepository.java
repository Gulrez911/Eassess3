package com.assessment.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.assessment.data.RecruitCandidateProfile;

public interface RecruitCandidateProfileRepository extends JpaRepository<RecruitCandidateProfile, Long> {

	List<RecruitCandidateProfile> findByJobDescriptionIdAndRecruiterEmail(Long jobId, String recruiterEmail);
}
