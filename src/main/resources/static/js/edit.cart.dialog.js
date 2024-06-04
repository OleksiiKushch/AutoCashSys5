$(".editLink").click(function () {
    let barcode = $(this).attr("data-id");
    let amount = $(this).attr("data-amount");
    let message = $(this).attr("data-message-format");
    $("#modalBodyEditForm").html(message);
    $("#editFormProductId").attr("value", barcode);
    $("#inputAmountEdit").attr("value", amount);
});