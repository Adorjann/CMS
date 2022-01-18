
$(document).ready(function() {

    
    //nav bar fixed on scroll
    function convertVWtoPX(vw) {
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
      
    
    

    // galery panel
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

})

