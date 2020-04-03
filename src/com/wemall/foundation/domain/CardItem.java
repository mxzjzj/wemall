package com.wemall.foundation.domain;


import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name="wemall_card_item")
@Cache(usage= CacheConcurrencyStrategy.READ_WRITE)
public class CardItem  extends Atom  {
    @Column(name="item_number")
    private String itemNumber;
    @Column(name="item_password")
    private String itemPassword;
    @Column(name="item_qrcode")
    private String itemQrcode;
    @Column(name="item_balance", precision=12, scale=2)
    private BigDecimal itemBalance;
    @Column(name="frozen_flag")
    private Boolean frozenFlag;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="card_fk")
    private Card card;
   /* @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="cart_item_fk")
    private CartItem cartItem;*/
    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="owner_fk")
    private User owner;

    public String getItemNumber()
    {
        return itemNumber;
    }

    public void setItemNumber(String itemNumber)
    {
        this.itemNumber = itemNumber;
    }

    public String getItemPassword()
    {
        return itemPassword;
    }

    public String getItemQrcode()
    {
        return itemQrcode;
    }

    public void setItemQrcode(String itemQrcode)
    {
        this.itemQrcode = itemQrcode;
    }

    public void setItemPassword(String itemPassword)
    {
        this.itemPassword = itemPassword;
    }

    public BigDecimal getItemBalance()
    {
        return itemBalance;
    }

    public void setItemBalance(BigDecimal itemBalance)
    {
        this.itemBalance = itemBalance;
    }

    public Boolean getFrozenFlag()
    {
        return frozenFlag;
    }

    public void setFrozenFlag(Boolean frozenFlag)
    {
        this.frozenFlag = frozenFlag;
    }

    public Card getCard()
    {
        return card;
    }

    public void setCard(Card card)
    {
        this.card = card;
    }

   /* public CartItem getCartItem()
    {
        return cartItem;
    }

    public void setCartItem(CartItem cartItem)
    {
        this.cartItem = cartItem;
    }*/

    public User getOwner()
    {
        return owner;
    }

    public void setOwner(User owner)
    {
        this.owner = owner;
    }

}
