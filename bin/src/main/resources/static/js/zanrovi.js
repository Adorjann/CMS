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
			if (prijavljeniKorisnik != null) {
				// prijavljen
				var korisnikLink = $("table.korisnik:first").find("a:first")
				korisnikLink.attr("href", korisnikLink.attr("href") + prijavljeniKorisnik.korisnickoIme) // dodavanje korisničkog imena na href link-a do korisnika
				korisnikLink.text(prijavljeniKorisnik.korisnickoIme)
				
				$("table.korisnik:last").hide() // sakrij tabelu za neprijavljenog korisnika
			} else {
				// neprijavljen
				$("table.korisnik:first").hide() // sakrij tabelu za prijavljenog korisnika
			}
			if (!(prijavljeniKorisnik != null && prijavljeniKorisnik.administrator == true)) {
				// nije (prijavljen i administrator)
				$('a[href="korisnici.html"]').parent().hide() // sakrij list item koji obuhvata link do korisnici.html
				$('a[href="dodavanjeZanra.html"]').parent().parent().hide()
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

	var tabela = $("table.tabela")
	var nazivInput = tabela.find("input[name=naziv]")
	
	function popuniZanrove() {
		var naziv = nazivInput.val()

		var params = {
			naziv: naziv
		}
		console.log(params)
		$.get("Zanrovi", params, function(odgovor) {
			console.log(odgovor)

			if (odgovor.status == "ok") {
				tabela.find("tr:gt(1)").remove()

				var zanrovi = odgovor.zanrovi
				for (var it in zanrovi) {
					tabela.append(
							'<tr>' + 
								'<td class="broj">' + (parseInt(it) + 1) + '</td>' + 
								'<td><a href="zanr.html?id=' + zanrovi[it].id + '">' + zanrovi[it].naziv + '</a></td>' + 
								'<td><a href="filmovi.html?zanrId=' + zanrovi[it].id + '">filmovi</a></td>' + 
							'</tr>'
						)
				}
			}
		})
		console.log("GET: Zanrovi")
	}
	tabela.find("input[type=submit]").click(function() {
		popuniZanrove()
		return false
	})

	popuniZanrove()
})