
# JavaScript Help Codes

### Hide Show Div

```
function myFunction() {
  var x = document.getElementById("myDIV");
  if (x.style.display === "none") {
    x.style.display = "block";
  } else {
    x.style.display = "none";
  }
}

// style.display -> none, block
// style.visibility -> hidden, visible
```


### AJAX only JavaScript

```
	var xhr = new XMLHttpRequest();

	xhr.open('GET', '/Purchase/CreateOrder?amount=' + amount);

	xhr.send();

	// Event Handler
	xhr.onreadystatechange = function () {

		if (xhr.readyState === XMLHttpRequest.DONE) {
			if (xhr.status === 200) {

				console.log('ajax succeed : ' + xhr.responseText);

			} else {
				console.log('[' + xhr.status + ']: ' + xhr.statusText);
			}
		}
	};
```