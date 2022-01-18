
$(document).ready( ()=> {
    console.log("DOCUMENT IS READY")
    //pictures 

    //TODO: load pictures

    //ajax call for a big image by id below:
    const bigPicture = $('.pictures .big-picture');

    //ajax call for a big image by id above:

    const prikaziSlike = $('.pictures h3');
    const pictures = $('.pictures .pictures-container');
    const headline = $('.pictures .naslov');
    

    prikaziSlike.on('click', ()=> {
        //opening the pictures card
        prikaziSlike.toggleClass('h3-rotation');
        $('.pictures').toggleClass('active-height');
        $('.pictures').css('height','25vw');
        const caption = $('.pictures .uputstvo');
        
        
        //showing the pictures,caption and changing the button name
        if(prikaziSlike.text()== "Prikaži album")  {
            
            setTimeout( ()=> {
                pictures.children().each( (index, pic)=>{
                    $(pic).slideToggle(200);
                    prikaziSlike.text('Sakrij album');

                    if(bigPicture.attr('id') == "randomId"){
                        setTimeout(()=>{
                            caption.css('visibility','visible');
                        },500);
                    };
                })
                headline.css('visibility','visible');
            },500);

        }else{
            //closing everything.
            pictures.children().each( (index, pic)=>{
                $('.pictures').css('height','3vw');
                $('.pictures .big-picture').css('visibility','hidden');
                caption.css('visibility','hidden');
                $(pic).slideToggle(250);
                prikaziSlike.text('Prikaži album');
                removeBorders();
                bigPicture.attr('id','randomId');
                headline.css('visibility','hidden');
            })
        }  
    })    
        //initial onload opening of the card
         setTimeout(()=>{
             prikaziSlike.click();
             pictures.find('div:first-child').find('img').click();
             
             
        
        },50);
    
    //Big Picture open


    pictures.children().each( (index, pic)=>{
        

        $(pic).on('click',(e)=>{
            if($(e.target).attr('id') == bigPicture.attr('id')){

                removeBigPicture(e);

            }else{

                changeBigPicture(e,pic);
            }    
        });
    })

    removeBigPicture= (e)=>{
        removeBorders();
                $(e.target).removeClass('brand-border');
                $('.pictures').css('height','25vw');
                bigPicture.css('visibility','hidden');
                bigPicture.attr('id','randomId');
                $('.uputstvo').css('visibility','visible');
    };

    changeBigPicture= (e,pic)=>{

                removeBorders();
                $(pic).addClass('brand-border');
                $('.pictures').css('height','59vw');
                bigPicture.css('visibility','visible');
                bigPicture.attr('id',$(e.target).attr('id'));
                $('.uputstvo').css('visibility','hidden');

    };
   
    removeBorders= () => {
        pictures.children().each( (index, pic)=>{
            $(pic).removeClass('brand-border');
        })
    };

    
     
    //aside  

    const headlines = $('aside ul').children();
   
    headlines.each((index,headline)=>{
        $(headline).click(()=>{
            console.log("A CLick on A HEADLINE"); 
            //TODO: make the change of article and pictures
            
            $(headline).addClass('active-text',removeActiveText());
        });
    });
    removeActiveText=()=>{
        $(headlines).each((index,hl)=>{
            $(hl).removeClass('active-text');
        })
    }
    
    // galery panel vanilaJs 
    const panels = document.querySelectorAll('.panel');

    panels.forEach((panel)=>{
        panel.addEventListener('click',()=>{
            removeActive();
            panel.classList.add('active');
        });
    });
    removeActive = ()=>{
        panels.forEach( panel =>{
             panel.classList.remove('active');
        });
    };

    

});

