package com.wemall.foundation.domain;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class Atom  implements Serializable{
	
	private static final long serialVersionUID = 8889641215543004970L;

	  @Id
	  @Column(unique=true, nullable=false)
	  @GeneratedValue(strategy=GenerationType.AUTO)
	  protected long id;

	  @Column(name="create_date")
	  protected Date createDate = new Date();

	  @Column(name="update_date")
	  protected Date updateDate = new Date();

	  @Column(name="valid_flag")
	  protected Boolean validFlag = Boolean.valueOf(true);
	  protected String memo;

	  @ManyToOne(fetch=FetchType.LAZY)
	  @JoinColumn(name="creator_fk")
	  protected User creator;

	  @ManyToOne(fetch=FetchType.LAZY)
	  @JoinColumn(name="updater_fk")
	  protected User updater;

	  public long getId() { return this.id; }


	  public void setId(long id)
	  {
	    this.id = id;
	  }

	  public Date getCreateDate()
	  {
	    return this.createDate;
	  }

	  public void setCreateDate(Date createDate)
	  {
	    this.createDate = createDate;
	  }

	  public Date getUpdateDate()
	  {
	    return this.updateDate;
	  }

	  public void setUpdateDate(Date updateDate)
	  {
	    this.updateDate = updateDate;
	  }

	  public Boolean getValidFlag()
	  {
	    return this.validFlag;
	  }

	  public void setValidFlag(Boolean validFlag)
	  {
	    this.validFlag = validFlag;
	  }

	  public String getMemo()
	  {
	    return this.memo;
	  }

	  public void setMemo(String memo)
	  {
	    this.memo = memo;
	  }

	  public User getCreator()
	  {
	    return this.creator;
	  }

	  public void setCreator(User creator)
	  {
	    this.creator = creator;
	  }

	  public User getUpdater()
	  {
	    return this.updater;
	  }

	  public void setUpdater(User updater)
	  {
	    this.updater = updater;
	  }

}
