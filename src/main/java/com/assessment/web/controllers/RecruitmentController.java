package com.assessment.web.controllers;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.assessment.common.CommonUtil;
import com.assessment.common.PropertyConfig;
import com.assessment.common.util.NavigationConstants;
import com.assessment.data.Campaign;
import com.assessment.data.CampaignCandidate;
import com.assessment.data.CandidateDetailsForJD;
import com.assessment.data.JobDescription;
import com.assessment.data.JobDescriptionRecruiter;
import com.assessment.data.RecruitCandidateProfile;
import com.assessment.data.User;
import com.assessment.data.UserType;
import com.assessment.repositories.CampaignRepository;
import com.assessment.repositories.CandidateDetailsForJDRepository;
import com.assessment.repositories.JobDescriptionRecruiterRepository;
import com.assessment.repositories.JobDescriptionRepository;
import com.assessment.repositories.RecruitCandidateProfileRepository;
import com.assessment.services.CampaignService;
import com.assessment.services.JobDescriptionRecruiterService;
import com.assessment.services.JobDescriptionService;
import com.assessment.services.UserService;

@Controller
public class RecruitmentController {

	@Autowired
	UserService userService;
	@Autowired
	PropertyConfig propertyConfig;
	@Autowired
	JobDescriptionService descriptionService;
	@Autowired
	JobDescriptionRepository descriptionRepository;
	@Autowired
	JobDescriptionRecruiterRepository descriptionRecruiterRepository;
	@Autowired
	JobDescriptionRecruiterService descriptionRecruiterService;
	@Autowired
	CampaignService campaignService;
	@Autowired
	CampaignRepository campaignRepository;
	@Autowired
	RecruitCandidateProfileRepository recruitCandidateProfileRepository;
	@Autowired
	CandidateDetailsForJDRepository jdRepository;

	@GetMapping("/recruiters")
	public ModelAndView recruitment(@RequestParam(name = "page", required = false) Integer pageNumber, HttpServletRequest request, HttpServletResponse response,
								ModelMap modelMap) {
		ModelAndView mav = new ModelAndView("recruiters");
		User user = (User) request.getSession().getAttribute("user");
		if (pageNumber == null) {
			pageNumber = 0;
		}
		Page<User> users = userService.findUsersByTypeAndCompany(user.getCompanyId(), UserType.RECRUITER.getType(),
									PageRequest.of(pageNumber, NavigationConstants.NO_RECRUITER_PAGE));
		mav.addObject("usr", new User());
		mav.addObject("users", users.getContent());
		CommonUtil.setCommonAttributesOfPagination(users, modelMap, pageNumber, "recruiters", null);
		return mav;
	}

	@PostMapping("/saveRecruiter")
	public ModelAndView saveRecruiter(@RequestParam(name = "page", required = false) Integer pageNumber, @ModelAttribute("usr") User usr, HttpServletRequest request,
								HttpServletResponse response, ModelMap modelMap) {
		ModelAndView mav = new ModelAndView("recruiters");
		User user = (User) request.getSession().getAttribute("user");
		usr.setCompanyId(user.getCompanyId());
		usr.setCompanyName(user.getCompanyName());
		usr.setUserType(UserType.RECRUITER);
		userService.saveOrUpdateRecruiter(usr);
		if (pageNumber == null) {
			pageNumber = 0;
		}
		Page<User> users = userService.findUsersByTypeAndCompany(user.getCompanyId(), UserType.RECRUITER.getType(),
									PageRequest.of(pageNumber, NavigationConstants.NO_RECRUITER_PAGE));
		mav.addObject("usr", usr);
		mav.addObject("users", users.getContent());
		CommonUtil.setCommonAttributesOfPagination(users, modelMap, pageNumber, "recruiters", null);
		return mav;
	}

	@RequestMapping(value = "/editRecruiter", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> editRecruiter(HttpServletResponse response, HttpServletRequest request, @RequestParam String email) throws Exception {
		Map<String, Object> map = new HashMap<>();
		User user = (User) request.getSession().getAttribute("user");
		User user2 = userService.findByPrimaryKey(email, user.getCompanyId());
		map.put("user2", user2);
		return map;
	}

	@RequestMapping(value = "/searchRecruiter", method = RequestMethod.GET)
	public ModelAndView searchRecruiter(@RequestParam(name = "page", required = false) Integer pageNumber, @RequestParam String searchText, HttpServletResponse response,
								HttpServletRequest request) throws Exception {
		User user = (User) request.getSession().getAttribute("user");
		if (pageNumber == null) {
			pageNumber = 0;
		}
		Page<User> users = userService.searchRecruiters(user.getCompanyId(), searchText, PageRequest.of(pageNumber, NavigationConstants.NO_RECRUITER_PAGE));
		ModelAndView mav = new ModelAndView("recruiters");
		mav.addObject("users", users.getContent());
		mav.addObject("usr", new User());
		Map<String, String> queryParams = new HashMap<>();
		queryParams.put("searchText", searchText);
		CommonUtil.setCommonAttributesOfPagination(users, mav.getModelMap(), pageNumber, "searchRecruiter", queryParams);
		return mav;
	}

	@GetMapping("/jobDescriptions")
	public ModelAndView jobDescription(@RequestParam(name = "page", required = false) Integer pageNumber, @RequestParam(name = "msg", required = false) String msg,
								HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
		ModelAndView mav = new ModelAndView("jobDescription");
		User user = (User) request.getSession().getAttribute("user");
		if (msg != null) {
			mav.addObject("msgtype", "Information");
			mav.addObject("message", msg);
			mav.addObject("icon", "success");
		}
		if (pageNumber == null) {
			pageNumber = 0;
		}
		Page<JobDescription> descriptions = descriptionService.findByCompanyId(user.getCompanyId(), PageRequest.of(pageNumber, NavigationConstants.NO_RECRUITER_PAGE));
//			 mav.addObject("usr", new User());
		mav.addObject("descriptions", descriptions.getContent());
		CommonUtil.setCommonAttributesOfPagination(descriptions, modelMap, pageNumber, "jobDescriptions", null);
		return mav;
	}

	@GetMapping("/createJobStep1")
	public ModelAndView createJobStep1(@RequestParam(required = false) Long id, HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
		ModelAndView mav = new ModelAndView("jobDescStep1");
//		ModelAndView mav = new ModelAndView("testUpload");
		JobDescription description = new JobDescription();
		User user = (User) request.getSession().getAttribute("user");
		description.setCompanyId(user.getCompanyId());
		description.setCompanyName(user.getCompanyName());
		if (id != null) {
			description = descriptionRepository.findById(id).get();
			mav.addObject("disabled", true);
//			request.getSession().setAttribute("newCampaign", false);
		} else {
			mav.addObject("disabled", false);
//			request.getSession().setAttribute("newCampaign", true);
		}
		request.getSession().setAttribute("description", description);
		mav.addObject("description", description);
		return mav;
	}

	@PostMapping("/goToJobStep2")
	@ResponseBody
	public String goToJobStep2(@RequestParam(name = "name", required = false) String name, @RequestParam(name = "file", required = false) MultipartFile uploadFile,
								HttpServletRequest request) throws IOException {
		if (uploadFile != null && uploadFile.getSize() != 0) {
			String destination = propertyConfig.getFileServerLocation() + File.separator + uploadFile.getOriginalFilename();
			JobDescription description = (JobDescription) request.getSession().getAttribute("description");
			description.setName(name);
			File file = new File(destination);
			if (file.exists()) {
				if (uploadFile.getOriginalFilename() != null && uploadFile.getOriginalFilename().trim().length() > 0) {
					FileUtils.forceDelete(file);
				}

			}
			if (uploadFile.getOriginalFilename() != null && uploadFile.getOriginalFilename().trim().length() > 0) {
				String fileUrl = propertyConfig.getFileServerWebUrl() + File.separator + uploadFile.getOriginalFilename();

				uploadFile.transferTo(file);
				description.setJobDescFileUrl(fileUrl);
			}
		}
		return "ok";
	}

	@GetMapping("/showRecruiters")
	@ResponseBody
	public String showRecruiters(HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("user");
		JobDescription description = (JobDescription) request.getSession().getAttribute("description");
//		 campaignName = URLDecoder.decode(campaignName);

		List<JobDescriptionRecruiter> descriptionRecruiters = descriptionRecruiterRepository.findByCompanyIdAndJobDescriptionName(user.getCompanyId(),
									description.getName());
		String parent = "<ul class=\"mt-3\" style=\"width: 250%;\">	${RECRUITERS}</ul>";

		String childs = "";
		for (JobDescriptionRecruiter recruiter : descriptionRecruiters) {
			String child = "<li style=\"border-bottom:none\">	<b>${FIRST}, ${LAST}	</b>&nbsp;&nbsp;	(<small>  ${EMAIL}</small>)</li>\n";
			child = child.replace("${FIRST}", recruiter.getFirstName() == null ? "NA" : recruiter.getFirstName());
			child = child.replace("${LAST}", recruiter.getLastName() == null ? "NA" : recruiter.getLastName());
			child = child.replace("${EMAIL}", recruiter.getEmail());
			childs += child;
		}
		parent = parent.replace("${RECRUITERS}", childs);
		return parent;
	}

	@GetMapping("/showCampaign")
	@ResponseBody
	public String showCampaign(HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("user");
		JobDescription description = (JobDescription) request.getSession().getAttribute("description");

		String parent = "<ul class=\"mt-3\" style=\"width: 250%;\">	${CAMPAIGN}</ul>";
		String childs = "";
		String child = "<li style=\"border-bottom:none\">	<b>Campaign Name: </b>&nbsp;&nbsp;	   ${CAMPAIGN_NAME} </li>\n";
		child = child.replace("${CAMPAIGN_NAME}", description.getCampaign().getCampaignName() == null ? "NA" : description.getCampaign().getCampaignName());
		childs += child;
		parent = parent.replace("${CAMPAIGN}", childs);
		return parent;
	}

	@GetMapping("/searchAndPopulaterRecruitersTable")
	@ResponseBody
	public String searchAndPopulaterRecruitersTable(HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("user");
		Page<User> users = userService.findUsersByTypeAndCompany(user.getCompanyId(), UserType.RECRUITER.getType(), PageRequest.of(0, 100));
		String parent = "<table class=\"table\">	<thead>		<tr>			<th>				<input type=\"checkbox\" name=\"\" >			</th>			<th>				Name			</th>			<th>				Email			</th>		</tr>	</thead>	<tbody>		${ROWS}			</tbody></table>";
		String childs = "";
		for (User usr : users.getContent()) {
//			String child = "<tr id=\"T${ID}\">	<td>		<input type=\"checkbox\" name=\"${ID}\" id=\"${ID}\" onchange=\"changeCandidateCheckbox(this, '${CAMPAIGN_NAME}')\">	</td>	<td>		${FIRSTNAME}, ${LASTNAME}	</td>	<td>		${EMAIL}	</td></tr>";
			String child = "<tr id=\"T${ID}\">	<td>		<input type=\"checkbox\" name=\"${ID}\" id=\"${ID}\" onchange=\"changeRecruiterCheckbox(this)\">	</td>	<td>		${FIRSTNAME}, ${LASTNAME}	</td>	<td>		${EMAIL}	</td></tr>";

			child = child.replace("${FIRSTNAME}", usr.getFirstName() == null ? "NA" : usr.getFirstName());
			child = child.replace("${LASTNAME}", usr.getLastName() == null ? "NA" : usr.getLastName());
			child = child.replace("${EMAIL}", usr.getEmail());
			child = child.replace("${ID}", usr.getId() + "");
//			child = child.replace("${CAMPAIGN_NAME}", URLEncoder.encode(campaignName));
			childs += child;
		}
		parent = parent.replace("${ROWS}", childs);
		return parent;
//		return map;
	}

	@RequestMapping(value = "/selectRecruiterForJob", method = RequestMethod.GET)
	public @ResponseBody String selectCandidateForCampaign(@RequestParam(required = true, name = "cid") Long cid, HttpServletRequest request, HttpServletResponse response) {
		User user = (User) request.getSession().getAttribute("user");
		User can = userService.findById(cid);
		JobDescription description = (JobDescription) request.getSession().getAttribute("description");
		// campaignCandidateService
		JobDescriptionRecruiter descriptionRecruiter = new JobDescriptionRecruiter();
		descriptionRecruiter.setCompanyId(user.getCompanyId());
		descriptionRecruiter.setCompanyName(user.getCompanyName());
		descriptionRecruiter.setJobDescriptionName(description.getName());
		descriptionRecruiter.setEmail(can.getEmail());
		descriptionRecruiter.setFirstName(can.getFirstName());
		descriptionRecruiter.setLastName(can.getLastName());
		JobDescription description2 = descriptionRepository.findByPrimaryKey(description.getName(), user.getCompanyId());
		if (description2 != null) {
			descriptionRecruiter.setJobDescriptionId(description2.getId());
		}
		descriptionRecruiterService.saveOrUpdate(descriptionRecruiter);
//			campaignCandidateService.saveOrUpdate(campaignCandidate);
		return "ok";
	}

	@RequestMapping(value = "/unselectRecruiterForJob", method = RequestMethod.GET)
	public @ResponseBody String unselectCandidateForCampaign(@RequestParam(required = true, name = "cid") Long cid, HttpServletRequest request, HttpServletResponse response) {
		User user = (User) request.getSession().getAttribute("user");
		JobDescription description = (JobDescription) request.getSession().getAttribute("description");
		User can = userService.findById(cid);
		JobDescriptionRecruiter descriptionRecruiter = descriptionRecruiterService.findUniqueJobDescriptionRecruiter(user.getCompanyId(), description.getName(),
									can.getEmail());
//		
		descriptionRecruiterRepository.deleteById(descriptionRecruiter.getId());
		return "ok";
	}

	@GetMapping("/searchAndPopulaterCampaignTable")
	@ResponseBody
	public String searchAndPopulaterCampaignTable(HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("user");
		Page<User> users = userService.findUsersByTypeAndCompany(user.getCompanyId(), UserType.RECRUITER.getType(), PageRequest.of(0, 100));
		List<Campaign> campaigns = campaignService.findByPrimaryKey(user.getCompanyId());
		String parent = "<table class=\"table\">	<thead>		<tr>			<th>				 			</th>			<th>			Campaign Name			</th>			<th>				Camapaign Description			</th>		</tr>	</thead>	<tbody>		${ROWS}			</tbody></table>";
		String childs = "";
		for (Campaign campaign : campaigns) {
//			String child = "<tr id=\"T${ID}\">	<td>		<input type=\"checkbox\" name=\"${ID}\" id=\"${ID}\" onchange=\"changeCandidateCheckbox(this, '${CAMPAIGN_NAME}')\">	</td>	<td>		${FIRSTNAME}, ${LASTNAME}	</td>	<td>		${EMAIL}	</td></tr>";
			String child = "<tr id=\"T${ID}\">	<td>		<input type=\"radio\" name=\"campaignId\" id=\"${ID}\" onchange=\"selectCampaign(this)\">	</td>	<td>		${CAMPAIGN_NAME}	</td>	<td>		${CAMPAIGN_DESC}	</td></tr>";

			child = child.replace("${CAMPAIGN_NAME}", campaign.getCampaignName());
			child = child.replace("${CAMPAIGN_DESC}", campaign.getCampaignDesc() == null ? "NA" : campaign.getCampaignDesc());
//			child = child.replace("${EMAIL}", usr.getEmail());
			child = child.replace("${ID}", campaign.getId() + "");
//			child = child.replace("${CAMPAIGN_NAME}", URLEncoder.encode(campaignName));
			childs += child;
		}
		parent = parent.replace("${ROWS}", childs);
		return parent;
//		return map;
	}

	@RequestMapping(value = "/selectCampaignForJob", method = RequestMethod.GET)
	public @ResponseBody String selectCampaignForJob(@RequestParam(required = true, name = "cid") Long cid, HttpServletRequest request, HttpServletResponse response) {
		User user = (User) request.getSession().getAttribute("user");
		JobDescription description = (JobDescription) request.getSession().getAttribute("description");
		Campaign campaign = campaignRepository.findById(cid).get();
		description.setCampaign(campaign);
		return "ok";
	}

	@RequestMapping(value = "/saveJobDescription", method = RequestMethod.GET)
	public ModelAndView saveJobDescription(HttpServletRequest request, HttpServletResponse response) {
		User user = (User) request.getSession().getAttribute("user");
		JobDescription description = (JobDescription) request.getSession().getAttribute("description");
		descriptionRepository.save(description);
		String msg = "Saved Successfully";
		return new ModelAndView("redirect:/jobDescriptions?msg=" + msg);
	}

	@PostMapping("/uploadProfile")
	@ResponseBody
	public String uploadProfile(@RequestParam Long jobId, @RequestParam String firstName, @RequestParam String lastName, @RequestParam(required = false) String email,
								@RequestParam(name = "file", required = false) MultipartFile uploadFile, HttpServletRequest request)
								throws IOException {
//		if (uploadFile != null && uploadFile.getSize() != 0) {
		User user = (User) request.getSession().getAttribute("user");
		String baseFolder = "";
		baseFolder = propertyConfig.getProfileBaseLocation() + File.separator + user.getRecruitmentCompanyName() + File.separator + user.getEmail() + File.separator
									+ jobId;
		File file = new File(baseFolder);
		file.mkdirs();
		File actual = new File(baseFolder + File.separator + uploadFile.getOriginalFilename());
		FileUtils.copyInputStreamToFile(uploadFile.getInputStream(), actual);// or uploadFile.transferTo(file);

		RecruitCandidateProfile candidateProfile = new RecruitCandidateProfile();
		candidateProfile.setCompanyId(user.getCompanyId());
		candidateProfile.setCompanyName(user.getCompanyName());
		candidateProfile.setFirstName(firstName);
		candidateProfile.setLastName(lastName);
		if (email != null) {
			candidateProfile.setEmail(email);
		}
		candidateProfile.setRecruiterEmail(user.getEmail());
		candidateProfile.setJobDescriptionId(jobId);
		candidateProfile.setCandidateCVName(uploadFile.getOriginalFilename());
		candidateProfile.setCandidateCVURL(propertyConfig.getFileServerWebUrl() + "Files" + File.separator + user.getRecruitmentCompanyName() + File.separator
									+ user.getEmail() + File.separator + jobId + File.separator + uploadFile.getOriginalFilename());
		recruitCandidateProfileRepository.save(candidateProfile);
//		}
		return "ok";
	}

	@GetMapping("/getCandidateProfile")
	@ResponseBody
	public String getCandidateProfile(@RequestParam Long jobId, HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("user");
		List<RecruitCandidateProfile> candidateProfiles = recruitCandidateProfileRepository.findByJobDescriptionIdAndRecruiterEmail(jobId, user.getEmail());

		Page<User> users = userService.findUsersByTypeAndCompany(user.getCompanyId(), UserType.RECRUITER.getType(), PageRequest.of(0, 100));
		String parent = "<table class=\"table\">	<thead>		<tr>  <th>				Name			</th>			<th>			Email				</th><th>		View Profile					</th>		</tr>	</thead>	<tbody>		${ROWS}			</tbody></table>";
		String childs = "";
		for (RecruitCandidateProfile candidateProfile : candidateProfiles) {
			String child = "<tr>		<td>		${FIRSTNAME}, ${LASTNAME}	</td>	<td>		${EMAIL}	</td><td>	<a href='${URL}' target='_blank'>View Profile</a>		</td></tr>";

			child = child.replace("${FIRSTNAME}", candidateProfile.getFirstName() == null ? "NA" : candidateProfile.getFirstName());
			child = child.replace("${LASTNAME}", candidateProfile.getLastName() == null ? "NA" : candidateProfile.getLastName());
			child = child.replace("${EMAIL}", candidateProfile.getEmail());
//			child = child.replace("${ID}", candidateProfile.getId() + "");
			child = child.replace("${URL}", candidateProfile.getCandidateCVURL());
//			child = child.replace("${CAMPAIGN_NAME}", URLEncoder.encode(campaignName));
			childs += child;
		}
		parent = parent.replace("${ROWS}", childs);
		return parent;
//		return map;
	}

	@GetMapping("/profileForJobDescription")
	public ModelAndView profileForJobDescription(@RequestParam(name = "page", required = false) Integer pageNumber, HttpServletRequest request, HttpServletResponse response,
								ModelMap modelMap) {
		ModelAndView mav = new ModelAndView("jobDescriptionProfile");
		User user = (User) request.getSession().getAttribute("user");
		if (pageNumber == null) {
			pageNumber = 0;
		}
		Page<JobDescription> descriptions = descriptionService.findByCompanyId(user.getCompanyId(), PageRequest.of(pageNumber, NavigationConstants.NO_RECRUITER_PAGE));
//			 mav.addObject("usr", new User());
		mav.addObject("descriptions", descriptions.getContent());
		CommonUtil.setCommonAttributesOfPagination(descriptions, modelMap, pageNumber, "jobDescriptions", null);
		return mav;
	}

	@GetMapping("/getCandidates")
	@ResponseBody
	public String getCandidates(@RequestParam(name = "id", required = false) Long id, HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("user");
		List<CandidateDetailsForJD> candidateDetailsForJDs = jdRepository.findByRelvancy(user.getCompanyId(), id);
		
		String parent = "<table class=\"table\">	<thead>	<tr><th>JD Name</th><th>Candidate</th>	<th>  View Parsing Details </th><th>   Bucket	</th><th>    No. Of relevant Years    </th><th>    Current Location   </th><th>    Source    </th><th>   Connect    </th>	<th>   Initiate Next Steps	  </th></tr>	</thead>	<tbody>		${ROWS}			</tbody></table>";
		String childs = "";
		for (CandidateDetailsForJD  jd : candidateDetailsForJDs) {
//			String child = "<tr id=\"T${ID}\">	<td>		<input type=\"checkbox\" name=\"${ID}\" id=\"${ID}\" onchange=\"changeCandidateCheckbox(this, '${CAMPAIGN_NAME}')\">	</td>	<td>		${FIRSTNAME}, ${LASTNAME}	</td>	<td>		${EMAIL}	</td></tr>";
			String child = "<tr>	<td>		${JDNAME}</td>	<td>		${CANDIDATE}	</td>	<td>		${PARSE}	</td><td>${BUCKET}</td><td>${RELEVANT_YEARS}</td><td>${CURRENT_LOCATION}</td><td>${SOURCE}</td><td>${CONNECT}</td><td><a href=\"javascript:openScheduleModel(${ARGS})\">Schedule</a></td></tr>";

			child = child.replace("${JDNAME}", jd.getJobDescName());
			child = child.replace("${CANDIDATE}", jd.getCandidateName());
//			child = child.replace("${FIRSTNAME}", candidateProfile.getFirstName() == null ? "NA" : candidateProfile.getFirstName());
			child = child.replace("${PARSE}", "<a href='#'>Click Here</a>");
			child = child.replace("${BUCKET}", "NA");
			child = child.replace("${RELEVANT_YEARS}", jd.getRelevantYears());
			child = child.replace("${CURRENT_LOCATION}", jd.getCurrentLocation());
			child = child.replace("${SOURCE}", "NA");
			child = child.replace("${CONNECT}", jd.getEmail());
//			child = child.replace("${NEXT_STEP}", "<a href='javascript:openScheduleModel("  +jd.getJobDescName()+ '\'  ","+jd.getEmail()+")'>Schedule</a>");"
			String args= "'" + jd.getJobDescName() + "','"+jd.getEmail() + "'";
			  child = child.replace("${ARGS}",args);
//			child = child.replace("${CAMPAIGN_NAME}", URLEncoder.encode(campaignName));
			childs += child;
		}
		parent = parent.replace("${ROWS}", childs);
		return parent;
//		return map;
	}
}
