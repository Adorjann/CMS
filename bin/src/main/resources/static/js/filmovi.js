var baseURL = ""
var zanrId = window.location.search.slice(1).split('&')[0].split('=')[1]; // čitanje vrednosti prvog parametra GET zahteva na ovu stranicu (zanrId-a u ovom slučaju)

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
				$('a[href="dodavanjeFilma.html"]').parent().parent().hide() // sakrij listu koja obuhvata link do dodavanjeFilma.html
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
	
	// keširajne referenci na elemente
	var tabela = $("table.tabela")
	var nazivInput = tabela.find("input[name=naziv]")
	var zanrIdSelect = tabela.find("select[name=zanrId]")
	var trajanjeOdInput = tabela.find("input[name=trajanjeOd]")
	var trajanjeDoInput = tabela.find("input[name=trajanjeDo]")

	function popuniFilmove() {
		// čitanje parametara forme za pretragu
		var naziv = nazivInput.val()
		var zanrId = zanrIdSelect.find("option:selected").val()
		var trajanjeOd = trajanjeOdInput.val()
		var trajanjeDo = trajanjeDoInput.val()

		// parametri zahteva
		var params = {
			naziv: naziv, 
			zanrId: zanrId,
			trajanjeOd: trajanjeOd,
			trajanjeDo: trajanjeDo
		}
		console.log(params)
		$.get("Filmovi", params, function(odgovor) {
			console.log(odgovor)
			
			if (odgovor.status == "ok") {
				tabela.find("tr:gt(1)").remove() // ukloni sve redove u tabeli iza forme za pretragu
				
				var filmovi = odgovor.filmovi 
				for (var itFilm in filmovi) {
					tabela.append( // kreiraj, popuni red i dodaj ga u tabelu
						'<tr>' + 
							'<td class="broj">' + (parseInt(itFilm) + 1) + '</td>' + 
							'<td><a href="film.html?id=' + filmovi[itFilm].id + '">' + filmovi[itFilm].naziv + '</a></td>' + 
							'<td><ul></ul></td>' + // lista u redu
							'<td class="broj">' + filmovi[itFilm].trajanje + '</td>' + 
							'<td>' + 
								'<a href="projekcije.html?filmId=' + filmovi[itFilm].id + '">projekcije</a>' + 
							'</td>' + 
						'</tr>'
					)
					var lista = tabela.find("tr").eq(parseInt(itFilm) + 2).find("ul") // pronađi listu u redu

					var zanroviFilma = filmovi[itFilm].zanrovi
					for (var itZanr in zanroviFilma) { // kreiraj stavke u listi i dodaj ih u listu
						lista.append(
								'<li><a href="zanr.html?id=' + zanroviFilma[itZanr].id + '">' + zanroviFilma[itZanr].naziv + '</a></li>'
						)
					}
				}
			}
		})
		console.log("GET: " + "Filmovi")
	}
	function popuniZanroveIFilmove() {
		// traži od server-a žanrove
		$.get("Zanrovi", function(odgovor) {
			console.log(odgovor)

			if (odgovor.status == "ok") {
				zanrIdSelect.find("option:gt(0)").remove() // ukloni sve opcije osim prve opcije

				var zanrovi = odgovor.zanrovi
				for (var itZanr in zanrovi) {
					var selected = (zanrovi[itZanr].id == zanrId)? "selected": ""
					zanrIdSelect.append( // kreiraj, popuni opciju i dodaj je u select
						'<option value="' + zanrovi[itZanr].id + '"' + selected + '>' + zanrovi[itZanr].naziv + '</option>'
					)
				}

				// prvo se moraju popuniti žanrovi pa onda film
				popuniFilmove()
			}
		})
		console.log("GET: " + "Zanrovi")
	}
	function popuniStatistikuFilmova() {
		// traži od server-a statistiku filmova
		$.get("Filmovi/StatistikaFilmova", function(odgovor) {
			console.log(odgovor)

			if (odgovor.status == "ok") {
				var tabela = $("table.horizontalni-meni").eq(0)

				var filmovi = odgovor.statistikaFilmova.filmovi
				if (filmovi.length > 0) {
					// ima popularnih filmova
					var lista = tabela.find("ul")
					lista.empty()

					for (var it in filmovi) { // za svaki film
						lista.append( // kreiraj i popuni item, a u njemu link do filma
								'<li>' + 
									'<a href="film.html?id=' + filmovi[it].film.id + '">' + filmovi[it].film.naziv + '</a>' + 
									'<progress value="' + filmovi[it].brojac + '" max="' + odgovor.statistikaFilmova.max + '"></progress>' + 
									'<span>' + filmovi[it].brojac + '</span>' + 
								'</li>'
						)
					}
				} else {
					tabela.hide() // sakri tabelu ako nema popularnih filmova
				}
			}
		})
		console.log("GET: " + "Filmovi/StatistikaFilmova")
	}
	function popuniPoseceneFilmove() {
		// traži od server-a posećene filmove
		$.get("Filmovi/PoseceniFilmovi", function(odgovor) {
			console.log(odgovor)

			if (odgovor.status == "ok") {
				var tabela = $("table.horizontalni-meni").eq(1)
	
				var filmovi = odgovor.poseceniFilmovi
				if (filmovi.length > 0) {
					// ima posećenih filmova
					var lista = tabela.find("ul")
					lista.empty()
					for (var it in filmovi) { // za svaki film
						lista.append( // kreiraj i popuni item, a u njemu link do filma
								'<li><a href="film.html?id=' + filmovi[it].id + '">' + filmovi[it].naziv + '</a></li>'
						)
					}
				} else {
					tabela.hide() // sakri tabelu ako nema posećenih filmova
				}
			}
		})
		console.log("GET: " + "Filmovi/PoseceniFilmovi")
	}

	// pripremi podatke na stranici
	popuniZanroveIFilmove()
	popuniStatistikuFilmova()
	popuniPoseceneFilmove()
	
	$("form").submit(function() { // registracija handler-a za pretragu
		popuniFilmove()

		return false // sprečiti da submit forme promeni stranicu
	})
})