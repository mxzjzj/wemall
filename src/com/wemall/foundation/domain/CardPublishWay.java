package com.wemall.foundation.domain;

import com.wemall.core.domain.IdEntity;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "wemall_card_publish_way")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CardPublishWay extends Atom {
    @Column(name = "way_name")
    private String wayName;

    public String getWayName() {
        return wayName;
    }

    public void setWayName(String wayName) {
        this.wayName = wayName;
    }
}
