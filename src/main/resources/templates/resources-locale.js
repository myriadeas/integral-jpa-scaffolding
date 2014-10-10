[
		 	{
		        "key":"my.com.myriadeas.dao.domain.listOfModule",
		        "value":"List Of Module"
		    },  
	#foreach($property in $properties.entrySet())	
		 #if ($foreach.count == $properties.entrySet().size())
		 	{
		        "key":"${property.key}",
		        "value":"${property.value}"	
		    }
		#else
			{
		        "key":"${property.key}",
		        "value":"${property.value}"	
		    },
		#end	
	#end
]