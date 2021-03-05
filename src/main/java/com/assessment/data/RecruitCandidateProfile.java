package com.assessment.data;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
public class RecruitCandidateProfile extends Base{

	String firstName;
	
	String lastName;

	String email;

	String candidateCVName;

	String candidateCVURL;
	
	Long jobDescriptionId;
	
	String recruiterEmail;
	
	@Transient
	String jobDescriptionName;
	
	@Transient
	String dateWhenFirstApplied;
	
	public RecruitCandidateProfile(){
		
	}
	
	public RecruitCandidateProfile(String firstName, String lastName, String email, String candidateCVURL, String recruiterEmail, String jobDescriptionName, Date createDate ){
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.candidateCVURL = candidateCVURL;
		this.recruiterEmail = recruiterEmail;
		this.jobDescriptionName = jobDescriptionName;
		this.createDate = createDate;
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

	public String getJobDescriptionName() {
		return jobDescriptionName;
	}

	public void setJobDescriptionName(String jobDescriptionName) {
		this.jobDescriptionName = jobDescriptionName;
	}

	public String getDateWhenFirstApplied() {
			if(getCreateDate() != null){
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm aa");
				return dateFormat.format(getCreateDate());
			}
		return "NA";
	}

	public void setDateWhenFirstApplied(String dateWhenFirstApplied) {
		this.dateWhenFirstApplied = dateWhenFirstApplied;
	}

	
	
	
	
}
