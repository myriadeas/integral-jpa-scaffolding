'use strict';

/* Services */
integralApp.factory('Notification', function () {
    return {
        success: function (message) {
            toastr.success(message);
        },
        error: function (message) {
            toastr.error(message);
        }
    };
});

integralApp.factory('ErrorBinder', function () {
    return {
        bind: function (form, errorResponse) {
            if(errorResponse.status == 400) {
                this.constraints(form, errorResponse.data.messages);
            }
            if(errorResponse.status == 409) {
                this.conflict(form, errorResponse);
            }
        },
        constraints: function(form, messages) {
            $.each(messages, function(key, message) {
                var formInputName = 'input_' + message.entity + '.' + message.property;
                form[formInputName].$setValidity('constraint', false);
                form[formInputName].$error['constraintObject'] = message;
            });
        },
        conflict: function(form, errorResponse) {
            form.$setValidity('uniqueConstraint', false);
        },
        reset: function(form) {
            form.$setValidity('uniqueConstraint', true);
            if(!angular.isUndefined(form.$error.constraint)) {
                $.each(form.$error.constraint, function(key, error){
                    form.$error.constraint[0].$setValidity('constraint', true);
                });
            }
            
        }
    };
});

integralApp.factory('Lookup', function (Restangular) {
    return {
    	loadDefaultList: function(lookups){
    		var $scope = lookups.$scope;
            $.each(lookups.properties, function(key, property){
            	var propertyDomain = property.domain;
            	Restangular.all(propertyDomain).getList().then(function(response){
            		$scope[propertyDomain + 's'] = response.content;
            	}, function(errorResponse){
            	});
            });
    	},
    	loadId: function (lookups) {
    		this.loadSelection(lookups, true);
    	},    
        loadSelection: function (lookups, lookupId) {
           var rest = lookups.rest;
           var $scope = lookups.$scope;
           var domain = $scope[lookups.name];
           $.each(lookups.properties, function(key, property){
        	   var propertyName = property.name;
        	   var propertyDomain = property.domain;
        	   rest.one(propertyName).get().then(function(response){
        		   domain[propertyName] = response.content;
         	       if(!lookupId) {
	         	       Restangular.all(propertyDomain).getList().then(function(response){
	           		   		var associationDomains = response.content;
	           				$scope[propertyDomain + 's'] = associationDomains;
	           		          $.each(associationDomains, function(key, associationDomain){
	           	                  if(associationDomain.id == domain[propertyName].id) {
	           	                	 domain[propertyName] = associationDomain;
	           	                  }
	           	              });
	           			}, function(errorResponse){
	           				
	           			});   
         	       }
               }, function(errorResponse) {
             	   Restangular.all(propertyDomain).getList().then(function(response){
         		   		var associationDomains = response.content;
         		   		$scope[propertyDomain + 's'] = associationDomains;
         		    }, function(errorResponse){	
         			});
                });
           });
        }
    };
});

integralApp.factory('ConfirmDeleteDialog', function($dialog) {
	return {
		prompt: function() {
			var title = 'Confirm delete';
			var msg = 'Are you sure you want to delete this record?';
			var btns = [{result:false, label: 'Cancel'}, {result:true, label: 'OK', cssClass: 'btn-primary'}];
		    return $dialog.messageBox(title, msg, btns)
		}
	};
});

integralApp.factory('Localization', function (localize) {
    return {
        resolve: function (originalString, defaultString) {
        	var newString = defaultString;
        	try{
	        	newString = localize.getLocalizedString(originalString);  	
        	} catch (error){
        		
        	}
            return newString;
        }
    };
});

integralApp.factory('Search', function ($dialog, $log) {
    return {
    	openSearchDialog: function(property, searchDomain, $scope){	
    		var opts = {		
    				backdrop: true,
    				keyboard: true,
    				backdropClick: true,
    				modalClass: 'modal-scroll-wrap',
    				// template: t,
    				templateUrl: 'partials/' + searchDomain + '/searchDialog.html',
    				controller: 'SearchDialogController',
    				domain: searchDomain
    		};	
    		
    		var d = $dialog.dialog(opts);	    
    	    d.open().then(function(selected){
    	      if(selected){
    	    	 $scope[property.split('.')[0]][property.split('.')[1]] = selected;
    	      }
    	    });    	    
    	}
    
    	
    };
});
