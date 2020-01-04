//piccola magia per sostituire gli url in cui figura un get parameter per la notifica
//con una versione senza di esso, cosicché facendo refresh o usando back e forward nel browser
//la notifica non appaia più.
//si poteva usare semplicemente il pathname dell'url, ma così puoi tenere intatti gli altri parametri
//nel caso in cui non ci sia solo il parametro notifica
if (window.location.href.includes("?notifica") || window.location.href.includes("&notifica")) {
	var url = window.location.href;
	var newUrl = "";

	for (var x = 0; x < url.length; x++) {
		if (url.charAt(x) == '?' || url.charAt(x) == '&') {
			newUrl = newUrl + url.charAt(x).toString();
			if (url.length - x >= 11) {
				if (url.substring(x + 1, x + 9) == "notifica") {
					if (!(url.substring(x + 11, url.length).includes("&"))) {
						break;
					} else {
						for (var y = x + 11; y < url.length; y++) {
							if (url.charAt(y) == '&') {
								if (url.charAt(x) == '&') {
									x = y;
									break;
								} else if (url.charAt(x) == ('?')) {
									break;
								}
							}
						}
					}
				}
			}
		} else {
			newUrl = newUrl + url.charAt(x).toString();
			continue;
		}
	}
	if (newUrl[newUrl.length - 1] == '&' || newUrl[newUrl.length - 1] == '?') {
		newUrl = newUrl.substring(0, newUrl.length - 1);
	}

	window.history.replaceState({}, window.location.href, newUrl);
}

//animazione di fade-out degli alert di bootstrap
window.setTimeout(function() {
	$(".alert-dismissible").fadeTo(500, 0).slideUp(500, function() {
		$(this).remove();
	});
}, 6000);
