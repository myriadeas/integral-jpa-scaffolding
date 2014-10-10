package my.com.myriadeas.integral.scaffolding.jpa;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;

import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EntityType;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import my.com.myriadeas.jpa.Render;
import my.com.myriadeas.jpa.RenderDisplay;
import my.com.myriadeas.jpa.RenderEditor;
import my.com.myriadeas.jpa.RenderViewDisplay;
import my.com.myriadeas.jpa.TemplateGenerator;

import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.VelocityContext;
import org.atteo.evo.inflector.English;
import org.junit.Test;
import org.hibernate.ejb.metamodel.EntityTypeImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

@ContextConfiguration("classpath:application-context-integral.xml")
public class Generator extends AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean entityManagerFactory;

	private ValidatorFactory validatorFactory = Validation
			.buildDefaultValidatorFactory();

	@Test
	public void testEntityManagerFactory() {
		assertNotNull(entityManagerFactory);
		assertNotNull(validatorFactory);

		Set<EntityType<?>> entities = entityManagerFactory.nativeEntityManagerFactory
				.getMetamodel().getEntities();

		System.out.println(entities.getClass());
		for (EntityType<?> entity : entityManagerFactory.nativeEntityManagerFactory
				.getMetamodel().getEntities()) {
			System.out.println(entity.getJavaType());
			EntityTypeImpl entityImpl = (EntityTypeImpl)entity;
			Set<Attribute<?, ?>>  sortedAttributes = entityImpl.getSortedAttributes();
			for (Attribute<?, ?> attribute : sortedAttributes) {
				
				System.out.println("field=" + attribute.getName() + ", type= "
						+ attribute.getJavaType());
			}
		}
	}

	@Test
	public void testGenerateAppJs() {
		generateAppJs(entityManagerFactory.nativeEntityManagerFactory
				.getMetamodel().getEntities());
	}
	
	@Test
	public void testGenerateCrud() {
		testGenerateFormHtml();
		testGenerateViewHtml();
		testGenerateMessagesProperties();
	}

	// @Test
	public void testGenerateServicesJs() {
		// No longer need to generate service js
		/*-
		generateServicesJs(entityManagerFactory.nativeEntityManagerFactory
				.getMetamodel().getEntities());
		 */
	}

	@Test
	public void testGenerateControllersJs() {
		generateControllersJs(entityManagerFactory.nativeEntityManagerFactory
				.getMetamodel().getEntities());
	}

	@Test
	public void testGenerateCreateHtml() {
		generateCreateHtml(entityManagerFactory.nativeEntityManagerFactory
				.getMetamodel().getEntities());
	}

	@Test
	public void testGenerateEditHtml() {
		generateEditHtml(entityManagerFactory.nativeEntityManagerFactory
				.getMetamodel().getEntities());
	}
	

	@Test
	public void testGenerateFormHtml() {
		generateFormHtml(entityManagerFactory.nativeEntityManagerFactory
				.getMetamodel().getEntities());
	}

	@Test
	public void testGenerateViewHtml() {
		generateViewHtml(entityManagerFactory.nativeEntityManagerFactory
				.getMetamodel().getEntities());
	}

	@Test
	public void testGenerateListHtml() {
		generateListHtml(entityManagerFactory.nativeEntityManagerFactory
				.getMetamodel().getEntities());
	}

	@Test
	public void testGenerateUiProperties() {
		generateUiProperties(entityManagerFactory.nativeEntityManagerFactory
				.getMetamodel().getEntities());
	}

	@Test
	public void testGenerateMessagesProperties() {
		StringUtils.splitByCharacterTypeCamelCase("");
		generateMessagesProperties(sortEntitiesAndAttributes(entityManagerFactory.nativeEntityManagerFactory
				.getMetamodel().getEntities()));
	}

	public void generateAppJs(Set<EntityType<?>> entities) {
		String template = "src/main/resources/templates/app.js";
		String baseOutput = "generated/js/";
		VelocityContext context = new VelocityContext();
		String output = baseOutput + "app.js";
		List<EntityType<?>> listOfEntities = new ArrayList<EntityType<?>>();
		for (EntityType<?> entity : entities) {
			listOfEntities.add(entity);
		}
		context.put("entities", entities);
		context.put("listOfEntities", listOfEntities);
		TemplateGenerator.generateVelocity(template, context, output);
	}

	public void generateServicesJs(Set<EntityType<?>> entities) {
		String template = "src/main/resources/templates/services.js";
		String baseOutput = "generated/js/";
		VelocityContext context = new VelocityContext();
		String output = baseOutput + "services.js";
		List<EntityType<?>> listOfEntities = new ArrayList<EntityType<?>>();
		for (EntityType<?> entity : entities) {
			listOfEntities.add(entity);
		}
		context.put("entities", entities);
		context.put("listOfEntities", listOfEntities);
		TemplateGenerator.generateVelocity(template, context, output);
	}

	public void generateControllersJs(Set<EntityType<?>> entities) {
		String template = "src/main/resources/templates/controllers.js";
		String baseOutput = "generated/js/controllers/";
		VelocityContext context = new VelocityContext();
		List<EntityType<?>> listOfEntities = new ArrayList<EntityType<?>>();
		for (EntityType<?> entity : entities) {
			String output = baseOutput + entity.getName().toLowerCase() + "Controllers.js";
			// listOfEntities.add(entity);
			context.put("entity", entity);
			Render renderEditor = new RenderEditor(
					entityManagerFactory.nativeEntityManagerFactory
							.getMetamodel());
			context.put("renderEditor", renderEditor);
			TemplateGenerator.generateVelocity(template, context, output);
		}
		// context.put("entities", entities);
		// context.put("listOfEntities", listOfEntities);

	}

	public void generateCreateHtml(Set<EntityType<?>> entities) {
		for (EntityType<?> entity : entities) {
			generateCreateHtml(entity);
		}
	}

	public void generateCreateHtml(EntityType<?> entity) {
		String template = "src/main/resources/templates/create.html";
		String baseOutput = "generated/partials/";
		VelocityContext context = new VelocityContext();
		String output = baseOutput + entity.getName().toLowerCase() + "/"
				+ "create.html";
		context.put("entity", entity);
		Render renderEditor = new RenderEditor(
				entityManagerFactory.nativeEntityManagerFactory.getMetamodel());
		context.put("renderEditor", renderEditor);

		TemplateGenerator.generateVelocity(template, context, output);
	}

	public void generateEditHtml(Set<EntityType<?>> entities) {
		for (EntityType<?> entity : entities) {
			generateEditHtml(entity);
		}
	}

	public void generateEditHtml(EntityType<?> entity) {
		String template = "src/main/resources/templates/edit.html";
		String baseOutput = "generated/partials/";
		VelocityContext context = new VelocityContext();
		Render renderEditor = new RenderEditor(
				entityManagerFactory.nativeEntityManagerFactory.getMetamodel());
		String output = baseOutput + entity.getName().toLowerCase() + "/"
				+ "edit.html";
		context.put("entity", entity);
		context.put("renderEditor", renderEditor);
		TemplateGenerator.generateVelocity(template, context, output);
	}
	

	public void generateFormHtml(Set<EntityType<?>> entities) {
		for (EntityType<?> entity : entities) {
			generateFormHtml(entity);
		}
	}
	

	public void generateFormHtml(EntityType<?> entity) {
		String template = "src/main/resources/templates/form.html";
		String baseOutput = "generated/views/repository/";
		VelocityContext context = new VelocityContext();
		Render renderEditor = new RenderEditor(
				entityManagerFactory.nativeEntityManagerFactory.getMetamodel());
		String output = baseOutput + entity.getName()
				+ "/form.html";
		context.put("entity", entity);
		context.put("renderEditor", renderEditor);
		TemplateGenerator.generateVelocity(template, context, output);
	}

	public void generateViewHtml(Set<EntityType<?>> entities) {
		for (EntityType<?> entity : entities) {
			generateViewHtml(entity);
		}
	}

	public void generateViewHtml(EntityType<?> entity) {
		String template = "src/main/resources/templates/view.html";
		String baseOutput = "generated/views/repository/";
		VelocityContext context = new VelocityContext();
		RenderViewDisplay renderViewDisplay = new RenderViewDisplay(
				entityManagerFactory.nativeEntityManagerFactory.getMetamodel());
		String output = baseOutput + entity.getName().toLowerCase()
				+ "/view.html";
		context.put("entity", entity);
		context.put("renderViewDisplay", renderViewDisplay);
		TemplateGenerator.generateVelocity(template, context, output);
	}

	public void generateListHtml(Set<EntityType<?>> entities) {
		for (EntityType<?> entity : entities) {
			generateListHtml(entity);
		}
	}

	public void generateListHtml(EntityType<?> entity) {
		String template = "src/main/resources/templates/list.html";
		String baseOutput = "generated/partials/";
		VelocityContext context = new VelocityContext();
		RenderDisplay renderDisplay = new RenderDisplay(
				entityManagerFactory.nativeEntityManagerFactory.getMetamodel());
		String output = baseOutput + entity.getName().toLowerCase() + "/"
				+ "list.html";
		context.put("entity", entity);
		context.put("renderDisplay", renderDisplay);
		TemplateGenerator.generateVelocity(template, context, output);
	}

	// TODO do we really need this at server?
	public void generateMessagesProperties(Set<EntityType<?>> entities) {
		String template = "src/main/resources/templates/messages.properties";
		String baseOutput = "generated/";
		VelocityContext context = new VelocityContext();
		RenderDisplay renderDisplay = new RenderDisplay(
				entityManagerFactory.nativeEntityManagerFactory.getMetamodel());
		String output = baseOutput + "messages.properties";
		context.put("entities", entities);
		context.put("renderDisplay", renderDisplay);
		context.put("entitiesAttributesMap", getEntityAttributesMap(entities));
		TemplateGenerator.generateVelocity(template, context, output);
	}

	public void generateUiProperties(Set<EntityType<?>> entities) {
		String template = "src/main/resources/templates/ui.json";
		String baseOutput = "generated/";
		VelocityContext context = new VelocityContext();
		RenderEditor renderEditor = new RenderEditor(
				entityManagerFactory.nativeEntityManagerFactory.getMetamodel());
		String output = baseOutput + "ui.json";
		context.put("entities", entities);
		context.put("renderEditor", renderEditor);
		TemplateGenerator.generateVelocity(template, context, output);
	}

	public Set<EntityType<?>> sortEntitiesAndAttributes(
			Set<EntityType<?>> entities) {
		for (EntityType<?> entityType : entities) {
			Set<? extends Attribute<? super Object, ?>> sortedAttributes = sortAttributes(entityType
					.getAttributes());
		}
		return sortEntities(entities);
	}

	public Set<EntityType<?>> sortEntities(Set<EntityType<?>> entities) {
		Set<EntityType<?>> treeSet = new TreeSet<EntityType<?>>(
				new EntityTypeComparator());
		treeSet.addAll(entities);
		return treeSet;
	}

	public Map<String, Set<?>> getEntityAttributesMap(
			Set<EntityType<?>> entities) {
		Map<String, Set<?>> entityAttributesMap = new HashMap<String, Set<?>>();
		for (EntityType<?> entityType : entities) {
			Set<? extends Attribute<? super Object, ?>> sortedAttributes = sortAttributes(entityType
					.getAttributes());
			entityAttributesMap.put(
					entityType.getJavaType().getCanonicalName(),
					sortedAttributes);
		}
		return entityAttributesMap;
	}

	@SuppressWarnings("unchecked")
	public Set<? extends Attribute<? super Object, ?>> sortAttributes(
			Set<? extends Attribute<?, ?>> set) {
		Set<Attribute<?, ?>> treeSet = new TreeSet<Attribute<?, ?>>(
				new AttributeComparator());
		treeSet.addAll(set);
		return (Set<? extends Attribute<? super Object, ?>>) treeSet;
	}
}
