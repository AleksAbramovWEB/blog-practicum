
$(document).ready(function () {
    $("#tags").selectize({
        delimiter: ",",
        persist: false,
        maxItems: null,
        create: function (input) {
            return {
                value: input,
                text: input,
            };
        }
    });
});