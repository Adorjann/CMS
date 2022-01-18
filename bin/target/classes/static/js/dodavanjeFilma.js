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
				// administrator
				var korisnikLink = $("table.korisnik:first").find("a:first")
				korisnikLink.attr("href", korisnikLink.attr("href") + prijavljeniKorisnik.korisnickoIme) // dodavanje korisničkog imena na href link-a do korisnika
				korisnikLink.text(prijavljeniKorisnik.korisnickoIme)

				// pripremi podatke na stranci
				popuniZanrove() 
			} else {
				// nije prijavljen ili nije administrator
				window.location.replace(baseURL) // client-side redirekcija na početnu stranicu
			}
		}
	})
	console.log("GET: " + "Korisnici/PrijavljeniKorisnik")
}

function popuniZanrove() {
	// traži od server-a sve žanrove
	$.get("Zanrovi", function(odgovor) { // GET zahtev
		console.log(odgovor)

		if (odgovor.status == "ok") {
			var celija = $("table.forma").find("input[name=zanrId]").parent()
			celija.empty() // ukloni sve checkbox-ove

			var zanrovi = odgovor.zanrovi
			for (var itZanr in zanrovi) { // za svaki žanr
				celija.append( // kreiraj, popuni checkbox i dodaj ga u celiju
					'<input type="checkbox" name="zanrId" value="' + zanrovi[itZanr].id + '"/><span>' + zanrovi[itZanr].naziv + '</span><br>'
				)
			}
		}
	})
	console.log("GET: " + "Zanrovi")
}

function odjava() {
	// pošalji server-u zahtev za odjavu
	$.get("Korisnici/Logout", function(odgovor) { // GET zahtev
		console.log(odgovor)

		if (odgovor.status == "ok") {
			// odjava uspela
			window.location.replace(baseURL) // klijent-side redirekcija na početnu stranicu
		}
	})
	console.log("GET: " + "Korisnici/Logout")

	return false // sprečiti da link za odjavu promeni stranicu
}

$(document).ready(function() {
	popuniBaseURL() // dobavi i ugradi u HTML informaciju o baseURL-u
	popuniPrijavljenogKorisnika() // dobavi i ugradi u HTML informaciju o prijavljenom korisniku

	$("table.korisnik:first a:last").click(odjava) // registracija handler-a za odjavu

	// keširanje referenci na elemente
	var nazivInput = $("input[name=naziv]")
	var zanrIdCelija = $("input[name=zanrId]").parent()
	var trajanjeInput = $("input[name=trajanje]")

	var pasusGreska = $("p.greska")
	
	function dodaj() {
		// čitanje parametara forme za dodavanje
		var naziv = nazivInput.val()
		
		var zanrId = [] // lista id-eva žanrova
		zanrIdCelija.find("input:checked").each(function(it, itCheckbox) { // za svaki čekirani žanr
			zanrId.push($(itCheckbox).val()) // dodaj id žanra u listu
		})

		var trajanje = trajanjeInput.val()
	
		// parametri zahteva
		var params = {
			naziv: naziv, 
			zanrId: zanrId.toString(), // vrlo speceifčan slučaj u kome niz neće automatski da se pretvori u odgovarajuću string-ovnu reprezentaciju, kako bi ga Spring prihvatio kao parametar
			trajanje: trajanje
		}
		console.log(params)
		$.post("Filmovi/Create", params, function(odgovor) { // POST zahtev
			console.log(odgovor)
	
			if (odgovor.status == "ok" || odgovor.status == "odbijen") {
				window.location.replace("filmovi.html") // client-side redirekcija na filmovi.html
			} else if (odgovor.status == "greska") {
				pasusGreska.text(odgovor.poruka)  // ispis poruke o greški
			}
		})
		console.log("POST: " + "Filmovi/Create")
		
		return false // sprečiti da submit forme promeni stranicu
	}
	
	$("form").submit(dodaj) // registracija handler-a za dodavanje
})