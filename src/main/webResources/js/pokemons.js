angular.module('hello')
    .controller('pokemons', function ($http, $scope, $window,$rootScope,$location) {
        var self = this;


        self.getPokemons = function () {
            $http.get("/getPokemons").then(function (response) {
                $scope.listPokemons = response.data;
            });
        };
        self.getPokemons();
 });