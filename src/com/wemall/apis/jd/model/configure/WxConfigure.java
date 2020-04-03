package com.wemall.apis.jd.model.configure;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by zhangjun on 2017-07-16.
 */
@Entity
@Table(name = "apis_wechat_token")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class WxConfigure  implements Serializable {
    private static final long serialVersionUID = 8889641215543004970L;

    @Id
    @Column(unique=true, nullable=false)
    @GeneratedValue(strategy=GenerationType.AUTO)
    protected int id;

    @Column(name = "token")
    private String token;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
