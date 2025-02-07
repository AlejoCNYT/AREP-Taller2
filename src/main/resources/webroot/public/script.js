function fetchHello()
{
    fetch('/hello?name=Pedro')
        .then(response => response.text())
        .then(data =>
            {
                document.getElementById('helloMessage').innerText = data;
            })
        .catch(error => console.error("Error:", error));
}

function fetchPi()
{
    fetch('/pi')
        .then(response => response.text())
        .then( data =>
            {
                document.getElementById('piValue').innerText = "PI: " + data;
            })
        .catch(error => console.error('Error:', error));
}
