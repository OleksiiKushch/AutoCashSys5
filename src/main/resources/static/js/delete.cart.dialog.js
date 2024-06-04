$(".deleteLink").click(function () {
    let barcode = $(this).attr("data-id");
    let message = $(this).attr("data-message-format");
    $("#modalBodyDeleteForm").html(message);
    $("#deleteFormProductId").attr("value", barcode);
});