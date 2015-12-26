package org.jpwh.model.inheritance.singletable;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(BankAccount.class)
public abstract class BankAccount_ extends org.jpwh.model.inheritance.singletable.BillingDetails_ {

	public static volatile SingularAttribute<BankAccount, String> bankName;
	public static volatile SingularAttribute<BankAccount, String> account;
	public static volatile SingularAttribute<BankAccount, String> swift;

}

