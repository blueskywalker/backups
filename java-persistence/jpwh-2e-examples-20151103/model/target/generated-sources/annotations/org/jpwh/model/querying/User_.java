package org.jpwh.model.querying;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(User.class)
public abstract class User_ {

	public static volatile SingularAttribute<User, String> firstname;
	public static volatile SingularAttribute<User, Long> id;
	public static volatile SingularAttribute<User, String> username;
	public static volatile SingularAttribute<User, String> lastname;
	public static volatile SingularAttribute<User, Boolean> activated;
	public static volatile SingularAttribute<User, Address> homeAddress;

}

