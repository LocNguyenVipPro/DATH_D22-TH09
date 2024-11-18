document.addEventListener("DOMContentLoaded", function() {
    var pages = document.querySelectorAll(".page-item");
    var currentPageIndex = 3;

    function showPages() {
        for (var i = 0; i < pages.length; i++) {
            if (i < currentPageIndex - 3 || i > currentPageIndex + 3) {
                pages[i].style.display = "none";
            } else {
                pages[i].style.display = "inline-flex";
            }
        }
    }

    showPages();

    function updateCurrentPageIndex(newIndex) {
        currentPageIndex = newIndex;
        showPages();
    }

    var prevButton = document.querySelector(".prev-button");
    var nextButton = document.querySelector(".next-button");

    prevButton.addEventListener("click", function() {
        updateCurrentPageIndex(currentPageIndex - 1);
    });

    nextButton.addEventListener("click", function() {
        updateCurrentPageIndex(currentPageIndex + 1);
    });
});