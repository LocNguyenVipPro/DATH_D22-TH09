

function clearAll() {
    handleFormSubmission("Bạn có chắc muốn xoá hết trong giỏ hàng?", function(confirm) {
        if (confirm) {
            window.location.href = '/cart/clear';
        }
    });
}
function checkOut() {
    handleFormSubmission("Bạn có chắc muốn thanh toán?", function (confirm) {
        if(confirm)
        {
            var form = document.getElementById('submitForm');
            form.setAttribute('action', '/cart/checkout');
            form.submit();
        }
    })
}

function deleteCartLineItem(cartLineItemId)
{
    handleFormSubmission("Bạn có chắc muỗn xoá vật phẩm này",function (confirm)
    {
        if(confirm) {
            window.location.href = '/cart/remove?cartLineItemId='+cartLineItemId;
        }
    })
}
function updateHiddenQuantity(productId, value) {
    $('#quantityInputHidden' + productId).val(value);
}

function buyInCartLineItem(cartLineItemId)
{
    var form = document.getElementById('submitForm');
    //get product and quantity
    var quantity = document.getElementById('quantityInputHidden'+cartLineItemId).value;
    quantity = parseInt(quantity);
    document.getElementById('cartLineItemId').setAttribute('value',cartLineItemId);
    document.getElementById('quantity').setAttribute('value',quantity);

    handleFormSubmission("Bạn có chắc muốn thanh toán chứ",function (confirm)
    {
        if(confirm) {
            form.setAttribute('action', '/order/purchase-in-cart');
            form.submit();
        }
    })
}



function updateHiddenQuantity(productId, value) {
        $('#quantityInputHidden' + productId).val(value);
}

function decrementQuantity(productId) {
    let quantityElement = $('#quantityInput' + productId);
    let quantity = parseInt(quantityElement.val());
    quantity--;
    if (quantity < 1)
    {
        quantity = 1;
    }else
    {
        quantityElement.val(quantity);
        updatePriceDetails(productId, quantity);
    }
}

function incrementQuantity(productId) {
    let quantityElement = $('#quantityInput' + productId);
    let quantity = parseInt(quantityElement.val());
    quantity++;
    quantityElement.val(quantity);
    updatePriceDetails(productId, quantity);
}

// update label when click plus or minus
function updatePriceDetails(productId, quantity) {
    let originalDiscountPrice = parseFloat($('#originalDiscountProductPrice' + productId).val());
    let taxPercentage = parseFloat($('#taxPrice' + productId).val());
    let subTotal = parseFloat($('#subTotal').text().replace('$', ''));
    let taxTotal = parseFloat($('#taxTotal').text().replace('$', ''));
    let totalAmount = parseFloat($('#totalAmount').text().replace('$', ''));
    let quantityInputHidden = parseFloat($('#quantityInputHidden' + productId).val());
    let totalDiscountPrice = originalDiscountPrice * quantity;
    taxPercentage=(originalDiscountPrice*taxPercentage)/100;

    if (quantityInputHidden < quantity) {
        subTotal+=originalDiscountPrice;
        taxTotal+=taxPercentage;
        totalAmount+=(originalDiscountPrice+taxPercentage)
    }else
    {
        subTotal-=originalDiscountPrice;
        taxTotal-=taxPercentage;
        totalAmount-=(originalDiscountPrice+taxPercentage)
    }
    updateHiddenQuantity(productId, quantity);
    $('#discountPrice' + productId).text("$" + totalDiscountPrice.toFixed(2));
    $('#subTotal').text("$" + subTotal.toFixed(2));
    $('#taxTotal').text("$" + taxTotal.toFixed(2));
    $('#totalAmount').text("$" + totalAmount.toFixed(2));
}
$(document).ready(function() {
    $('.product').each(function() {
        let productId = $(this).attr('data-productId');
        let quantity = parseInt($('#quantityInput' + productId).val());
        updatePriceDetails(productId, quantity);
    });
});


