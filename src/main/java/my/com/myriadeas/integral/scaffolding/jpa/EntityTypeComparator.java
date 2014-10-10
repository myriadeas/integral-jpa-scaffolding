package my.com.myriadeas.integral.scaffolding.jpa;

import java.util.Comparator;

import javax.persistence.metamodel.EntityType;

public class EntityTypeComparator implements Comparator<EntityType<?>> {

	@Override
	public int compare(EntityType<?> o1, EntityType<?> o2) {
		return o1.getName().compareTo(o2.getName());
	}

}
