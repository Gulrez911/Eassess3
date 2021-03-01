package com.assessment.data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

@Entity
public class JobDescription extends Base {

	@Column(length = 200)
	String name;

	String testName;

	@Column(length = 3000)
	String description;

	@OneToMany(cascade=CascadeType.ALL, targetEntity=JobDescriptionSkill.class, orphanRemoval=true, fetch=FetchType.EAGER)
	@JoinColumn(name = "skill_id")
	Set<JobDescriptionSkill> skills = new HashSet<JobDescriptionSkill>();

	@Transient
	List<CandidateProfileParams> params = new ArrayList<>();

	@Transient
	String skillsDisplay;

	@Column(length = 2000)
	String jobDescFileUrl;

	Integer primarySkillWeight = 1;

	Integer secondarySkillWeight = 1;

	@OneToOne(fetch = FetchType.EAGER, cascade = { CascadeType.MERGE, CascadeType.REFRESH })
	@JoinColumn(name = "campaign_id")
	private Campaign campaign;

	public Campaign getCampaign() {
		return campaign;
	}

	public void setCampaign(Campaign campaign) {
		this.campaign = campaign;
	}

	public Integer getPrimarySkillWeight() {
		return primarySkillWeight;
	}

	public void setPrimarySkillWeight(Integer primarySkillWeight) {
		this.primarySkillWeight = primarySkillWeight;
	}

	public Integer getSecondarySkillWeight() {
		return secondarySkillWeight;
	}

	public void setSecondarySkillWeight(Integer secondarySkillWeight) {
		this.secondarySkillWeight = secondarySkillWeight;
	}

	public String getJobDescFileUrl() {
		return jobDescFileUrl;
	}

	public void setJobDescFileUrl(String jobDescFileUrl) {
		this.jobDescFileUrl = jobDescFileUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTestName() {
		return testName;
	}

	public void setTestName(String testName) {
		this.testName = testName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	
	public Set<JobDescriptionSkill> getSkills() {
		return skills;
	}

	public void setSkills(Set<JobDescriptionSkill> skills) {
		this.skills = skills;
	}

	public List<CandidateProfileParams> getParams() {
		return params;
	}

	public void setParams(List<CandidateProfileParams> params) {
		this.params = params;
	}

	public void setSkillsDisplay(String skillsDisplay) {
		this.skillsDisplay = skillsDisplay;
	}

	public String getSkillsDisplay() {
		String ret = "";
		if(getSkills() != null && getSkills().size() > 0){
			int count = 0;
			for(JobDescriptionSkill skill: getSkills()){
				if(count < getSkills().size()-1){
					ret += skill.getQualifier1()+" - "+skill.getQualifier2()+" - "+skill.getQualifier3()+" - "+skill.getQualifier4()+" - "+skill.getQualifier5()+"<br/>";
				}
				else{
					ret += skill.getQualifier1()+" - "+skill.getQualifier2()+" - "+skill.getQualifier3()+" - "+skill.getQualifier4()+" - "+skill.getQualifier5();
				}
				
			}
		}
		
		return ret;
	}

}
