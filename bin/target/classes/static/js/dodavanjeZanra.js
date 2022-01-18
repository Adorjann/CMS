var baseURL = ""

function popuniBaseURL() {
	// traži od servera baseURL
	$.get("baseURL", function(odgovor) { // GET zahtev
		console.log(odgovor)

		if (odgovor.status == "ok") {
			baseURL = odgovor.baseURL // inicjalizuj globalnu promenljivu baseURL
			$("base").attr("href", baseURL) // postavi href atribut base elementa
		}
	})
	console.log("GET: " + "baseURL")
}

function popuniPrijavljenogKorisnika() {
	// traži od servera prijavljenog korisnika
	$.get("Korisnici/PrijavljeniKorisnik", function(odgovor) { // GET zahtev
		console.log(odgovor)

		if (odgovor.status == "ok") {
			var prijavljeniKorisnik = odgovor.prijavljeniKorisnik
			if (prijavljeniKorisnik != null && prijavljeniKorisnik.administrator == true) {
				// prijavljen
				var korisnikLink = $("table.korisnik:first").find("a:first")
				korisnikLink.attr("href", korisnikLink.attr("href") + prijavljeniKorisnik.korisnickoIme) // dodavanje korisničkog imena na href link-a do korisnika
				korisnikLink.text(prijavljeniKorisnik.korisnickoIme)
			} else {
				// neprijavljen ili nije admin
				window.location.replace("zanrovi.html")
			}
		}
	})
	console.log("GET: " + "Korisnici/PrijavljeniKorisnik")
}

function odjava() {
	// pošalji server-u zahtev za odjavu
	$.get("Korisnici/Logout", function(odgovor) { // GET zahtev
		console.log(odgovor)

		if (odgovor.status == "ok") {
			// odjava uspela
			window.location.replace(baseURL) // client-side redirekcija na početnu stranicu
		}
	})
	console.log("GET: " + "Korisnici/Logout")

	return false // sprečiti da link za odjavu promeni stranicu
}

$(document).ready(function() {
	popuniBaseURL() // dobavi i ugradi u HTML informaciju o baseURL-u
	popuniPrijavljenogKorisnika() // dobavi i ugradi u HTML informaciju o prijavljenom korisniku

	$("table.korisnik:first a:last").click(odjava) // registracija handler-a za odjavu

	var nazivInput = $("input[name=naziv]")
	var pasusGreska = $("p.greska")
	
	function dodajZanr() {
		var naziv = nazivInput.val()
		
		var params = {
			naziv: naziv
		}
		console.log(params)
		$.post("Zanrovi/Create", params, function(odgovor) {
			console.log(odgovor)

			if (odgovor.status == "ok" || odgovor.status == "odbijen") {
				window.location.replace("zanrovi.html")
			} else if (odgovor.status == "greska") {
				pasusGreska.text(odgovor.poruka)
			}
		})
		console.log("POST: Zanrovi/Create")
	}

	$("input[type=submit]").click(function() {
		dodajZanr()
		return false
	})
})