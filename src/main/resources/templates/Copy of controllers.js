'use strict';
#foreach($entity in $listOfEntities)
function ${entity.name}ListCtrl($dialog, $scope, $route, $routeParams, $location, $log, Restangular, Notification, ConfirmDeleteDialog, Localization) {
    $scope.$on('ListSelectedItems', function(event, selectedItems){	
		$scope.mySelections = selectedItems;
		
		if (selectedItems.length > 0){
    		$scope.isRecordSelected = true;
    	} else {
    		$scope.isRecordSelected = false;
    	}
    });
	
	
	$scope.view = function() {
    	if ($scope.mySelections.length > 0){
    		var itemSelected = [];
    		angular.forEach($scope.mySelections, function(item){
    			itemSelected.push(item.id); 
    		});
    		
			$.each(itemSelected, function(index, value) {
	    		var url = "#/${entity.name.toLowerCase()}/" + value; 
	    		window.open(url, "_blank");
			});
			
		}else{
			alert('No record selected!');
		}
    }
	
	$scope.edit = function() {
    	if ($scope.mySelections.length > 0){
    		var itemSelected = [];
    		angular.forEach($scope.mySelections, function(item){
    			itemSelected.push(item.id); 
    		});
    		
			$.each(itemSelected, function(index, value) {
	    		var url = "#/${entity.name.toLowerCase()}/edit/" + value; 
	    		window.open(url, "_blank");
			});
			
		}else{
			alert('No record selected!');
		}
    }
	
	
    $scope.delete = function(id) {
    	ConfirmDeleteDialog.prompt($dialog).open()
    	.then(function(result){
    		if (result) {
    			Restangular.one('${entity.name.toLowerCase()}', id).remove().then(function(response){
    				Notification.success(Localization.resolve("${entity.name.toLowerCase()}.deleted", "Successfully deleted ${entity.name}."));
    				$route.reload();
    			}, function(errorResponse){
    				Notification.error(Localization.resolve("${entity.name.toLowerCase()}.deleted.failed","Failed to delete ${entity.name}.")_;
    			});
	         }
    	});
    }
	
}

function ${entity.name}EditCtrl($scope, $dialog, $log, $routeParams, $location, Restangular, Notification, ErrorBinder, SelectionLookup, Lookup, ConfirmDeleteDialog, Localization) {
	var ${entity.name.toLowerCase()}Rest = Restangular.one('${entity.name.toLowerCase()}', $routeParams.id);
	var selectionLookups = {
		"rest" : ${entity.name.toLowerCase()}Rest,
		"name" : "${entity.name.toLowerCase()}",
		"$scope" : $scope,
		"properties" : [
#foreach($attribute in $entity.attributes)
		#if($renderEditor.isSelection($attribute))
		{ 
			"name" : "${attribute.name}",
			"domain" : "${attribute.javaType.simpleName.toLowerCase()}"
		},
		#end
#end	]
	};
	var idLookups = {
			"rest" : ${entity.name.toLowerCase()}Rest,
			"name" : "${entity.name.toLowerCase()}",
			"$scope" : $scope,
			"properties" : [
	#foreach($attribute in $entity.attributes)
			#if($renderEditor.isSearchBox($attribute)){ 
			
				"name" : "${attribute.name}",
				"domain" : "${attribute.javaType.simpleName.toLowerCase()}"
			
			},
			#end
	#end	]
		};
	${entity.name.toLowerCase()}Rest.get().then(function(response){
	   var ${entity.name.toLowerCase()} = response;
	   $scope.${entity.name.toLowerCase()} = ${entity.name.toLowerCase()};
	   SelectionLookup.registerPropertyListener(selectionLookups);
       SelectionLookup.loadPropertyList(selectionLookups);
       Lookup.loadPropertyDomain(selectionLookups);
	}, function(errorResponse) {
        Notification.success(Localization.resolve("${entity.name.toLowerCase()}.notfound", "Failed to load ${entity.name}."));
	});
	
	$scope.save = function() {
		ErrorBinder.reset($scope.form${entity.name});
        $scope.${entity.name.toLowerCase()}.put().then(function(response){
            Notification.success(Localization.resolve("${entity.name.toLowerCase()}.updated", "Successfully updated ${entity.name}."));
        }, function(errorResponse){
			Notification.error(Localization.resolve("${entity.name.toLowerCase()}.updated.failed", "Failed to update ${entity.name}."));
			ErrorBinder.bind($scope.form${entity.name}, errorResponse);
        });
	}
	
	$scope.delete = function(${entity.name.toLowerCase()}) {
		var id = ${entity.name.toLowerCase()}.id;
		ConfirmDeleteDialog.prompt($dialog).open()
    	.then(function(result){
    		if (result) {
    			Restangular.one('${entity.name.toLowerCase()}', id).remove().then(function(response){
    				Notification.success(Localization.resolve("${entity.name.toLowerCase()}.deleted","Successfully deleted ${entity.name}."));
    				var path = '/${entity.name.toLowerCase()}/';
    				$location.path(path);
    			}, function(errorResponse){
    				Notification.error(Localization.resolve("${entity.name.toLowerCase()}.deleted.failed","Failed to delete ${entity.name}."));
    			});
	         }
    	});
    }
	
}

function ${entity.name}ViewCtrl($dialog, $scope, $routeParams, $location, Restangular, Notification, ErrorBinder, SelectionLookup, Lookup, ConfirmDeleteDialog, DomainUtils) {
	var ${entity.name.toLowerCase()}Rest = Restangular.one('${entity.name.toLowerCase()}', $routeParams.id);
	var selectionLookups = {
		"rest" : ${entity.name.toLowerCase()}Rest,
		"name" : "${entity.name.toLowerCase()}",
		"$scope" : $scope,
		"properties" : [
#foreach($attribute in $entity.attributes)
		#if($renderEditor.isSelection($attribute))
		{ 
			"name" : "${attribute.name}",
			"domain" : "${attribute.javaType.simpleName.toLowerCase()}"
		},
		#end
#end	]
	};
	var idLookups = {
			"rest" : ${entity.name.toLowerCase()}Rest,
			"name" : "${entity.name.toLowerCase()}",
			"$scope" : $scope,
			"properties" : [
	#foreach($attribute in $entity.attributes)
			#if($renderEditor.isSearchBox($attribute)){ 
			
				"name" : "${attribute.name}",
				"domain" : "${attribute.javaType.simpleName.toLowerCase()}"
			
			},
			#end
	#end	]
		};
	
	${entity.name.toLowerCase()}Rest.get().then(function(response){
	   var ${entity.name.toLowerCase()} = response;
	   $scope.${entity.name.toLowerCase()} = ${entity.name.toLowerCase()};
	   SelectionLookup.registerPropertyListener(selectionLookups);
       SelectionLookup.loadPropertyList(selectionLookups);
       Lookup.loadPropertyDomain(selectionLookups);
       $scope.generalInfo = DomainUtils.getGeneralInfo(${entity.name.toLowerCase()});
	}, function(errorResponse) {
        Notification.success(Localization.resolve("${entity.name.toLowerCase()}.notfound","Failed to load ${entity.name}."));
	});
	
	$scope.delete = function(${entity.name.toLowerCase()}) {
		var id = ${entity.name.toLowerCase()}.id;
		ConfirmDeleteDialog.prompt($dialog).open()
    	.then(function(result){
    		if (result) {
    			Restangular.one('${entity.name.toLowerCase()}', id).remove().then(function(response){
    				Notification.success(Localization.resolve("${entity.name.toLowerCase()}.deleted","Successfully deleted ${entity.name}."));
    				var path = '/${entity.name.toLowerCase()}/';
    				$location.path(path);
    			}, function(errorResponse){
    				Notification.error(Localization.resolve("${entity.name.toLowerCase()}.deleted.failed","Failed to delete ${entity.name}."));
    			});
	         }
    	});
    }
}

function ${entity.name}CreateCtrl($scope, $routeParams, $location, Restangular, Notification, ErrorBinder, SelectionLookup) {
	var ${entity.name.toLowerCase()} = {};
	$scope.${entity.name.toLowerCase()} = ${entity.name.toLowerCase()};
	var selectionLookups = {
		"name" : "${entity.name.toLowerCase()}",
		"$scope" : $scope,
		"properties" : [
#foreach($attribute in $entity.attributes)
		#if($renderEditor.isSelection($attribute))
		{
			"name" : "${attribute.name}",
			"domain" : "${attribute.javaType.simpleName.toLowerCase()}"
		},
		#end
#end	]
	};
	SelectionLookup.loadPropertyList(selectionLookups);
	
	$scope.save = function() {
        ErrorBinder.reset($scope.form${entity.name});
		Restangular.all('${entity.name.toLowerCase()}').post($scope.${entity.name.toLowerCase()}).then(function(response){
			var ${entity.name.toLowerCase()} = response;
			#set($locationPathMethod = "$location.path")  
			$locationPathMethod("/${entity.name.toLowerCase()}/" + ${entity.name.toLowerCase()}.id);
			Notification.success(Localization.resolve("${entity.name.toLowerCase()}.created","Successfully created ${entity.name}."));
		}, function(errorResponse){
			Notification.error(Localization.resolve("${entity.name.toLowerCase()}.created.failed","Failed to create ${entity.name}."));
			ErrorBinder.bind($scope.form${entity.name}, errorResponse);
		});
	}
}

#end