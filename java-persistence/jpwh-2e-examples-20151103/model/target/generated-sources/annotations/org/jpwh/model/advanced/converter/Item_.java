package org.jpwh.model.advanced.converter;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.jpwh.model.advanced.MonetaryAmount;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Item.class)
public abstract class Item_ {

	public static volatile SingularAttribute<Item, String> name;
	public static volatile SingularAttribute<Item, Long> id;
	public static volatile SingularAttribute<Item, MonetaryAmount> buyNowPrice;
	public static volatile SingularAttribute<Item, Date> createdOn;

}

