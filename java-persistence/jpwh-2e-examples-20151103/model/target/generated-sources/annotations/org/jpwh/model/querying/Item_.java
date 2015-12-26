package org.jpwh.model.querying;

import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Item.class)
public abstract class Item_ {

	public static volatile SingularAttribute<Item, User> seller;
	public static volatile SingularAttribute<Item, Boolean> approved;
	public static volatile SetAttribute<Item, Image> images;
	public static volatile SingularAttribute<Item, Date> auctionEnd;
	public static volatile SingularAttribute<Item, String> name;
	public static volatile SetAttribute<Item, Bid> bids;
	public static volatile SingularAttribute<Item, Long> id;
	public static volatile SingularAttribute<Item, AuctionType> auctionType;
	public static volatile SingularAttribute<Item, BigDecimal> buyNowPrice;
	public static volatile SetAttribute<Item, Category> categories;
	public static volatile SingularAttribute<Item, Date> createdOn;

}

