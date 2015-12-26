package org.jpwh.model.filtering.cascade;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(User.class)
public abstract class User_ {

	public static volatile SetAttribute<User, BillingDetails> billingDetails;
	public static volatile SingularAttribute<User, Long> id;
	public static volatile SingularAttribute<User, String> username;

}

