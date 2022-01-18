aside = () => {
    //******* aside
    const aside = $('aside ul')
    const articleHeadline = $('article h2');
    const articleText = $('article .text p');

    
   // REQEST 

    //all blog posts
    let params = {
        publishFROM : null,
        publishTO : null,
        headline : null,
        content: null,
        publish: true,
        softDelete: false,
        pageNum: 1
    }

    let allBlogPosts = null;
    $.get('BlogPosts',params, (response)=>{

        if(response.status == "200"){
            
            allBlogPosts = response.blogPosts;
            let pageNum = response.pageNum;
            
            //set the headlines 
            if(allBlogPosts.length > 0){
                aside.children().remove();

                allBlogPosts.forEach( blogPost => {

                    aside.append(
                        `<li>`+
                        `<img src="http://placehold.it/144x150">`+
                        `<div>${blogPost.publishDate}</div>`+
                        `<div class="caption"><caption>${blogPost.headline}</caption></div>`+
                        `</li>`
                    );
                    
                }) 
                //setting the initialy open article
                aside.children().first().addClass("active-text");
                ArticleChange(response.blogPosts, aside.children().first());

                const headlines = $('aside ul').children();

                //click eventListener
                headlines.each( (index,headline) =>{
                    $(headline).click(()=>{
                        console.log("A CLick on A HEADLINE"); 
                        //TODO: make the change of article and pictures

                        removeActiveText(headlines); //removing the css class
                        $(headline).addClass('active-text'); //adding the css class

                        ArticleChange(response.blogPosts,headline); 
                    });
                });
            }
        }else{
            console.log("BlogPosts| status : "+ response.status);
        }
    })

    //changing the article/blogPost text and headline
    ArticleChange=(allPosts,headline)=>{
        let articleHeadline = $(".blog article .naslov");
        let articleText = $(".blog article .text");
        let textAuthor = $(".blog article h3");

        let activePost = allPosts.find(post => {
            return post.headline === $(headline).text().slice(10);
        });

        //CHANGE THE ARTICLE 
        articleHeadline.text(activePost.headline);
        textAuthor.text(`Autor: ${activePost.author}`);

        //Changing the blogText
        let articleSections = activePost.blogText.split("<>");//spliting the html sections

        articleText.children().remove();
        articleSections.forEach(section => {
            let sectionMark = section.substring(section.length - 1, section.length);//getting the html tag
            
            articleText.append(
                `<${sectionMark}>`+
                `${section.substring(0,section.length -1)}`+
                `</${sectionMark}>`
            );

        });

    };
    removeActiveText=(headlines)=>{
        headlines.each((index,headline)=>{
            $(headline).removeClass('active-text');
        })
    }
}

$(document).ready( ()=> {
    console.log("DOCUMENT IS READY")
    aside();

    //pictures 

    //TODO: load pictures

    //ajax call for a big image by id below:
    const bigPicture = $('.pictures .big-picture');

    //ajax call for a big image by id above:

    const prikaziSlike = $('.pictures h3');
    const pictures = $('.pictures .pictures-container').children();

    prikaziSlike.on('click', ()=> {
        
        prikaziSlike.toggleClass('h3-rotation');
        $('.pictures').toggleClass('active-height');
        $('.pictures').css('height','15vw');
        const caption = $('.pictures .uputstvo');
        
        if(prikaziSlike.text()== "Prikaži slike")  {
            
            setTimeout( ()=> {
                pictures.each( (index, pic)=>{
                    $(pic).slideToggle(500);
                    prikaziSlike.text('Sakrij slike');
                    setTimeout(()=>{
                        caption.css('visibility','visible');
                    },500);
                    
                })
            },350);
        }else{
            
            pictures.each( (index, pic)=>{
                $('.pictures').css('height','3vw');
                $('.pictures .big-picture').css('visibility','hidden');
                caption.css('visibility','hidden');
                $(pic).slideToggle(250);
                prikaziSlike.text('Prikaži slike');
                removeBorder();
                bigPicture.attr('id','randomId');
                
            })
        }  
    })    
         setTimeout(()=>{prikaziSlike.click()},2000);
    
    //Big Picture open


    pictures.each( (index, pic)=>{
        

        $(pic).on('click',(e)=>{
            if($(e.target).attr('id') == bigPicture.attr('id')){

                removeBorder();
                console.log('id are same')
                $(e.target).removeClass('brand-border');
                $('.pictures').css('height','15vw');
                bigPicture.css('visibility','hidden');
                bigPicture.attr('id','randomId');
                $('.uputstvo').css('visibility','visible');

            }else{

                removeBorder();
                $(pic).addClass('brand-border');
                $('.pictures').css('height','42vw');
                bigPicture.css('visibility','visible');
                bigPicture.attr('id',$(e.target).attr('id'));
                $('.uputstvo').css('visibility','hidden');
            }    
        });
    })
   
    removeBorder= () => {
        pictures.each( (index, pic)=>{
            $(pic).removeClass('brand-border');
        })
    };

    
     
    
    
    
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

