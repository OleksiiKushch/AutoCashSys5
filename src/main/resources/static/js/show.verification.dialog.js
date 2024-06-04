$(document).ready(function() {
    if ($('#payForPurchaseLink').attr("data-verification")) {
        $('#approveModal').modal('show');
    }
});