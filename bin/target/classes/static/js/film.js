var baseURL = ""
var id = window.location.search.slice(1).split('&')[0].split('=')[1]; // čitanje vrednosti prvog parametra GET zahteva na ovu stranicu (id-a u ovom slučaju)

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

	$("table.korisnik:first a:last").click(odjava) // registracija handler-a za odjavu

	// keširanje referenci na elemente
	var formaIzmena = $("form").eq(0)
	var formaBrisanje = $("form").eq(1)
	var tabelaZaObicanPrikaz = $("table.forma").eq(2)

	function popuniFilm() {
		// traži od server-a film

		// parametri zahteva
		var params = {
			id: id
		}
		console.log(params)
		$.get("Filmovi/Details", params, function(odgovor) { // GET zahtev
			console.log(odgovor)
	
			if (odgovor.status == "ok") {
				var celija = formaIzmena.find("input[name=zanrId]").parent()
				
				var film = odgovor.film
				var zanroviFilma = film.zanrovi
	
				// popuni formu za izmenu
				formaIzmena.find("input[name=naziv]").val(film.naziv)
				for (var it in zanroviFilma) { // za svaki žanr filma
					celija.find("input[value=" + zanroviFilma[it].id + "]").prop("checked", true) // pronađi i čekiraj odgovarajući checkbox
				}
				formaIzmena.find("input[name=trajanje]").val(film.trajanje)

				// popuni tabelu za običan prikaz
				var redoviTabele = tabelaZaObicanPrikaz.find("tr")

				redoviTabele.eq(0).find("td").text(film.naziv)
	
				var lista = redoviTabele.eq(1).find("ul")
				lista.empty() // isprazni listu link-ova do žanrova
				for (var it in zanroviFilma) { // za svaki žanr
					lista.append('<li><a href="zanr.html?id=' + zanroviFilma[it].id + '">' + zanroviFilma[it].naziv + '</a></li>') // kreiraj item u listi, a u njemu link do žanra
				}	

				redoviTabele.eq(2).find("td").text(film.trajanje)

				var linkKaProjekcijama = redoviTabele.eq(3).find("a")
				linkKaProjekcijama.attr("href", linkKaProjekcijama.attr("href") + film.id) // dodavanje id-a filma na href link-a do filma
			}
		})
		console.log("GET: " + "Filmovi/Details")
	}
	function popuniZanroveIFilm() {
		// traži od server-a žanrove
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

				// prvo se moraju popuniti žanrovi pa onda film
				popuniFilm()
			}
		})
		console.log("GET: " + "Zanrovi")
	}
	function popuniPrijavljenogKorisnikaIPodatke() {
		// traži od servera prijavljenog korisnika
		$.get("Korisnici/PrijavljeniKorisnik", function(odgovor) { // GET zahtev
			console.log(odgovor)
	
			if (odgovor.status == "ok") {
				var prijavljeniKorisnik = odgovor.prijavljeniKorisnik
				if (prijavljeniKorisnik != null) {
					if (prijavljeniKorisnik.administrator == true) {
						// prijavljen i administrator
						var korisnikLink = $("table.korisnik:first").find("a:first")
						korisnikLink.attr("href", korisnikLink.attr("href") + prijavljeniKorisnik.korisnickoIme) // dodavanje korisničkog imena na href link-a do korisnika
						korisnikLink.text(prijavljeniKorisnik.korisnickoIme)
						
						$("table.korisnik:last").hide() // sakrij tabelu za neprijavljenog korisnika
						tabelaZaObicanPrikaz.hide() // sakrij tabelu za običan prikaz
		
						// pripremi podatke na stranci
						popuniZanroveIFilm()
		
						// keširanje referenci na elemente
						var nazivInput = $("input[name=naziv]")
						var zanrIdCelija = $("input[name=zanrId]").parent()
						var trajanjeInput = $("input[name=trajanje]")
					
						var pasusGreska = $("p.greska")
						
						function izmeni() {
							// čitanje parametara forme za dodavanje
							var naziv = nazivInput.val()
							
							var zanrId = [] // lista id-eva žanrova
							zanrIdCelija.find("input:checked").each(function(it, itCheckbox) { // za svaki čekirani žanr
								zanrId.push($(itCheckbox).val()) // dodaj id žanra u listu
							})
					
							var trajanje = trajanjeInput.val()
						
							// parametri zahteva
							var params = {
								id: id, 
								naziv: naziv, 
								zanrId: zanrId.toString(), // vrlo speceifčan slučaj u kome niz neće automatski da se pretvori u odgovarajuću string-ovnu reprezentaciju, kako bi ga Spring prihvatio kao parametar
								trajanje: trajanje
							}
							console.log(params)
							$.post("Filmovi/Edit", params, function(odgovor) { // POST zahtev
								console.log(odgovor)
						
								if (odgovor.status == "ok" || odgovor.status == "odbijen") {
									window.location.replace("filmovi.html") // client-side redirekcija na filmovi.html
								} else if (odgovor.status == "greska") {
									pasusGreska.text(odgovor.poruka)  // ispis poruke o greški
								}
							})
							console.log("POST: " + "Filmovi/Edit")
							
							return false // sprečiti da submit forme promeni stranicu
						}
						function obrisi() {
							// parametri zahteva
							var params = {
								id: id
							}
							console.log(params)
							$.post("Filmovi/Delete", params, function(odgovor) { // POST zahtev
								console.log(odgovor)
						
								if (odgovor.status == "ok" || odgovor.status == "odbijen") {
									window.location.replace("filmovi.html") // client-side redirekcija na filmovi.html
								}
							})
							console.log("POST: " + "Filmovi/Delete")
							
							return false // sprečiti da submit forme promeni stranicu
						}
	
						formaIzmena.submit(izmeni) // registracija handler-a za izmenu
						formaBrisanje.submit(obrisi) // registracija handler-a za brisanje
					} else {
						// nije administrator
						$("table.korisnik:last").hide() // sakrij tabelu za neprijavljenog korisnika
						$('a[href="korisnici.html"]').parent().hide() // sakrij list item koji obuhvata link do korisnici.html
						formaIzmena.hide() // sakrij formu za izmenu
						formaBrisanje.hide() // sakrij formu za brisanje

						// pripremi podatke na stranci
						popuniFilm()
					}
				} else {
					// nije prijavljen
					$("table.korisnik:first").hide() // sakrij tabelu za prijavljenog korisnika
					$('a[href="korisnici.html"]').parent().hide() // sakrij list item koji obuhvata link do korisnici.html
					formaIzmena.hide() // sakrij formu za izmenu
					formaBrisanje.hide() // sakrij formu za brisanje

					// pripremi podatke na stranci
					popuniFilm()
				}
			}
		})
		console.log("GET: " + "Korisnici/PrijavljeniKorisnik")
	}
	
	popuniPrijavljenogKorisnikaIPodatke() // dobavi i ugradi u HTML informaciju o prijavljenom korisniku i podatke o žanrovima i filmu
})