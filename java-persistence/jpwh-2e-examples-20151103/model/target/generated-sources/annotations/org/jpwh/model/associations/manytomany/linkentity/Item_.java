package org.jpwh.model.associations.manytomany.linkentity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Item.class)
public abstract class Item_ {

	public static volatile SingularAttribute<Item, String> name;
	public static volatile SingularAttribute<Item, Long> id;
	public static volatile SetAttribute<Item, CategorizedItem> categorizedItems;

}

