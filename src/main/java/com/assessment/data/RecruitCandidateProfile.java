package com.assessment.data;

import javax.persistence.Entity;

@Entity
public class RecruitCandidateProfile extends Base{

	String firstName;
	
	String lastName;

	String email;

	String candidateCVName;

	String candidateCVURL;
	
	Long jobDescriptionId;
	
	String recruiterEmail;
	
	String jobDescriptionName;
	
	public String getJobDescriptionName() {
		return jobDescriptionName;
	}

	public void setJobDescriptionName(String jobDescriptionName) {
		this.jobDescriptionName = jobDescriptionName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCandidateCVName() {
		return candidateCVName;
	}

	public void setCandidateCVName(String candidateCVName) {
		this.candidateCVName = candidateCVName;
	}

	public String getCandidateCVURL() {
		return candidateCVURL;
	}

	public void setCandidateCVURL(String candidateCVURL) {
		this.candidateCVURL = candidateCVURL;
	}

	public Long getJobDescriptionId() {
		return jobDescriptionId;
	}

	public void setJobDescriptionId(Long jobDescriptionId) {
		this.jobDescriptionId = jobDescriptionId;
	}

	public String getRecruiterEmail() {
		return recruiterEmail;
	}

	public void setRecruiterEmail(String recruiterEmail) {
		this.recruiterEmail = recruiterEmail;
	}
	
}
