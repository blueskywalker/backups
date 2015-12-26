package org.jpwh.model.inheritance.mappedsuperclass;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(CreditCard.class)
public abstract class CreditCard_ extends org.jpwh.model.inheritance.mappedsuperclass.BillingDetails_ {

	public static volatile SingularAttribute<CreditCard, String> expMonth;
	public static volatile SingularAttribute<CreditCard, String> expYear;
	public static volatile SingularAttribute<CreditCard, Long> id;
	public static volatile SingularAttribute<CreditCard, String> cardNumber;

}

