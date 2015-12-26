package org.jpwh.model.querying;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Bid.class)
public abstract class Bid_ {

	public static volatile SingularAttribute<Bid, Item> item;
	public static volatile SingularAttribute<Bid, BigDecimal> amount;
	public static volatile SingularAttribute<Bid, User> bidder;
	public static volatile SingularAttribute<Bid, Long> id;

}

