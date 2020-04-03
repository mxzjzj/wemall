package com.wemall.apis.jd.model.configure;

import com.wemall.foundation.domain.Atom;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "apis_jd_configure")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class JdConfigure extends Atom {
	@Column(name = "client_id")
	private String clientid;

	@Column(name = "client_Secret")
	private String clientSecret;

	@Column(name = "refresh_Token")
	private String refreshToken;

	@Column(name = "app_Token")
	private String appToken;

	public String getClientid() {
		return this.clientid;
	}

	public void setClientid(String clientid) {
		this.clientid = clientid;
	}

	public String getClientSecret() {
		return this.clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public String getRefreshToken() {
		return this.refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public String getAppToken() {
		return this.appToken;
	}

	public void setAppToken(String appToken) {
		this.appToken = appToken;
	}
}
