package com.umasuo.datapoint.domain.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

/**
 * PlatformDataDefinition.
 */
@Data
@Entity
@Table(name = "platform_data_definition")
@EntityListeners(AuditingEntityListener.class)
public class PlatformDataDefinition implements Serializable{

  /**
   * The serialVersionUID.
   */
  private static final long serialVersionUID = -3480326990339520679L;

  /**
   * Id.
   */
  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  @Column(name = "id")
  private String id;

  /**
   * The Created at.
   */
  @CreatedDate
  @Column(name = "created_at")
  protected Long createdAt;

  /**
   * The Last modified at.
   */
  @LastModifiedDate
  @Column(name = "last_modified_at")
  protected Long lastModifiedAt;

  /**
   * version used for update date check.
   */
  @Version
  private Integer version;

  /**
   * data id defined by the developer. 开发者ID＋ dataId全局唯一.
   */
  private String dataId;

  /**
   * ProductType id.
   */
  private String productTypeId;

  /**
   * the data structure.
   */
  @Column(length = 65536)
  private String dataSchema;

  /**
   * name of this definition.
   */
  private String name;

  /**
   * describe the usage of this definition.
   */
  private String description;
}
