$(document).ready(function () {
    if (window.location.href.indexOf('?') === -1) {
        $(".card-body").hide();
    }
    $(".card-header").click(function () {
        $(".card-body").toggle(150);
    });
    $(".form-control, .btn").click(function (event) {
        event.stopPropagation();
    });
});