$(document).ready(function ()
{
    document.addEventListener("DOMContentLoaded", function() {
        const elements = document.querySelectorAll(".truncate");
        const maxLength = 10; // Maximum character length

        elements.forEach(function(element) {
            const text = element.textContent; // Get the text content
            elements.forEach(function (text)
            {

                if (text.length > maxLength) {
                    const truncatedText = text.substring(0, maxLength) + "..."; // Truncate with ellipsis
                    element.textContent = truncatedText;
                }
            })

        });
    });



    $('#searchButton').on('click',function ()
    {
        var keyword = document.getElementById("valueSearch").value.trim();
        var isHideStr = document.getElementById("isHide").value.trim();
        var isHide = (isHideStr === "true");
        isHide = !isHide;
        if(keyword.length!==0)
        {
            window.location.href = "/products?search=" + encodeURIComponent(keyword)+"&isHide="+encodeURIComponent(isHide);

        }
    })

    const description = document.querySelector('.description')
    console.log(description)
})

