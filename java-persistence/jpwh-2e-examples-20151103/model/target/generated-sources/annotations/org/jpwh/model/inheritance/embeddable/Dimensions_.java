package org.jpwh.model.inheritance.embeddable;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Dimensions.class)
public abstract class Dimensions_ extends org.jpwh.model.inheritance.embeddable.Measurement_ {

	public static volatile SingularAttribute<Dimensions, BigDecimal> depth;
	public static volatile SingularAttribute<Dimensions, BigDecimal> width;
	public static volatile SingularAttribute<Dimensions, BigDecimal> height;

}

