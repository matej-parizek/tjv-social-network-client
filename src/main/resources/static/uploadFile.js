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