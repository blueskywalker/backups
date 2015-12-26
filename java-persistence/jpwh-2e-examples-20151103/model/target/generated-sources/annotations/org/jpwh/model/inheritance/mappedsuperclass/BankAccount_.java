package org.jpwh.model.inheritance.mappedsuperclass;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(BankAccount.class)
public abstract class BankAccount_ extends org.jpwh.model.inheritance.mappedsuperclass.BillingDetails_ {

	public static volatile SingularAttribute<BankAccount, Long> id;
	public static volatile SingularAttribute<BankAccount, String> bankname;
	public static volatile SingularAttribute<BankAccount, String> account;
	public static volatile SingularAttribute<BankAccount, String> swift;

}

