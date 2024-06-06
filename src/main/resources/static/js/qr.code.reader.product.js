const html5QrcodeScannerProduct = new Html5Qrcode("reader-product");

let lastScanTimeProduct = 0;
const scanDelayProduct = 500;

function startProductScanner() {
    html5QrcodeScannerProduct.start(
        { facingMode: "environment" },
        { fps: 10 },
        function(qrCodeMessage) {
            const now = Date.now();
            if (now - lastScanTimeProduct > scanDelayProduct) {
                console.log(`QR Code detected, result: ${qrCodeMessage}`);

                $.post("/put_to_cart", { barcode: qrCodeMessage, amount: 1 },
                    function () {
                        window.location.href = "/form_purchase";
                    })
                    .fail(function(error) {
                        console.error(`Failed to add product to cart. Error: ${error}`);
                    });

                lastScanTimeProduct = now;
            }
        },
        function(errorMessage) {
            const now = Date.now();
            if (now - lastScanTimeProduct > scanDelayProduct) {
                console.log(`QR Code no detected, error: ${errorMessage}`);
                lastScanTimeProduct = now;
            }
        }
    );
}