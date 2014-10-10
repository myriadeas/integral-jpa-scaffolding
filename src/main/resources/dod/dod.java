package my.com.myriadeas.integral.data.jpa.repositories.dod;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import ${entity.javaType.canonicalName};
import my.com.myriadeas.integral.data.jpa.repositories.impl.${entity.name}Repository;
import my.com.myriadeas.integral.data.populator.CustomPatronDataOnDemand;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service(value = "${entity.name.toLowerCase()}DataOnDemand")
public class ${entity.name}DataOnDemand implements InitializingBean {

	@Autowired
	ApplicationContext context;
	
	@Autowired
	${entity.name}Repository repository;

	private Random rnd = new SecureRandom();

	private List<${entity.name}> data;

	public ${entity.name} getNewTransient(int index) {
		${entity.name} obj = new ${entity.name}();
		#foreach($attribute in $entity.attributes)
		
        #if($attribute.name != "id")
			set$StringUtils.capitalizeFirstLetter(${attribute.name})(obj, index);
		#end
		#end
		return obj;
	}
	#foreach($attribute in $entity.attributes)
	
        #if($attribute.name != "id")
		private void set$StringUtils.capitalizeFirstLetter(${attribute.name})(${entity.name} obj, int index) {
            #if($attribute.javaType.isAssignableFrom($Patron))
        		obj.set$StringUtils.capitalizeFirstLetter($attribute.name)(context.getBean(CustomPatronDataOnDemand.class).getPatron());
            #end 
            #if(!$attribute.javaType.isAssignableFrom($Patron))
            	$renderEditor.renderPropertyEditor($entity, $attribute)
            #end
		}
		#end
	#end

	public ${entity.name} getSpecific(int index) {
		init();
		if (index < 0) {
			index = 0;
		}
		if (index > (data.size() - 1)) {
			index = data.size() - 1;
		}
		${entity.name} obj = data.get(index);
		Long id = obj.getId();
		return repository.findOne(id);
	}

	public ${entity.name} getRandom() {
		init();
		${entity.name} obj = data.get(rnd.nextInt(data.size()));
		Long id = obj.getId();
		return repository.findOne(id);
	}

	public void init() {
		int from = 0;
		int to = 10;
		PageRequest pageable = new PageRequest(from, to);
		data = repository.findAll(pageable).getContent();
		if (data == null) {
			throw new IllegalStateException(
					"Find entries implementation for '${entity.name}' illegally returned null");
		}
		if (!data.isEmpty()) {
			return;
		}

		data = new ArrayList<${entity.name}>();
		for (int i = 0; i < 10; i++) {
			${entity.name} obj = getNewTransient(i);
			try {
				repository.save(obj);
			} catch (ConstraintViolationException e) {
				StringBuilder msg = new StringBuilder();
				for (Iterator<ConstraintViolation<?>> iter = e
						.getConstraintViolations().iterator(); iter.hasNext();) {
					ConstraintViolation<?> cv = iter.next();
					msg.append("[").append(cv.getConstraintDescriptor())
							.append(":").append(cv.getMessage()).append("=")
							.append(cv.getInvalidValue()).append("]");
				}
				throw new RuntimeException(msg.toString(), e);
			}
			data.add(obj);
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		init();
	}
}
