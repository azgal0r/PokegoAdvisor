angular.module('hello', [])
    .controller('pokemons', function ($http, $scope, $window) {
        var self = this;
        self.getAPICode = function () {
            $http.get("/getGoogleCode").then(function (response) {
                $window.open(response.data.url);
            });
        };

        self.getPokemons = function () {
            $http.get("/getPokemons?key="+$scope.key).then(function (response) {
                $scope.listPokemons = response.data;
            });
        };
 });