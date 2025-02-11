function sayHello() {
    alert("Hello from My Web Framework!");
}

function getPi() {
    fetch('/pi')
        .then(response => response.text())
        .then(data => alert("PI: " + data))
        .catch(error => console.error("Error:", error));
}
