$(document).ready(function() {
    if ($('#payForPurchaseLink').attr("data-verification")) {
        $('#approveModal').modal('show');
        startEmployeeScanner();
    } else {
        startProductScanner();
    }
});

$('#approveModal').on('hidden.bs.modal', function () {
    startProductScanner();
});

