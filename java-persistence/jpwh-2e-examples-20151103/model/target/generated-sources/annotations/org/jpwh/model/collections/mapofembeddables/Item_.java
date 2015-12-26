package org.jpwh.model.collections.mapofembeddables;

import javax.annotation.Generated;
import javax.persistence.metamodel.MapAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Item.class)
public abstract class Item_ {

	public static volatile MapAttribute<Item, Filename, Image> images;
	public static volatile SingularAttribute<Item, Long> id;

}

