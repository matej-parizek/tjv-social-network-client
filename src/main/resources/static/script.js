const fileUpload = (event) => {
    const files = event.target.files;
    const filesLength = files.length;
    if (filesLength > 0) {
        const imageSrc = URL.createObjectURL(files[0]);
        const imagePreviewElement = document.querySelector("#tb-image");
        imagePreviewElement.src = imageSrc;
        imagePreviewElement.style.display = "block";
    }
};
function showCoWorker() {
    var checkBox = document.getElementById("Co-Worker");
    var text = document.getElementById("co-text");
    if (checkBox.checked == true){
        text.style.display = "block";
    } else {
        text.style.display = "none";
    }
}

function saveScrollPosition() {
    localStorage.setItem('scrollPosition', window.scrollY);
}

window.onload = function () {
    var scrollPosition = localStorage.getItem('scrollPosition');
    if (scrollPosition !== null) {
        window.scrollTo(0, parseInt(scrollPosition));
        localStorage.removeItem('scrollPosition');
    }
};

function togglePasswordVisibility() {
    var passwordInput = document.getElementById("password");
    var checkbox = document.getElementById("showPassword");

    if (checkbox.checked) {
        passwordInput.type = "text";
    } else {
        passwordInput.type = "password";
    }
}