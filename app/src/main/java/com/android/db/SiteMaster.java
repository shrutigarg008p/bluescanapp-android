package com.android.db;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table SITE_MASTER.
 */
public class SiteMaster {

    private String site_id;
    private String qr_code;
    private String address;
    private String zipcode;
    private String city;
    private String contact_person;
    private String contact_number;
    private String email_id;
    private String branch_id;
    private String branch_name;
    private String customer_name;
    private String company_name;
    private String guard;
    private String siteName;

    public SiteMaster() {
    }

    public SiteMaster(String site_id) {
        this.site_id = site_id;
    }

    public SiteMaster(String site_id, String qr_code, String address, String zipcode, String city, String contact_person, String contact_number, String email_id, String branch_id, String branch_name, String customer_name, String company_name, String guard, String siteName) {
        this.site_id = site_id;
        this.qr_code = qr_code;
        this.address = address;
        this.zipcode = zipcode;
        this.city = city;
        this.contact_person = contact_person;
        this.contact_number = contact_number;
        this.email_id = email_id;
        this.branch_id = branch_id;
        this.branch_name = branch_name;
        this.customer_name = customer_name;
        this.company_name = company_name;
        this.guard = guard;
        this.siteName = siteName;
    }

    public String getSite_id() {
        return site_id;
    }

    public void setSite_id(String site_id) {
        this.site_id = site_id;
    }

    public String getQr_code() {
        return qr_code;
    }

    public void setQr_code(String qr_code) {
        this.qr_code = qr_code;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getContact_person() {
        return contact_person;
    }

    public void setContact_person(String contact_person) {
        this.contact_person = contact_person;
    }

    public String getContact_number() {
        return contact_number;
    }

    public void setContact_number(String contact_number) {
        this.contact_number = contact_number;
    }

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public String getBranch_id() {
        return branch_id;
    }

    public void setBranch_id(String branch_id) {
        this.branch_id = branch_id;
    }

    public String getBranch_name() {
        return branch_name;
    }

    public void setBranch_name(String branch_name) {
        this.branch_name = branch_name;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getGuard() {
        return guard;
    }

    public void setGuard(String guard) {
        this.guard = guard;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

}
