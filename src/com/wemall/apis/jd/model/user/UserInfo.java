package com.wemall.apis.jd.model.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.wemall.foundation.domain.Atom;

@Entity
@Table(name="apis_jd_user_info")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class UserInfo  extends Atom{


	  @Column(name="name")
	  private String name;

	  @Column(name="province")
	  private String province;

	  @Column(name="province_val")
	  private String provinceVal;

	  @Column(name="city")
	  private String city;

	  @Column(name="city_val")
	  private String cityVal;

	  @Column(name="county")
	  private String county;

	  @Column(name="county_val")
	  private String countyVal;

	  @Column(name="town")
	  private String town;

	  @Column(name="town_val")
	  private String townVal;

	  @Column(name="address")
	  private String address;

	  @Column(name="mobile")
	  private String mobile;

	  @Column(name="email")
	  private String email;

	  @Column(name="companyName")
	  private String companyName;

	  @Column(name="postcode")
	  private String postcode;

	  @Column(name="remark_end")
	  private String remarkEnd;

	  @Column(name="service_state")
	  private String serviceState;

	  @Column(name="internals_nature")
	  private String internalsNature;

	  @Column(name="leave_a_note_end")
	  private String leaveANoteEnd;

	  @Column(name="pay_state")
	  private String payState;

	  public String getRemarkEnd()
	  {
	    return this.remarkEnd;
	  }
	  public void setRemarkEnd(String remarkEnd) {
	    this.remarkEnd = remarkEnd;
	  }
	  public String getServiceState() {
	    return this.serviceState;
	  }
	  public void setServiceState(String serviceState) {
	    this.serviceState = serviceState;
	  }
	  public String getInternalsNature() {
	    return this.internalsNature;
	  }
	  public void setInternalsNature(String internalsNature) {
	    this.internalsNature = internalsNature;
	  }
	  public String getLeaveANoteEnd() {
	    return this.leaveANoteEnd;
	  }
	  public void setLeaveANoteEnd(String leaveANoteEnd) {
	    this.leaveANoteEnd = leaveANoteEnd;
	  }
	  public String getPayState() {
	    return this.payState;
	  }
	  public void setPayState(String payState) {
	    this.payState = payState;
	  }
	  public String getPostcode() {
	    return this.postcode;
	  }
	  public void setPostcode(String postcode) {
	    this.postcode = postcode;
	  }
	  public String getName() {
	    return this.name;
	  }
	  public void setName(String name) {
	    this.name = name;
	  }
	  public String getProvince() {
	    return this.province;
	  }
	  public void setProvince(String province) {
	    this.province = province;
	  }
	  public String getCity() {
	    return this.city;
	  }
	  public void setCity(String city) {
	    this.city = city;
	  }
	  public String getCounty() {
	    return this.county;
	  }
	  public void setCounty(String county) {
	    this.county = county;
	  }
	  public String getTown() {
	    return this.town;
	  }
	  public void setTown(String town) {
	    this.town = town;
	  }
	  public String getAddress() {
	    return this.address;
	  }
	  public void setAddress(String address) {
	    this.address = address;
	  }
	  public String getMobile() {
	    return this.mobile;
	  }
	  public void setMobile(String mobile) {
	    this.mobile = mobile;
	  }
	  public String getEmail() {
	    return this.email;
	  }
	  public void setEmail(String email) {
	    this.email = email;
	  }

	  public String getCompanyName() {
	    return this.companyName;
	  }
	  public void setCompanyName(String companyName) {
	    this.companyName = companyName;
	  }
	  public String getProvinceVal() {
	    return this.provinceVal;
	  }
	  public void setProvinceVal(String provinceVal) {
	    this.provinceVal = provinceVal;
	  }
	  public String getCityVal() {
	    return this.cityVal;
	  }
	  public void setCityVal(String cityVal) {
	    this.cityVal = cityVal;
	  }
	  public String getCountyVal() {
	    return this.countyVal;
	  }
	  public void setCountyVal(String countyVal) {
	    this.countyVal = countyVal;
	  }
	  public String getTownVal() {
	    return this.townVal;
	  }
	  public void setTownVal(String townVal) {
	    this.townVal = townVal;
	  }
}
