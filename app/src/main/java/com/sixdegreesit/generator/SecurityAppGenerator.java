package com.sixdegreesit.generator;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class SecurityAppGenerator {

	public static void main(String[] args) throws Exception {
		Schema schema = new Schema(6, "com.android.db");
		createSecurityApp(schema);
	/*	new DaoGenerator().generateAll(schema,
				"/Users/Sanjay/Documents/Android Softwares/Workspace/SecurityApp/src");*/
		
		new DaoGenerator().generateAll(schema,
				"E:/bluescan/testbluescan/SecurityApp/src");
		
	
	}

	private static void createSecurityApp(Schema schema) {

		Entity user = schema.addEntity("User");
		user.addStringProperty("userId").primaryKey();
		user.addStringProperty("emailId");
		user.addStringProperty("status");
		user.addStringProperty("companyId");
		user.addStringProperty("name");
		user.addStringProperty("roleId");
		user.addStringProperty("roleName");
		user.addStringProperty("code");
		user.addStringProperty("regionId");
		user.addStringProperty("branchId");
		user.addStringProperty("siteId");
		user.addStringProperty("userTasksIds");
		user.addStringProperty("userTasks");
		user.addStringProperty("sessionToken");

		Entity site = schema.addEntity("Site");
		site.addStringProperty("site_id").primaryKey();
		site.addStringProperty("address");
		site.addStringProperty("zipcode");
		site.addStringProperty("city");
		site.addStringProperty("emailId");
		site.addStringProperty("contactPerson");
		site.addStringProperty("contactNumber");
		site.addStringProperty("branchId");
		site.addStringProperty("branchName");
		site.addStringProperty("customerName");		
		site.addStringProperty("companyName");
		site.addStringProperty("user_id");
		site.addStringProperty("siteVisitingId");
		
		Entity guard = schema.addEntity("Guard");
		guard.addIdProperty().autoincrement().primaryKey();
		guard.addStringProperty("guard_id");
		guard.addStringProperty("company_id");
		guard.addStringProperty("first_name");
		guard.addStringProperty("last_name");
		guard.addStringProperty("phone");
		guard.addStringProperty("mobile");
		guard.addStringProperty("address");
		guard.addStringProperty("zip");
		guard.addStringProperty("post");
		guard.addStringProperty("technical_qualification");
		guard.addStringProperty("language_known");
		guard.addStringProperty("experience");
		guard.addStringProperty("passcode");
		guard.addStringProperty("pf_no");
		guard.addStringProperty("esi_no");
		guard.addStringProperty("status");
		guard.addStringProperty("is_deleted");
		guard.addStringProperty("created_by");
		guard.addDateProperty("created_date");
		guard.addStringProperty("qr_code");
		guard.addStringProperty("site_id");
		guard.addStringProperty("companyName");
		guard.addStringProperty("user_id");
		guard.addStringProperty("siteVisitingId");
		guard.addStringProperty("img_url");
		guard.addStringProperty("designation");

		Entity questions = schema.addEntity("Questions");
		questions.addStringProperty("question_id").primaryKey();
		questions.addStringProperty("question");
		questions.addStringProperty("company_id");
		questions.addStringProperty("is_mandatory");
		questions.addStringProperty("sequence");
		questions.addStringProperty("question_type");
		questions.addStringProperty("question_option");
		questions.addStringProperty("remark");
		questions.addStringProperty("is_published");
		questions.addStringProperty("answer");
		
		Entity logsData = schema.addEntity("LogsData");
		logsData.addIdProperty().primaryKey().autoincrement();
		logsData.addStringProperty("userId");
		logsData.addStringProperty("parameters");
		logsData.addStringProperty("isSynced");
		logsData.addDateProperty("dateAdded");
		
		
		Entity siteMaster = schema.addEntity("SiteMaster");
		siteMaster.addStringProperty("site_id").primaryKey();
		siteMaster.addStringProperty("qr_code");
		siteMaster.addStringProperty("address");
		siteMaster.addStringProperty("zipcode");
		siteMaster.addStringProperty("city");
		siteMaster.addStringProperty("contact_person");
		siteMaster.addStringProperty("contact_number");
		siteMaster.addStringProperty("email_id");
		siteMaster.addStringProperty("branch_id");
		siteMaster.addStringProperty("branch_name");
		siteMaster.addStringProperty("customer_name");
		siteMaster.addStringProperty("company_name");
		siteMaster.addStringProperty("guard");
		siteMaster.addStringProperty("siteName");
		
		Entity gaurdMaster = schema.addEntity("GaurdMaster");
		gaurdMaster.addStringProperty("user_id").primaryKey();
		gaurdMaster.addStringProperty("company_id");
		gaurdMaster.addStringProperty("first_name");
		gaurdMaster.addStringProperty("last_name");
		gaurdMaster.addStringProperty("father_name");
		gaurdMaster.addStringProperty("dob");
		gaurdMaster.addStringProperty("age");
		gaurdMaster.addStringProperty("phone");
		gaurdMaster.addStringProperty("mobile");
		gaurdMaster.addStringProperty("l_address");
		gaurdMaster.addStringProperty("p_address");
		gaurdMaster.addStringProperty("latitude");
		gaurdMaster.addStringProperty("longitude");
		gaurdMaster.addStringProperty("zip");
		gaurdMaster.addStringProperty("user_name");
		gaurdMaster.addStringProperty("password");
		gaurdMaster.addStringProperty("email");
		gaurdMaster.addStringProperty("status");
		gaurdMaster.addStringProperty("is_deleted");
		gaurdMaster.addStringProperty("created_by");
		gaurdMaster.addDateProperty("created_date");
		gaurdMaster.addStringProperty("qr_code");
		gaurdMaster.addStringProperty("session_token");
		gaurdMaster.addStringProperty("apns_token");
		gaurdMaster.addStringProperty("img_url");
		gaurdMaster.addStringProperty("company_name");
		gaurdMaster.addStringProperty("site_ids");
		gaurdMaster.addStringProperty("designation");

		
		Entity masterQuestions = schema.addEntity("MasterQuestions");
		masterQuestions.addStringProperty("question_id");
		masterQuestions.addStringProperty("question");
		masterQuestions.addStringProperty("company_id");
		masterQuestions.addStringProperty("is_mandatory");
		masterQuestions.addStringProperty("sequence");
		masterQuestions.addStringProperty("question_type");
		masterQuestions.addStringProperty("question_option");
		masterQuestions.addStringProperty("remark");
		masterQuestions.addStringProperty("is_published");
		masterQuestions.addStringProperty("answer");
		masterQuestions.addStringProperty("qrcode");
		
		
		
		
		Entity designationData = schema.addEntity("DesignationData");
		designationData.addStringProperty("Designation");
		

	}
}
