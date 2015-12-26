package org.jpwh.model.associations.manytomany.ternary;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(CategorizedItem.class)
public abstract class CategorizedItem_ {

	public static volatile SingularAttribute<CategorizedItem, Item> item;
	public static volatile SingularAttribute<CategorizedItem, User> addedBy;
	public static volatile SingularAttribute<CategorizedItem, Date> addedOn;

}

