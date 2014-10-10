package my.com.myriadeas.integral.scaffolding.jpa;

import java.util.HashMap;
import java.util.Map;

public class DomainAttributeSequences {

	private static Map<String, Integer> domainAttributeSequences = new HashMap<String, Integer>();

	private static int defaultIndex = -1000;

	static {

		// 1
		String entity = "Department";
		addSequence(entity, "code", 1000);
		addSequence(entity, "description", 900);
		addSequence(entity, "address1", 800);
		addSequence(entity, "address2", 700);
		addSequence(entity, "address3", 600);
		addSequence(entity, "postcode", 500);
		addSequence(entity, "town", 400);
		addSequence(entity, "telephone", 300);
		addSequence(entity, "departmentHead", 200);
		addSequence(entity, "fax", 100);

		// 2
		entity = "PatronCategory";
		addSequence(entity, "code", 1000);
		addSequence(entity, "description", 900);

		// 3
		entity = "Designation";
		addSequence(entity, "code", 1000);
		addSequence(entity, "description", 900);

		// 4
		entity = "Location";
		addSequence(entity, "code", 2000);
		addSequence(entity, "description", 1900);
		addSequence(entity, "branch", 1800);
		// 5
		entity = "Race";
		addSequence(entity, "code", 1000);
		addSequence(entity, "description", 900);

		// 6
		entity = "Religion";
		addSequence(entity, "code", 1000);
		addSequence(entity, "description", 900);

		// 7
		entity = "Title";
		addSequence(entity, "code", 1000);
		addSequence(entity, "description", 900);

		// 8
		entity = "Town";
		addSequence(entity, "code", 1000);
		addSequence(entity, "description", 900);

		// 9
		entity = "ForeignExchange";
		addSequence(entity, "forex", 1000);
		addSequence(entity, "brate", 900);
		addSequence(entity, "pdate", 800);
		addSequence(entity, "description", 700);
		addSequence(entity, "bdate", 600);
		addSequence(entity, "prate", 500);

		// 10
		entity = "ItemCategory";
		addSequence(entity, "code", 1000);
		addSequence(entity, "description", 900);
		addSequence(entity, "unit", 800);
		addSequence(entity, "icat", 700);
		addSequence(entity, "reservec", 600);

		// 11
		entity = "SMD";
		addSequence(entity, "code", 1000);
		addSequence(entity, "description", 900);

		// 12
		entity = "ItemEligibility";
		addSequence(entity, "code", 1000);
		addSequence(entity, "description", 900);
		addSequence(entity, "criteria", 800);
		addSequence(entity, "maxLoanAllowed", 700);

		// 13
		entity = "Branch";
		addSequence(entity, "code", 1000);
		addSequence(entity, "description", 900);
		addSequence(entity, "address1", 800);
		addSequence(entity, "address2", 700);
		addSequence(entity, "address3", 600);
		addSequence(entity, "postcode", 500);
		addSequence(entity, "town", 400);

		// 12
		entity = "PatronEligibility";
		addSequence(entity, "criteria", 1000);
		addSequence(entity, "code", 900);
		addSequence(entity, "description", 800);
		addSequence(entity, "allowOverdue", 700);
		addSequence(entity, "allowReserve", 600);
		addSequence(entity, "finesLimit", 500);
		addSequence(entity, "maxFines", 400);
		addSequence(entity, "maxLoanAllowed", 300);
		addSequence(entity, "maxReservationAllowed", 200);

		// 12
		entity = "PatronItemEligibility";
		addSequence(entity, "criteria", 1000);
		addSequence(entity, "code", 900);
		addSequence(entity, "description", 800);
		addSequence(entity, "allowOverdue", 700);
		addSequence(entity, "allowReserve", 600);
		addSequence(entity, "includeFines", 500);
		addSequence(entity, "maxLoanAllowed", 400);
		addSequence(entity, "maxFines", 300);
		addSequence(entity, "loanPeriod", 200);
		addSequence(entity, "loanUnit", 100);
		addSequence(entity, "maxRenewAllowed", 0);
		addSequence(entity, "multiplyFinesOfLost", -100);
		addSequence(entity, "fines", -200);

		// 13
		entity = "Item";
		addSequence(entity, "itemIdentifier", 1000);
		addSequence(entity, "material", 900);
		addSequence(entity, "location", 800);
		addSequence(entity, "itemCategory", 700);
		addSequence(entity, "smd", 600);
		addSequence(entity, "condition", 500);
		addSequence(entity, "volume", 400);
		addSequence(entity, "copyNumber", 300);
		addSequence(entity, "foreignCost", 200);
		addSequence(entity, "localCost", 100);

		// 14
		entity = "Officer";
		addSequence(entity, "username", 1100);
		addSequence(entity, "firstname", 1000);
		addSequence(entity, "lastname", 900);
		addSequence(entity, "password", 890);
		addSequence(entity, "userGroups", 880);
		addSequence(entity, "branch", 875);
		addSequence(entity, "dateOfBirth", 871);
		addSequence(entity, "newIC", 870);
		addSequence(entity, "race", 865);
		addSequence(entity, "religion", 840);
		addSequence(entity, "gender", 830);
		addSequence(entity, "email", 800);
		addSequence(entity, "homeAddress", 655);
		addSequence(entity, "homePhone", 651);
		addSequence(entity, "mobilePhone", 650);
		addSequence(entity, "officeAddress", 649);
		addSequence(entity, "officePhone", 645);
		addSequence(entity, "roles", Integer.MIN_VALUE);
		addSequence(entity, "designation", 400);
		addSequence(entity, "department", 300);
		addSequence(entity, "title", 200);
	}

	private static void addSequence(String entity, String attribute,
			Integer weight) {

		domainAttributeSequences.put(entity + "." + attribute, weight);
	}

	public static Integer getWeight(String entity, String attribute) {
		String key = entity + "." + attribute;
		return domainAttributeSequences.containsKey(key) ? domainAttributeSequences
				.get(key) : -10000;
	}
}
