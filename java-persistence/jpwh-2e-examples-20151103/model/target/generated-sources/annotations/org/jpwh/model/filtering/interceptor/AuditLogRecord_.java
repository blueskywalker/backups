package org.jpwh.model.filtering.interceptor;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(AuditLogRecord.class)
public abstract class AuditLogRecord_ {

	public static volatile SingularAttribute<AuditLogRecord, Class> entityClass;
	public static volatile SingularAttribute<AuditLogRecord, Long> entityId;
	public static volatile SingularAttribute<AuditLogRecord, Long> id;
	public static volatile SingularAttribute<AuditLogRecord, String> message;
	public static volatile SingularAttribute<AuditLogRecord, Long> userId;
	public static volatile SingularAttribute<AuditLogRecord, Date> createdOn;

}

