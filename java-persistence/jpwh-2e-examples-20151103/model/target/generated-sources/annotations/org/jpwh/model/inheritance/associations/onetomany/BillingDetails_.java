package org.jpwh.model.inheritance.associations.onetomany;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(BillingDetails.class)
public abstract class BillingDetails_ {

	public static volatile SingularAttribute<BillingDetails, String> owner;
	public static volatile SingularAttribute<BillingDetails, Long> id;
	public static volatile SingularAttribute<BillingDetails, User> user;

}

