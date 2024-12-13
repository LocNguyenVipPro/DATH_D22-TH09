

function addCartItem() {
    handleFormSubmission("Bạn có muốn thêm vào giỏ hàng?", function(confirm) {
        if(confirm===true)
        {
            var form = document.getElementById('submitForm');
                form.setAttribute('action', '/cart/update-cart');
                form.submit();
        }
    });
}

function purchase() {
    handleFormSubmission("Bạn có muốn mua sản phẩm này không?", function(confirm) {
        if(confirm===true)
        {
            var form = document.getElementById('submitForm');
            form.setAttribute('action', '/order/purchase');
            form.submit()
        }
    });
}

document.addEventListener("DOMContentLoaded", function() {
    var originalProductPrice = parseFloat(document.getElementById('originalProductPrice').value);
    var originalDiscountPrice = parseFloat(document.getElementById('originalDiscountProductPrice').value);
    var taxPercentage = parseFloat(document.getElementById('taxPercent').textContent);

    var totalPrice = originalDiscountPrice / 100 * taxPercentage + originalDiscountPrice;
    var taxAmount = originalDiscountPrice / 100 * taxPercentage;

    document.getElementById('totalPrice').textContent = totalPrice.toFixed(2);
    document.getElementById('taxAmount').textContent = taxAmount.toFixed(2);

    var totalLabelElement = document.getElementById('totalLabel');
    totalLabelElement.textContent = "Please pay $" + totalPrice.toFixed(2) + " upon delivery.";
});


function updatePrice(quantity, originalProductPrice, originalDiscountPrice) {
    if (isNaN(originalProductPrice) || isNaN(originalDiscountPrice) || isNaN(quantity)) {
        alert("Invalid input. Please check the values.");
        return;
    }

    var totalPrice = originalProductPrice * quantity;
    if (isNaN(totalPrice)) {
        alert("Invalid calculation. Please check the values.");
        return;
    }

    var totalDiscountPrice = originalDiscountPrice * quantity;
    if (isNaN(totalDiscountPrice)) {
        alert("Invalid calculation. Please check the values.");
        return;
    }

    document.getElementById('price').textContent = "$" + totalPrice;
    document.getElementById('discountPrice').textContent = "$" + totalDiscountPrice;


    //update Price
    var taxPercentage = parseFloat(document.getElementById('taxPercent').textContent);
    var taxAmount = (totalDiscountPrice * taxPercentage) / 100;

    var totalAmount = (taxAmount + totalDiscountPrice).toFixed(2);
    document.getElementById('taxAmount').textContent = "$" + taxAmount.toFixed(2);
    document.getElementById('totalPrice').textContent = "$" + totalAmount;
    document.getElementById('totalLabel').textContent = "Please pay " + totalAmount + "$ upon delivery.";
}

function decrementQuantity() {
    var quantityElement = document.getElementById('quantity');
    var originalProductPrice = parseFloat(document.getElementById('originalProductPrice').value);
    var originalDiscountPrice = parseFloat(document.getElementById('originalDiscountProductPrice').value);
    var quantity = parseInt(quantityElement.value);
    quantity--;
    if (quantity < 1) quantity = 1;

    quantityElement.value = quantity;
    updatePrice(quantity, originalProductPrice, originalDiscountPrice);
}

function incrementQuantity() {
    var quantityElement = document.getElementById('quantity');
    var originalProductPrice = parseFloat(document.getElementById('originalProductPrice').value);
    var originalDiscountPrice = parseFloat(document.getElementById('originalDiscountProductPrice').value);
    var quantity = parseInt(quantityElement.value);
    quantity++;
    if (quantity < 1) quantity = 1;

    quantityElement.value = quantity;
    updatePrice(quantity, originalProductPrice, originalDiscountPrice);
}

function toggleFavorite() {
    var form = document.getElementById('submitForm');
    form.setAttribute('action', '/wishlist');
    form.submit()
}
