package org.jpwh.model.advanced;

import java.math.BigDecimal;
import java.sql.Blob;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Item.class)
public abstract class Item_ {

	public static volatile SingularAttribute<Item, byte[]> image;
	public static volatile SingularAttribute<Item, BigDecimal> averageBidAmount;
	public static volatile SingularAttribute<Item, BigDecimal> initialPrice;
	public static volatile SingularAttribute<Item, Boolean> verified;
	public static volatile SingularAttribute<Item, String> description;
	public static volatile SingularAttribute<Item, String> shortDescription;
	public static volatile SingularAttribute<Item, Date> createdOn;
	public static volatile SingularAttribute<Item, Double> metricWeight;
	public static volatile SingularAttribute<Item, Blob> imageBlob;
	public static volatile SingularAttribute<Item, String> name;
	public static volatile SingularAttribute<Item, Long> id;
	public static volatile SingularAttribute<Item, AuctionType> auctionType;
	public static volatile SingularAttribute<Item, Date> lastModified;

}

