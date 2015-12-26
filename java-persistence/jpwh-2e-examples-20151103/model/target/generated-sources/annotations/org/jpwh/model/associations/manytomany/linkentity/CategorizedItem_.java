package org.jpwh.model.associations.manytomany.linkentity;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.jpwh.model.associations.manytomany.linkentity.CategorizedItem.Id;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(CategorizedItem.class)
public abstract class CategorizedItem_ {

	public static volatile SingularAttribute<CategorizedItem, Item> item;
	public static volatile SingularAttribute<CategorizedItem, String> addedBy;
	public static volatile SingularAttribute<CategorizedItem, Id> id;
	public static volatile SingularAttribute<CategorizedItem, Category> category;
	public static volatile SingularAttribute<CategorizedItem, Date> addedOn;

}

