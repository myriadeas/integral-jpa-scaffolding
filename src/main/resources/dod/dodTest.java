package my.com.myriadeas.integral.data.jpa.repositories.dod;

import static org.junit.Assert.*;

import my.com.myriadeas.dao.jpa.AbstractDataOnDemandTest;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class  ${entity.name}DataOnDemandTest extends AbstractDataOnDemandTest {

	@Autowired
	${entity.name}DataOnDemand dod;

	@Test
	public void test() {
		assertNotNull(dod);
		assertNotNull(dod.getRandom());
	}

}
