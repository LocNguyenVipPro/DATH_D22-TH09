document.addEventListener("DOMContentLoaded", function() {
    var searchButton = document.getElementById('searchButton');
    var categoryId = document.getElementById('categoryId').value;
    searchButton.addEventListener('click', function() {
        var keyword = document.getElementById("default-search").value;
        window.location.href = "/main-page?&search=" + encodeURIComponent(keyword);
    });

    function saveSearchValue(value) {
        localStorage.setItem('previousSearch', value);
    }

    function restoreSearchValue() {
        var previousSearch = localStorage.getItem('previousSearch');
        if (previousSearch) {
            document.getElementById('default-search').value = previousSearch;
        }
    }

    var previousBtn = document.getElementById('previous-btn');
    var nextBtn = document.getElementById('next-btn');
    if (previousBtn && nextBtn) {
        var currentPage = 1;
        var productsPerPage = 5;
        var totalProducts = document.getElementsByClassName('product-item').length;
        var totalPages = Math.ceil(totalProducts / productsPerPage);

        previousBtn.addEventListener('click', function() {
            if (currentPage > 1) {
                currentPage--;
                showProducts();
            }
        });

        nextBtn.addEventListener('click', function() {
            if (currentPage < totalPages) {
                currentPage++;
                showProducts();
            }
        });

        function showProducts() {
            var products = document.getElementsByClassName('product-item');
            var start = (currentPage - 1) * productsPerPage;
            var end = Math.min(start + productsPerPage, totalProducts);

            for (var i = 0; i < products.length; i++) {
                if (i >= start && i < end) {
                    products[i].style.display = 'block';
                } else {
                    products[i].style.display = 'none';
                }
            }
        }
        showProducts();
        previousBtn.style.display = "inline-block";
        nextBtn.style.display = "inline-block";
    }
});

function handleSuccess(response) {
    $('.delete-item').remove();
    var html = '';
    response.forEach(function(ProductDTO) {
        var name = ProductDTO.product.name.substring(0, 30);
        var content = ProductDTO.product.content.substring(0, 33);
        var price = ProductDTO.product.price || 0;
        var discountPrice = parseInt(ProductDTO.product.discountPrice) || 0;
        var link = 'cart?productId=' + ProductDTO.product.id;
        var saleItem = formatNumber(ProductDTO.quantityProduct);
        var saleTag = formatDiscount(((1 - (discountPrice / price)) * 100).toFixed(2));

        if (parseFloat(saleTag) >= 0) {
            saleTag = '<div class="absolute top-2 left-0 bg-green-400 py-2 px-4 rounded-md shadow-md flex items-center">\n' +
                '         <span>Sale ' + saleTag + '</span>\n' +
                '    </div>';
        } else {
            saleTag = '';
        }

        html +=
            '            <div  class="delete-item  overflow-hidden hover:border-amber-500 flex flex-col justify-center items-center hover:shadow-md transition-all duration-500 border-r-[1px] border-stone-200 hover: transform transition duration-300 hover:scale-105" id="parent-item">\n' +
            '                <div class="py-5 w-full bg-white shadow-md rounded-xl duration-500 hover:scale-105 hover:shadow-xl relative">\n' +
            '                    <a href="' + link + '">\n' +
            '                        <img src="/images/products/' + ProductDTO.product.id + '/' + ProductDTO.product.image + '"\n' +
            '                             alt="Product" class="h-80 w-72 object-cover rounded-t-xl" /> ' + saleTag + '\n' +

            '                        <div class="flex items-center  flex-col relative">\n' +
            '\n' +
            '                            <div class="py-2">\n' +
            '                                <p class="text-center text-xl font-bold font-mono leading-normal text-gray-700 ">\n' +
            '                                    <span >' + name + '</span>\n' +
            '                                     </p>\n' +
            '                                <p class="text-center text-lg font-mono leading-normal text-gray-700 opacity-70 ">\n' +
            '                                    <span>' + content + '</span>\n' +
            '                                </p>\n' +
            '                            </div>\n' +
            '                            <div class="px-2 py-4   ">\n' +
            '                            <span class="  text-sm line-through opacity-70"\n' +
            '                                 >' + price + '$</span>\n' +
            '                                <span class="ml-3 text-xl font-bold text-red-900 dark:text-white"\n' +
            '                                    >' + discountPrice + '$</span></span>\n' +
            '                            </div>\n' +
            '                        </div>\n' +
            '                        <span class="absolute  bottom-2 right-1 block text-sm font-bold text-gray-700" >Sell Item:' + saleItem + '</span>\n' +
            '                    </a>\n' +
            '                </div>\n' +
            '            </div>\n' +
            '        </section>'
    });
    // Update the product list with the new HTML
    $('#all-item').html(html);
}



function formatNumber(num) {
    if (num >= 1000000) {
        return (num / 1000000).toFixed(1) + 'm';
    } else if (num >= 1000) {
        return (num / 1000).toFixed(1) + 'k';
    } else {
        return num.toString();
    }
}

function formatDiscount(discount) {
    var roundedDiscount = Math.round(discount);
    if (discount - roundedDiscount >= 0.5) {
        return (roundedDiscount + 1) + '%';
    } else {
        return roundedDiscount + '%';
    }
}
function handleCheckbox(checkbox,dropdownId) {
    var saleRange;
    var priceRange;
    // alert("cldmlvmdlkm")
    if(dropdownId==='dropdownHoverButton')
    {
        var checkboxPercent = document.getElementsByName('saleRange');
        saleRange=checkbox.value;
        checkboxPercent.forEach((item) => {
            if (item !== checkbox) {
                item.checked = false;
            }
        })
        var checkBoxPriceRange = document.getElementsByName('price-range');
        checkBoxPriceRange.forEach((item) => {
            if (item.checked) {
                priceRange=item.value;
            }
        })
    }else if(dropdownId==='dropdownHoverButtonRanger')
    {
        priceRange=checkbox.value;
        var checkPriceRange = document.getElementsByName('price-range');
        checkPriceRange.forEach((item) => {
            if (item !== checkbox) {
                item.checked = false;
            }
        })
        var checkPercent = document.getElementsByName('saleRange');
        checkPercent.forEach((item) => {
            if (item.checked) {
                saleRange=item.value;
            }
        })
    }else if(dropdownId==='categorySelected')
    {
        $('#categoryId').val(checkbox);
        var checkPriceRangeIn = document.getElementsByName('price-range');
        checkPriceRangeIn.forEach((item) => {
            if (item.checked) {
                priceRange=item.value;
            }
        })
        var checkPercent12 = document.getElementsByName('saleRange');
        checkPercent12.forEach((item) => {
            if (item.checked) {
                saleRange=item.value;
            }
        })
    }else if(dropdownId==='clearAll')
    {
        var clearAllPriceRange = document.getElementsByName('price-range');
        var clearAllSaleRange = document.getElementsByName('saleRange');
        clearAllPriceRange.forEach((item)=>{
            if(item.checked)
            {
                item.checked=false;
            }
        })
        clearAllSaleRange.forEach((item)=>{
            if(item.checked)
            {
                item.checked=false;
            }
        })
        $('#categoryId').val('');
    }
    // alert(priceRange)
    // alert(saleRange)
    // alert(categoryId)
    if (typeof priceRange === 'undefined') {
        priceRange = '';
    }
    if (typeof saleRange === 'undefined') {
        saleRange = '';
    }
    var categoryId=document.getElementById('categoryId').value;
    if (typeof categoryId === 'undefined' || categoryId.trim().length===0) {
        categoryId = '';
    }


    $.ajax({
        type: "GET",
            url: "/products/load-product",
        data: {
            rangePrice:priceRange,
            rangeSalePercent:saleRange,
            categoryId:categoryId
        },
        success: handleSuccess,
        error: function() {
            alert('Yêu cầu AJAX không thành công.');
        }
    });
}
