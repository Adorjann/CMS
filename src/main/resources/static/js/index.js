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
fixedNavBar = ()=>{

	convertVWtoPX=(vw)=> {
        return vw / (100 / document.documentElement.clientWidth);
    }
    console.log(document.documentElement.clientWidth)

        if(document.documentElement.clientWidth > 600){
            $(window).scroll(function(){
                if ($(window).scrollTop() >= (convertVWtoPX(26))) {
                    $('nav').addClass('fixed-header');
                    $('nav div').addClass('visible-title');
                }
                else {
                    $('nav').removeClass('fixed-header');
                    $('nav div').removeClass('visible-title');
                }
            });
        }
}
galleryPanels = () =>{

	const panels = document.querySelectorAll('.panel');

    panels.forEach((panel)=>{
        panel.addEventListener('click',()=>{
            removeActive();
            panel.classList.add('active');
            console.log('panel clicked');
        })
    })
    removeActive = ()=>{
        panels.forEach( panel =>{
    
            panel.classList.remove('active');
        })
    }

}

$(document).ready(function() {
	popuniBaseURL() // dobavi i ugradi u HTML informaciju o baseURL-u
	popuniPrijavljenogKorisnika() // dobavi i ugradi u HTML informaciju o prijavljenom korisniku
	fixedNavBar() // dobavi informaciju o velicini ekrana klijenta i adaptira navbar
	galleryPanels() 

	$("table.korisnik:first a:last").click(odjava) // registracija handler-a za odjavu
})