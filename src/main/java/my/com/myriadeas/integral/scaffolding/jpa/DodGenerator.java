package my.com.myriadeas.integral.scaffolding.jpa;

import static org.junit.Assert.assertNotNull;

import java.util.Set;

import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EntityType;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import my.com.myriadeas.integral.data.jpa.domain.Patron;
import my.com.myriadeas.jpa.JavaDodRenderEditor;
import my.com.myriadeas.jpa.TemplateGenerator;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.util.StringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

@ContextConfiguration("classpath:application-context-integral.xml")
public class DodGenerator extends AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean entityManagerFactory;

	private ValidatorFactory validatorFactory = Validation
			.buildDefaultValidatorFactory();

	@Test
	public void testEntityManagerFactory() {
		assertNotNull(entityManagerFactory);
		assertNotNull(validatorFactory);
		for (EntityType<?> entity : entityManagerFactory.nativeEntityManagerFactory
				.getMetamodel().getEntities()) {
			System.out.println(entity.getJavaType());
			for (Attribute<?, ?> attribute : entity.getAttributes()) {
				System.out.println("field=" + attribute.getName() + ", type= "
						+ attribute.getJavaType());
			}
		}
	}

	@Test
	public void testGenerateDod() {
		generateDod(entityManagerFactory.nativeEntityManagerFactory
				.getMetamodel().getEntities());
	}

	@Test
	public void testGenerateDodTest() {
		generateDodTest(entityManagerFactory.nativeEntityManagerFactory
				.getMetamodel().getEntities());
	}

	public void generateDod(Set<EntityType<?>> entities) {
		for (EntityType<?> entity : entities) {
			generateDod(entity);
		}
	}

	public void generateDod(EntityType<?> entity) {
		String template = "src/main/resources/dod/dod.java";
		String baseOutput = "generated/src/main/java/my/com/myriadeas/integral/data/jpa/repositories/dod/";
		VelocityContext context = new VelocityContext();
		JavaDodRenderEditor renderEditor = new JavaDodRenderEditor(
				entityManagerFactory.nativeEntityManagerFactory.getMetamodel());
		String output = baseOutput + entity.getName() + "DataOnDemand.java";
		context.put("entity", entity);
		context.put("StringUtils", StringUtils.class);
		context.put("renderEditor", renderEditor);
		context.put("Patron", Patron.class);
		TemplateGenerator.generateVelocity(template, context, output);
	}

	public void generateDodTest(Set<EntityType<?>> entities) {
		for (EntityType<?> entity : entities) {
			generateDodTest(entity);
		}
	}

	public void generateDodTest(EntityType<?> entity) {
		String template = "src/main/resources/dod/dodTest.java";
		String baseOutput = "generated/src/test/java/my/com/myriadeas/integral/data/jpa/repositories/dod/";
		VelocityContext context = new VelocityContext();
		JavaDodRenderEditor renderEditor = new JavaDodRenderEditor(
				entityManagerFactory.nativeEntityManagerFactory.getMetamodel());
		String output = baseOutput + entity.getName() + "DataOnDemandTest.java";
		context.put("entity", entity);
		TemplateGenerator.generateVelocity(template, context, output);
	}
}
