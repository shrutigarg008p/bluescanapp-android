package com.sixdegreesit.utils;

import java.util.List;

public class GaurdProfileDTO {

	List<GaurdDTO> gaurdDTOs = null;
	List<QualificationDTO> qualificationDTOs = null;
	List<SkillDTO> skillDTOs = null;
	List<ExperienceDTO> experienceDTOs = null;
	List<ReviewDTO> reviewDTOs = null;
	List<RatingDTO> ratingDTOs = null;
	String numberOfReviews = "", avgRating = "";

	public GaurdProfileDTO(List<GaurdDTO> gaurdDTOs,
			List<QualificationDTO> qualificationDTOs, List<SkillDTO> skillDTOs,
			List<ExperienceDTO> experienceDTOs, List<ReviewDTO> reviewDTOs,
			List<RatingDTO> ratingDTOs, String numberOfReviews, String avgRating) {
		super();
		this.gaurdDTOs = gaurdDTOs;
		this.qualificationDTOs = qualificationDTOs;
		this.skillDTOs = skillDTOs;
		this.experienceDTOs = experienceDTOs;
		this.reviewDTOs = reviewDTOs;
		this.ratingDTOs = ratingDTOs;
		this.numberOfReviews = numberOfReviews;
		this.avgRating = avgRating;
	}

	public String getAvgRating() {
		return avgRating;
	}

	public void setAvgRating(String avgRating) {
		this.avgRating = avgRating;
	}

	public List<GaurdDTO> getGaurdDTOs() {
		return gaurdDTOs;
	}

	public void setGaurdDTOs(List<GaurdDTO> gaurdDTOs) {
		this.gaurdDTOs = gaurdDTOs;
	}

	public List<QualificationDTO> getQualificationDTOs() {
		return qualificationDTOs;
	}

	public void setQualificationDTOs(List<QualificationDTO> qualificationDTOs) {
		this.qualificationDTOs = qualificationDTOs;
	}

	public List<SkillDTO> getSkillDTOs() {
		return skillDTOs;
	}

	public void setSkillDTOs(List<SkillDTO> skillDTOs) {
		this.skillDTOs = skillDTOs;
	}

	public List<ExperienceDTO> getExperienceDTOs() {
		return experienceDTOs;
	}

	public void setExperienceDTOs(List<ExperienceDTO> experienceDTOs) {
		this.experienceDTOs = experienceDTOs;
	}

	public List<ReviewDTO> getReviewDTOs() {
		return reviewDTOs;
	}

	public void setReviewDTOs(List<ReviewDTO> reviewDTOs) {
		this.reviewDTOs = reviewDTOs;
	}

	public List<RatingDTO> getRatingDTOs() {
		return ratingDTOs;
	}

	public void setRatingDTOs(List<RatingDTO> ratingDTOs) {
		this.ratingDTOs = ratingDTOs;
	}

	public String getNumberOfReviews() {
		return numberOfReviews;
	}

	public void setNumberOfReviews(String numberOfReviews) {
		this.numberOfReviews = numberOfReviews;
	}

	public static class RatingDTO {
		String discipline_rating, punctuality_rating, fitness_rating,
				cleverness_rating, cleanliness_rating;

		public RatingDTO(String discipline_rating, String punctuality_rating,
				String fitness_rating, String cleverness_rating,
				String cleanliness_rating) {
			super();
			this.discipline_rating = discipline_rating;
			this.punctuality_rating = punctuality_rating;
			this.fitness_rating = fitness_rating;
			this.cleverness_rating = cleverness_rating;
			this.cleanliness_rating = cleanliness_rating;
		}

		public String getDiscipline_rating() {
			return discipline_rating;
		}

		public void setDiscipline_rating(String discipline_rating) {
			this.discipline_rating = discipline_rating;
		}

		public String getPunctuality_rating() {
			return punctuality_rating;
		}

		public void setPunctuality_rating(String punctuality_rating) {
			this.punctuality_rating = punctuality_rating;
		}

		public String getFitness_rating() {
			return fitness_rating;
		}

		public void setFitness_rating(String fitness_rating) {
			this.fitness_rating = fitness_rating;
		}

		public String getCleverness_rating() {
			return cleverness_rating;
		}

		public void setCleverness_rating(String cleverness_rating) {
			this.cleverness_rating = cleverness_rating;
		}

		public String getCleanliness_rating() {
			return cleanliness_rating;
		}

		public void setCleanliness_rating(String cleanliness_rating) {
			this.cleanliness_rating = cleanliness_rating;
		}

	}

	public static class ReviewDTO {
		String review_id, employee_id, review_by, review_text, review_date,
				rating, fo_name, img_url;

		public ReviewDTO(String review_id, String employee_id,
				String review_by, String review_text, String review_date,
				String rating, String fo_name, String img_url) {
			super();
			this.review_id = review_id;
			this.employee_id = employee_id;
			this.review_by = review_by;
			this.review_text = review_text;
			this.review_date = review_date;
			this.rating = rating;
			this.fo_name = fo_name;
			this.img_url = img_url;
		}

		public String getReview_id() {
			return review_id;
		}

		public void setReview_id(String review_id) {
			this.review_id = review_id;
		}

		public String getEmployee_id() {
			return employee_id;
		}

		public void setEmployee_id(String employee_id) {
			this.employee_id = employee_id;
		}

		public String getReview_by() {
			return review_by;
		}

		public void setReview_by(String review_by) {
			this.review_by = review_by;
		}

		public String getReview_text() {
			return review_text;
		}

		public void setReview_text(String review_text) {
			this.review_text = review_text;
		}

		public String getReview_date() {
			return review_date;
		}

		public void setReview_date(String review_date) {
			this.review_date = review_date;
		}

		public String getRating() {
			return rating;
		}

		public void setRating(String rating) {
			this.rating = rating;
		}

		public String getFo_name() {
			return fo_name;
		}

		public void setFo_name(String fo_name) {
			this.fo_name = fo_name;
		}

		public String getImg_url() {
			return img_url;
		}

		public void setImg_url(String img_url) {
			this.img_url = img_url;
		}

	}

	public static class ExperienceDTO {
		String employee_experience_id, employee_id, start_date, end_date,
				company_id, designation, salary_drawn, leaving_reason,
				verified_by, company_name, exp_duration_year,
				exp_duration_month;

		public ExperienceDTO(String employee_experience_id, String employee_id,
				String start_date, String end_date, String company_id,
				String designation, String salary_drawn, String leaving_reason,
				String verified_by, String company_name,
				String exp_duration_year, String exp_duration_month) {
			super();
			this.employee_experience_id = employee_experience_id;
			this.employee_id = employee_id;
			this.start_date = start_date;
			this.end_date = end_date;
			this.company_id = company_id;
			this.designation = designation;
			this.salary_drawn = salary_drawn;
			this.leaving_reason = leaving_reason;
			this.verified_by = verified_by;
			this.company_name = company_name;
			this.exp_duration_year = exp_duration_year;
			this.exp_duration_month = exp_duration_month;
		}

		public String getEmployee_experience_id() {
			return employee_experience_id;
		}

		public void setEmployee_experience_id(String employee_experience_id) {
			this.employee_experience_id = employee_experience_id;
		}

		public String getEmployee_id() {
			return employee_id;
		}

		public void setEmployee_id(String employee_id) {
			this.employee_id = employee_id;
		}

		public String getStart_date() {
			return start_date;
		}

		public void setStart_date(String start_date) {
			this.start_date = start_date;
		}

		public String getEnd_date() {
			return end_date;
		}

		public void setEnd_date(String end_date) {
			this.end_date = end_date;
		}

		public String getCompany_id() {
			return company_id;
		}

		public void setCompany_id(String company_id) {
			this.company_id = company_id;
		}

		public String getDesignation() {
			return designation;
		}

		public void setDesignation(String designation) {
			this.designation = designation;
		}

		public String getSalary_drawn() {
			return salary_drawn;
		}

		public void setSalary_drawn(String salary_drawn) {
			this.salary_drawn = salary_drawn;
		}

		public String getLeaving_reason() {
			return leaving_reason;
		}

		public void setLeaving_reason(String leaving_reason) {
			this.leaving_reason = leaving_reason;
		}

		public String getVerified_by() {
			return verified_by;
		}

		public void setVerified_by(String verified_by) {
			this.verified_by = verified_by;
		}

		public String getCompany_name() {
			return company_name;
		}

		public void setCompany_name(String company_name) {
			this.company_name = company_name;
		}

		public String getExp_duration_year() {
			return exp_duration_year;
		}

		public void setExp_duration_year(String exp_duration_year) {
			this.exp_duration_year = exp_duration_year;
		}

		public String getExp_duration_month() {
			return exp_duration_month;
		}

		public void setExp_duration_month(String exp_duration_month) {
			this.exp_duration_month = exp_duration_month;
		}
	}

	public static class SkillDTO {
		String employee_skill_id, employee_id, skill_id, skill_name;

		public SkillDTO(String employee_skill_id, String employee_id,
				String skill_id, String skill_name) {
			super();
			this.employee_skill_id = employee_skill_id;
			this.employee_id = employee_id;
			this.skill_id = skill_id;
			this.skill_name = skill_name;
		}

		public String getEmployee_skill_id() {
			return employee_skill_id;
		}

		public void setEmployee_skill_id(String employee_skill_id) {
			this.employee_skill_id = employee_skill_id;
		}

		public String getEmployee_id() {
			return employee_id;
		}

		public void setEmployee_id(String employee_id) {
			this.employee_id = employee_id;
		}

		public String getSkill_id() {
			return skill_id;
		}

		public void setSkill_id(String skill_id) {
			this.skill_id = skill_id;
		}

		public String getSkill_name() {
			return skill_name;
		}

		public void setSkill_name(String skill_name) {
			this.skill_name = skill_name;
		}

	}

	public static class QualificationDTO {
		String employee_id, qualification_id, qualification_name,
				is_qualification;

		public QualificationDTO(String employee_id, String qualification_id,
				String qualification_name, String is_qualification) {
			super();
			this.employee_id = employee_id;
			this.qualification_id = qualification_id;
			this.qualification_name = qualification_name;
			this.is_qualification = is_qualification;
		}

		public String getEmployee_id() {
			return employee_id;
		}

		public void setEmployee_id(String employee_id) {
			this.employee_id = employee_id;
		}

		public String getQualification_id() {
			return qualification_id;
		}

		public void setQualification_id(String qualification_id) {
			this.qualification_id = qualification_id;
		}

		public String getQualification_name() {
			return qualification_name;
		}

		public void setQualification_name(String qualification_name) {
			this.qualification_name = qualification_name;
		}

		public String getIs_qualification() {
			return is_qualification;
		}

		public void setIs_qualification(String is_qualification) {
			this.is_qualification = is_qualification;
		}

	}

	public static class GaurdDTO {
		String employee_id, user_id, qualification_id, language_known, status,
				is_deleted, created_by, created_date, ref_name_1, ref_add_1,
				ref_des_1, ref_name_2, ref_add_2, ref_des_2, bank_name,
				branch_name, ifsc_code, account_number, pf_account_number,
				esic_account_number, aadhar_card_verification,
				police_verification, education_verification,
				experience_verification, license_verification, aadhar_card_no,
				esic_smart_card_number, guard_id, company_id, first_name,
				last_name, aadhar_card_verification_img_url,
				police_verification_img_url, education_verification_img_url,
				experience_verification_img_url, license_verification_img_url,
				phone, mobile, address, company_name, img_url, site_id;

		public GaurdDTO(String employee_id, String user_id,
				String qualification_id, String language_known, String status,
				String is_deleted, String created_by, String created_date,
				String ref_name_1, String ref_add_1, String ref_des_1,
				String ref_name_2, String ref_add_2, String ref_des_2,
				String bank_name, String branch_name, String ifsc_code,
				String account_number, String pf_account_number,
				String esic_account_number, String aadhar_card_verification,
				String police_verification, String education_verification,
				String experience_verification, String license_verification,
				String aadhar_card_no, String esic_smart_card_number,
				String guard_id, String company_id, String first_name,
				String last_name, String aadhar_card_verification_img_url,
				String police_verification_img_url,
				String education_verification_img_url,
				String experience_verification_img_url,
				String license_verification_img_url, String phone,
				String mobile, String address, String company_name,
				String img_url, String site_id) {
			super();
			this.employee_id = employee_id;
			this.user_id = user_id;
			this.qualification_id = qualification_id;
			this.language_known = language_known;
			this.status = status;
			this.is_deleted = is_deleted;
			this.created_by = created_by;
			this.created_date = created_date;
			this.ref_name_1 = ref_name_1;
			this.ref_add_1 = ref_add_1;
			this.ref_des_1 = ref_des_1;
			this.ref_name_2 = ref_name_2;
			this.ref_add_2 = ref_add_2;
			this.ref_des_2 = ref_des_2;
			this.bank_name = bank_name;
			this.branch_name = branch_name;
			this.ifsc_code = ifsc_code;
			this.account_number = account_number;
			this.pf_account_number = pf_account_number;
			this.esic_account_number = esic_account_number;
			this.aadhar_card_verification = aadhar_card_verification;
			this.police_verification = police_verification;
			this.education_verification = education_verification;
			this.experience_verification = experience_verification;
			this.license_verification = license_verification;
			this.aadhar_card_no = aadhar_card_no;
			this.esic_smart_card_number = esic_smart_card_number;
			this.guard_id = guard_id;
			this.company_id = company_id;
			this.first_name = first_name;
			this.last_name = last_name;
			this.aadhar_card_verification_img_url = aadhar_card_verification_img_url;
			this.police_verification_img_url = police_verification_img_url;
			this.education_verification_img_url = education_verification_img_url;
			this.experience_verification_img_url = experience_verification_img_url;
			this.license_verification_img_url = license_verification_img_url;
			this.phone = phone;
			this.mobile = mobile;
			this.address = address;
			this.company_name = company_name;
			this.img_url = img_url;
			this.site_id = site_id;
		}

		public String getEmployee_id() {
			return employee_id;
		}

		public void setEmployee_id(String employee_id) {
			this.employee_id = employee_id;
		}

		public String getUser_id() {
			return user_id;
		}

		public void setUser_id(String user_id) {
			this.user_id = user_id;
		}

		public String getQualification_id() {
			return qualification_id;
		}

		public void setQualification_id(String qualification_id) {
			this.qualification_id = qualification_id;
		}

		public String getLanguage_known() {
			return language_known;
		}

		public void setLanguage_known(String language_known) {
			this.language_known = language_known;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public String getIs_deleted() {
			return is_deleted;
		}

		public void setIs_deleted(String is_deleted) {
			this.is_deleted = is_deleted;
		}

		public String getCreated_by() {
			return created_by;
		}

		public void setCreated_by(String created_by) {
			this.created_by = created_by;
		}

		public String getCreated_date() {
			return created_date;
		}

		public void setCreated_date(String created_date) {
			this.created_date = created_date;
		}

		public String getRef_name_1() {
			return ref_name_1;
		}

		public void setRef_name_1(String ref_name_1) {
			this.ref_name_1 = ref_name_1;
		}

		public String getRef_add_1() {
			return ref_add_1;
		}

		public void setRef_add_1(String ref_add_1) {
			this.ref_add_1 = ref_add_1;
		}

		public String getRef_des_1() {
			return ref_des_1;
		}

		public void setRef_des_1(String ref_des_1) {
			this.ref_des_1 = ref_des_1;
		}

		public String getRef_name_2() {
			return ref_name_2;
		}

		public void setRef_name_2(String ref_name_2) {
			this.ref_name_2 = ref_name_2;
		}

		public String getRef_add_2() {
			return ref_add_2;
		}

		public void setRef_add_2(String ref_add_2) {
			this.ref_add_2 = ref_add_2;
		}

		public String getRef_des_2() {
			return ref_des_2;
		}

		public void setRef_des_2(String ref_des_2) {
			this.ref_des_2 = ref_des_2;
		}

		public String getBank_name() {
			return bank_name;
		}

		public void setBank_name(String bank_name) {
			this.bank_name = bank_name;
		}

		public String getBranch_name() {
			return branch_name;
		}

		public void setBranch_name(String branch_name) {
			this.branch_name = branch_name;
		}

		public String getIfsc_code() {
			return ifsc_code;
		}

		public void setIfsc_code(String ifsc_code) {
			this.ifsc_code = ifsc_code;
		}

		public String getAccount_number() {
			return account_number;
		}

		public void setAccount_number(String account_number) {
			this.account_number = account_number;
		}

		public String getPf_account_number() {
			return pf_account_number;
		}

		public void setPf_account_number(String pf_account_number) {
			this.pf_account_number = pf_account_number;
		}

		public String getEsic_account_number() {
			return esic_account_number;
		}

		public void setEsic_account_number(String esic_account_number) {
			this.esic_account_number = esic_account_number;
		}

		public String getAadhar_card_verification() {
			return aadhar_card_verification;
		}

		public void setAadhar_card_verification(String aadhar_card_verification) {
			this.aadhar_card_verification = aadhar_card_verification;
		}

		public String getPolice_verification() {
			return police_verification;
		}

		public void setPolice_verification(String police_verification) {
			this.police_verification = police_verification;
		}

		public String getEducation_verification() {
			return education_verification;
		}

		public void setEducation_verification(String education_verification) {
			this.education_verification = education_verification;
		}

		public String getExperience_verification() {
			return experience_verification;
		}

		public void setExperience_verification(String experience_verification) {
			this.experience_verification = experience_verification;
		}

		public String getLicense_verification() {
			return license_verification;
		}

		public void setLicense_verification(String license_verification) {
			this.license_verification = license_verification;
		}

		public String getAadhar_card_no() {
			return aadhar_card_no;
		}

		public void setAadhar_card_no(String aadhar_card_no) {
			this.aadhar_card_no = aadhar_card_no;
		}

		public String getEsic_smart_card_number() {
			return esic_smart_card_number;
		}

		public void setEsic_smart_card_number(String esic_smart_card_number) {
			this.esic_smart_card_number = esic_smart_card_number;
		}

		public String getGuard_id() {
			return guard_id;
		}

		public void setGuard_id(String guard_id) {
			this.guard_id = guard_id;
		}

		public String getCompany_id() {
			return company_id;
		}

		public void setCompany_id(String company_id) {
			this.company_id = company_id;
		}

		public String getFirst_name() {
			return first_name;
		}

		public void setFirst_name(String first_name) {
			this.first_name = first_name;
		}

		public String getLast_name() {
			return last_name;
		}

		public void setLast_name(String last_name) {
			this.last_name = last_name;
		}

		public String getAadhar_card_verification_img_url() {
			return aadhar_card_verification_img_url;
		}

		public void setAadhar_card_verification_img_url(
				String aadhar_card_verification_img_url) {
			this.aadhar_card_verification_img_url = aadhar_card_verification_img_url;
		}

		public String getPolice_verification_img_url() {
			return police_verification_img_url;
		}

		public void setPolice_verification_img_url(
				String police_verification_img_url) {
			this.police_verification_img_url = police_verification_img_url;
		}

		public String getEducation_verification_img_url() {
			return education_verification_img_url;
		}

		public void setEducation_verification_img_url(
				String education_verification_img_url) {
			this.education_verification_img_url = education_verification_img_url;
		}

		public String getExperience_verification_img_url() {
			return experience_verification_img_url;
		}

		public void setExperience_verification_img_url(
				String experience_verification_img_url) {
			this.experience_verification_img_url = experience_verification_img_url;
		}

		public String getLicense_verification_img_url() {
			return license_verification_img_url;
		}

		public void setLicense_verification_img_url(
				String license_verification_img_url) {
			this.license_verification_img_url = license_verification_img_url;
		}

		public String getPhone() {
			return phone;
		}

		public void setPhone(String phone) {
			this.phone = phone;
		}

		public String getMobile() {
			return mobile;
		}

		public void setMobile(String mobile) {
			this.mobile = mobile;
		}

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}

		public String getCompany_name() {
			return company_name;
		}

		public void setCompany_name(String company_name) {
			this.company_name = company_name;
		}

		public String getImg_url() {
			return img_url;
		}

		public void setImg_url(String img_url) {
			this.img_url = img_url;
		}

		public String getSite_id() {
			return site_id;
		}

		public void setSite_id(String site_id) {
			this.site_id = site_id;
		}

	}

}
