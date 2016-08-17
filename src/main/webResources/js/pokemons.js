angular.module('hello')
    .controller('pokemons', function ($http, $scope) {
        var self = this;
        self.getPokemons = function () {
            $http.get("/getPokemons").then(function (response) {
                $scope.listPokemons = response.data;
            });
        };
        self.getPokemons();
 });