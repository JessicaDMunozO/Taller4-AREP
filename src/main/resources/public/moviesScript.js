function loadGetMsg() {
    let titleVar = document.getElementById("title").value;
    const xhttp = new XMLHttpRequest();
    xhttp.onload = function () {
        document.getElementById("getrespmsg").innerHTML =
            this.responseText;
    };
    xhttp.open("GET", "/movie?title=" + titleVar);
    xhttp.send();
}