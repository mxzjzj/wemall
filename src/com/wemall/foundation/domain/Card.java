package com.wemall.foundation.domain;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

import com.wemall.foundation.CardType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.wemall.core.domain.IdEntity;

@Entity
@Table(name="wemall_card")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class Card extends Atom {
    @Column(name="card_name")
    private String cardName;
    @Column(name="card_image")
    private String cardImage;
    @Column(name="card_amount", precision=12, scale=2)
    private BigDecimal cardAmount;
    @Column(name="begin_date")
    private Date beginDate;
    @Column(name="end_date")
    private Date endDate;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="card_type_fk")
    private CardType cardType;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="card_publish_way_fk")
    private CardPublishWay cardPublishWay;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="system_module_fk")
    private SysConfig systemModule;

    public String getCardName()
    {
        return cardName;
    }

    public void setCardName(String cardName)
    {
        this.cardName = cardName;
    }

    public String getCardImage()
    {
        return cardImage;
    }

    public void setCardImage(String cardImage)
    {
        this.cardImage = cardImage;
    }

    public BigDecimal getCardAmount()
    {
        return cardAmount;
    }

    public void setCardAmount(BigDecimal cardAmount)
    {
        this.cardAmount = cardAmount;
    }

    public Date getBeginDate()
    {
        return beginDate;
    }

    public void setBeginDate(Date beginDate)
    {
        this.beginDate = beginDate;
    }

    public Date getEndDate()
    {
        return endDate;
    }

    public void setEndDate(Date endDate)
    {
        this.endDate = endDate;
    }

    public CardType getCardType()
    {
        return cardType;
    }

    public void setCardType(CardType cardType)
    {
        this.cardType = cardType;
    }

    public CardPublishWay getCardPublishWay()
    {
        return cardPublishWay;
    }

    public void setCardPublishWay(CardPublishWay cardPublishWay)
    {
        this.cardPublishWay = cardPublishWay;
    }

    public SysConfig getSystemModule()
    {
        return systemModule;
    }

    public void setSystemModule(SysConfig systemModule)
    {
        this.systemModule = systemModule;
    }
}

