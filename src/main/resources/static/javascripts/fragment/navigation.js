// JavaScript for dropdown menu
document.addEventListener("DOMContentLoaded", function() {
    const userMenuButton = document.getElementById("user-menu-button");
    const userMenu = document.querySelector(".dropdown-menu");

    userMenuButton.addEventListener("click", function() {
        const expanded = this.getAttribute("aria-expanded") === "true" || false;
        this.setAttribute("aria-expanded", !expanded);
        userMenu.classList.toggle("hidden");
    });

    // Close the dropdown menu when clicking outside
    document.addEventListener("click", function(event) {
        const isClickInside = userMenu.contains(event.target) || userMenuButton.contains(event.target);
        if (!isClickInside) {
            userMenu.classList.add("hidden");
            userMenuButton.setAttribute("aria-expanded", "false");
        }
    });
});

