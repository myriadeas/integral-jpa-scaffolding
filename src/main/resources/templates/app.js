'use strict';

/* App Module */

var integralApp = angular.module('integralweb', ['ui.bootstrap', 'restangular','localization', 'ngGrid']).
  config(['$routeProvider', function($routeProvider) {
  $routeProvider.
      when('/home', {templateUrl: 'home.html',   controller: EntityListCtrl}).
      when('/circulation/charge', {templateUrl: 'partials/circulation/circulation.html', controller: CirculationCtrl}).
#foreach($entity in $listOfEntities)
      when('/${entity.name.toLowerCase()}/', {templateUrl: 'partials/${entity.name.toLowerCase()}/list.html',   controller: ${entity.name}ListCtrl}).
      when('/${entity.name.toLowerCase()}/create', {templateUrl: 'partials/${entity.name.toLowerCase()}/create.html',   controller: ${entity.name}CreateCtrl}).
      when('/${entity.name.toLowerCase()}/edit/:id', {templateUrl: 'partials/${entity.name.toLowerCase()}/edit.html',   controller: ${entity.name}EditCtrl}).
      when('/${entity.name.toLowerCase()}/:id', {templateUrl: 'partials/${entity.name.toLowerCase()}/view.html',   controller: ${entity.name}ViewCtrl}).
#end
	  when('/patron/adminCreate', {templateUrl: 'partials/patron/excelcreate.html',   controller: PatronExcelCreateCtrl}).
	  when('/login', {templateUrl: 'partials/login.html'}).
	  when('/loginfailed', {templateUrl: 'partials/loginfailed.html'}).
	  when('/j_spring_security_check', {templateUrl: 'spring-jpa-data/j_spring_security_check'}).
	  otherwise({redirectTo: '/login'});
}]);

integralApp.config(function(RestangularProvider) {
    RestangularProvider.setBaseUrl("/spring-jpa-data/rest");
    RestangularProvider.setFullRequestInterceptor(function(element, operation, route, url, headers, params) {
        if(operation == 'put' || operation == 'post') { 
            $.each(element, function(key, property) {
            	if(integralApp.isNotNull(property) && integralApp.isNotNull(property.links)) {
            		delete property.links;
            	}
            });
            if(integralApp.isNotNull(element.links)) {
            	delete element.links;	
            }
        }
        return {
            element: element,
            headers: headers,
            operation: operation,
            route: route,
            url: url,
            params: params
        };
    });
    
});


// register the interceptor as a service
integralApp.factory('hateoasOneLevelLinkInterceptor', function($q) {
    return function(promise) {
        return promise.then(function (response) {
            return response;
        }, function (response) {
            return $q.reject(response);
        });
    };
});

integralApp.config(function($httpProvider){
  //$httpProvider.responseInterceptors.push('hateoasOneLevelLinkInterceptor');
});



integralApp.config(function($httpProvider){
  //$httpProvider.responseInterceptors.push('hateoasOneLevelLinkInterceptor');
    $httpProvider.responseInterceptors.push('httpInterceptor');
    var spinnerFunction = function (data, headersGetter) {
        $('#ajax-loader').show();
        return data;
    };
    $httpProvider.defaults.transformRequest.push(spinnerFunction);
});


integralApp.factory('httpInterceptor', function ($q, $window) {
        return function (promise) {
            return promise.then(function (response) {
                $('#ajax-loader').hide();
                return response;
            }, function (response) {
                $('#ajax-loader').hide();
                return $q.reject(response);
            });
        };
});

//register global utility method
integralApp.isNotNull = function(val) {return !integralApp.isNull(val)}
integralApp.isNull = function(val){ return angular.isUndefined(val) || val === null}

integralApp.constant('imageRestApi', {
	'upload': 'http://localhost:8888/services/rest/upload/image',
	'get': 'http://localhost:8888/services/rest/get/image/binary'
});

integralApp.constant('csvRestApi', {
	'upload': 'http://localhost:8887/services/rest/integral/csv/lookup/patron'
});

integralApp.constant('uniqueConstraintRestApi', {
	'get': 'http://localhost:7788/services/rest/get/isUnique/tablecolumns'
});