package org.jpwh.model.concurrency.version;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Item.class)
public abstract class Item_ {

	public static volatile SetAttribute<Item, String> images;
	public static volatile SingularAttribute<Item, String> name;
	public static volatile SingularAttribute<Item, Long> id;
	public static volatile SingularAttribute<Item, BigDecimal> buyNowPrice;
	public static volatile SingularAttribute<Item, Category> category;
	public static volatile SingularAttribute<Item, Long> version;

}

