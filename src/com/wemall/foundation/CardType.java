package com.wemall.foundation;

import com.wemall.core.domain.IdEntity;
import com.wemall.foundation.domain.Atom;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "wemall_card_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CardType extends Atom {
    @Column(name = "type_name")
    private String typeName;

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
