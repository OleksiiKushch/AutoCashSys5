const html5QrcodeScannerEmployee = new Html5Qrcode("reader-employee");

let lastScanTimeEmployee = 0;
const scanDelayEmployee = 500;

html5QrcodeScannerEmployee.start(
    { facingMode: "environment" },
    { fps: 10 },
    function(qrCodeMessage) {
        const now = Date.now();
        if (now - lastScanTimeEmployee > scanDelayEmployee) {
            console.log(`QR Code detected, result: ${qrCodeMessage}`);

            $.post("/approve_cart", { employeeUsername: qrCodeMessage },
                function () {
                    window.location.href = "/pay_for_purchase";
                })
                .fail(function(error) {
                    console.error(`Failed to approve the cart. Error: ${error}`);
                });

            lastScanTimeEmployee = now;
        }
    },
    function(errorMessage) {
        const now = Date.now();
        if (now - lastScanTimeEmployee > scanDelayEmployee) {
            console.log(`QR Code no detected, error: ${errorMessage}`);
            lastScanTimeEmployee = now;
        }
    }
);