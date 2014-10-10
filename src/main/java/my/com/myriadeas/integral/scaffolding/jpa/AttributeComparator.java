package my.com.myriadeas.integral.scaffolding.jpa;

import java.util.Comparator;

import javax.persistence.metamodel.Attribute;

public class AttributeComparator implements Comparator<Attribute<?, ?>> {

	@Override
	public int compare(Attribute<?, ?> o1, Attribute<?, ?> o2) {
		return o1.getName().compareTo(o2.getName());
	}

}
