

 
dashboardContent = () => {

    cardsRouting();
    
    buttonsRouting();
    

}

cardsRouting = ()=>{
    //manages the cards in aside and content they provide

     let allCards = $('aside .h3Wrapper'); 
    console.log('cards routing')
     allCards.each((index,cards)=>{

         $(cards).children().each( (index,card)=> {
         
             $(card).click(()=> {
                 let cardID = $(card).attr('id');
                
                 cardsStyleChange(cardID,$(cards).children());
                 callSwitchHandler(cardID);
                
                 

                 
                 
             });
         })
     })

     cardsStyleChange=(clickedCardId,cards)=>{

         let card1 = $(cards[0]);
         let card2 = $(cards[1]);
         let card3 = $(cards[2]);
 
         $(cards).each( (index,card)=> {
             $(card).removeClass('chosenCardStyle');
         })
             
         if ((clickedCardId === "1") || (clickedCardId === "4")) {
             
                 card1.addClass('chosenCardStyle');
                 card2.css('border-bottom-right-radius','0%');
                 card2.css('border-bottom-left-radius','6px');
                 card3.css('border-bottom-left-radius','0%');
                 card3.css('border-bottom-right-radius','0%');
         }else if((clickedCardId === "2") || (clickedCardId === "5")){  
 
                 card2.addClass('chosenCardStyle');
                 card3.css('border-bottom-left-radius','6px');
                 card3.css('border-bottom-right-radius','0px');
                 card1.css('border-bottom-right-radius','6px');
                 card1.css('border-bottom-left-radius','0px');
 
                 
         }else{ 
                 card3.addClass('chosenCardStyle');
                 card2.css('border-bottom-right-radius','6px');
                 card2.css('border-bottom-left-radius','0%');
                 card1.css('border-bottom-left-radius','0%');
                 card1.css('border-bottom-right-radius','0%');
                 
            
         }
     };
     callSwitchHandler=(clickedCardId)=>{

        let albumOrBlog = $('.chosenCardStyle:first').text();
        let publishOrNot = $('.chosenCardStyle:last').attr('id');
        console.log(albumOrBlog);
 
         switch (clickedCardId) {
             case "1":

                 break;
             case "2":    
                 albumHandler(publishOrNot);
                 break;
             case "3": 
                 blogHandler(publishOrNot);
                 break;
             case "4":
                 if(albumOrBlog == "Album"){
                    albumHandler(clickedCardId);
                 }else{
                    blogHandler(clickedCardId);
                 }
                 break;
             case "5":
                if(albumOrBlog == "Album"){
                    albumHandler(clickedCardId);
                 }else{
                    blogHandler(clickedCardId);
                 }
                 break;
             case "6":
                if(albumOrBlog == "Album"){
                    albumHandler(clickedCardId);
                 }else{
                    blogHandler(clickedCardId);
                 }
                 
                 break;
             default:
                 break;    
         }
     };


     
     
};

albumHandler= (cardId) => {
    $('#preview').click();
    const  headlinesContainer = $('aside ul');
    let contentBoard = $('.content');

    //removing content below buttons
    if(contentBoard.children().length > 1) {
        $('.content').children().last().remove();
    }
    

        let params={
            publishFROM : null,
            publishTO : null, 
            albumName : null,
            publish : true,
            softDelete : false,
            pageNum : 0
        }

        if(cardId == 4){
            params.publish = true;
        }else if(cardId == 5){
            params.publish = false;
        }else if(cardId == 6){
            params.publish = false;
            params.softDelete = true;
        }
        console.log(params);
        $.get(`Albums`,params, (response)=>{
            if(response.status == 200) {
                console.log(`albumHandler |response status: 200`)
                console.log(response);

                let allAlbums = response.albums;

                //setting the headlines in aside
                let headlinesAndIds = [];
                allAlbums.forEach(album =>{

                    listItem = {
                        id : album.id,
                        headline : album.albumName
                    };
                    headlinesAndIds.push(listItem);
                });
                asideListManagement(headlinesAndIds,"album");
                
                //aside headline click handler
                let headlines =  headlinesContainer.children();
                
                $(headlines).each( (index,headline) =>{
                    $(headline).click(() =>{ 

                        removeActiveText(headlines); //removing the css class
                        $(headline).addClass('active-text'); //adding the css class

                        //changing the album in .content
                        albumChange(allAlbums, headline );

                        $('#preview').click();
                    })
                    
                });
                let firstItem = headlines.first();
                    firstItem.click();

            }
        });
        
        const pictures = $('.pictures .pictures-container');
         const headline = $('.pictures .naslov');


        pictures.children().each( (index, pic)=>{
            $(pic).slideDown();
            $(pic).click(()=>{
                removeBorder(pictures);
                $(pic).addClass("brand-border");
                //TODO: offer full screen, set cover, delete
            })
        });    
        
    
    albumChange = (allAlbums, clickedHeadline) =>{

        let activeAlbum = allAlbums.find(album => {
            return album.albumName === $(clickedHeadline).text();
        });

        $('section .pictures').remove();

        $('.content').append( 
            `<div class="pictures">`+
                `<h2 class="naslov">${activeAlbum.albumName}</h2>`+
                
                `<div class="pictures-container">`+
                 `   <div class="picture">`+
                        `<img id="1" src="http://placehold.it/144x150">`+
                    `</div>`+
                    `<div class="picture">`+
                        `<img id="2" src="http://placehold.it/144x150">`+
                    `</div>`+
                    `<div class="picture">`+
                        `<img id="3" src="http://placehold.it/144x150">`+
                    `</div>`+
                    `<div class="picture">`+
                        `<img id="4" src="http://placehold.it/144x150">`+
                    `</div>`+
                    `<div class="picture">`+
                        `<img id="5" src="http://placehold.it/144x150">`+
                   ` </div>`+
                   ` <div class="picture">`+
                    `    <img id="6" src="http://placehold.it/144x150">`+
                   ` </div>`+
                   ` <div class="picture">`+
                    `    <img id="7" src="http://placehold.it/144x150">`+
                    `</div>`+
                   ` <div class="picture">`+
                   `     <img id="8" src="http://placehold.it/144x150">`+
                    `</div>`+
                    `<div class="picture">`+
                    `    <img id="9" src="http://placehold.it/144x150">`+
                   ` </div>`+
                   ` <div class="picture">`+
                     `   <img id="10" src="http://placehold.it/144x150">`+
                   ` </div>`+
                `</div>`+
           ` </div>`
        );
        
        params={
            publishFROM : null,
            publishTO : null,
            imageAlbumId : activeAlbum.id,
            publish : false,
            softDelete: false,
            pageNum: 0
        }
        if(activeAlbum.publish == true){
            params.publish = true;
        }else if(activeAlbum.softDelete == true){
            params.softDelete = true;
        }

        $.get(`Images`,params, (response) => {

            if(response.status == 500) {
                console.log(`${response.status}<-status -- ${response.exception}   <--`);
            }
            
            

            if(response.status == 200 && response.images.length > 0){
                console.log(`--> IMA SLIKA <--`);

                let pictureContainer = $('.pictures-container');

                let imagesList = response.images;
                pictureContainer.children().remove();
                $(imagesList).each( (index,imageDto)=>{
                    

                    pictureContainer.append(

                        ` <div class="picture">`+
                        `   <img id="${imageDto.id}" src="data:image/png;base64,${imageDto.realPicture}">`+
                        ` </div> `
                        
                    );

                })




            }else if(response.status == 404){

                console.log(`--> Nema slikeee <--`);
            }
        })

    }
    
    
};

blogHandler = (cardId) => {
    $('#preview').click();
    const asideList = $('.aside-ul-container ul');
    let contentBoard = $('.content');

    if(contentBoard.children().length > 1) {
        $('.content').children().last().remove();
    }
    
    

        let params = {
            publishFROM : null,
            publishTO : null,
            headline : null,
            content: null,
            publish: true,
            softDelete: false,
            pageNum: 0
        }

        if(cardId == 4){
            params.publish = true;
        }else if(cardId == 5){
            params.publish = false;
        }else if(cardId == 6){
            params.publish = false;
            params.softDelete = true;
        }

        let allBlogPosts = null;
        $.get('BlogPosts',params, (response)=>{
    
            if(response.status == "200"){
                console.log(`blogHandler |response status: 200`)
                
                allBlogPosts = response.blogPosts;
                let pageNum = response.pageNum;
                
                //set the headlines list
                if(allBlogPosts.length > 0){

                    let headlinesAndIds = [];
                    allBlogPosts.forEach(bPost =>{

                    listItem = {
                        id : bPost.id,
                        headline : bPost.headline
                    };
                    headlinesAndIds.push(listItem);
                });
                asideListManagement(headlinesAndIds,"blog");
                    
                    
                    const headlines = $('aside ul').children();
    
                    //headlines click event
                    headlines.each( (index,headline) =>{
                        $(headline).click(()=>{ 

                            removeActiveText(headlines); //removing the css class
                            $(headline).addClass('active-text'); //adding the css class
    
                            ArticleChange(response.blogPosts,headline); 
                        });
                    });

                    let firstItem = headlines.first();
                    firstItem.click();

                }
            }else{
                //TODO: Enter some dummy content 
            }
        })
    
    

    //changing the article/blogPost text and headline
    ArticleChange=(allPosts,headline)=>{
        const content = $('.content');

        
        let activePost = allPosts.find(post => {
            return post.headline === $(headline).text()
        });
        
        $('article').remove();
        content.append(
            
             `<article>`+
              `  <h2 class="naslov"> ${activePost.headline}</h2>`+
                `<div class="text">`+
                    
               
            `</article>`        )

        let articleText = $(".blog article .text");
        
        
        let blogPostData = JSON.parse(activePost.blogText) // Blog Post Data Object

        articleText.children().remove();
        blogPostData.blocks.forEach(block => {
            
            let sectionTag = getSectionTag(block);

            articleText.append(
                `<${sectionTag}>`+
                `${block.data.text}`+
                `</${sectionTag}>`
            );

        });

    };
    getSectionTag =(block)=> {

        switch(block.type) {
            case "paragraph":
                return "p";
                
            case "header":
                return `h${block.data.level}`;

                  
        }
    }
};

buttonsRouting = () => {
    //buttons routing
    buttonsContainer = $('.buttons');
    buttonsList = buttonsContainer.children();


    $(buttonsList).each( (index,button) => {
        $(button).click(() => {
            let buttonId = $(button).children().attr('id');   
            let chosenCard = $('.chosenCardStyle').first().text();

            lastActiveButton = $('.button.active-text').children().attr('id');
            console.log(`LAST ACTIVE BUTTON: ${lastActiveButton}`);

            if(buttonId == `preview`){
                if(lastActiveButton != "preview"){

                    $('.editAlbumButtons').remove();
                    removeActiveText(buttonsList);
                    $(button).addClass('active-text');

                    let headlineId =$('aside ul li.active-text').attr('id'); 
                    
                    if(headlineId != undefined){
                        console.log(`Not undefined. headlineId: ${headlineId}`);
                        $('aside ul li.active-text').click();
                        
                    }else{
                        console.log(`headlineId is undefined : ${headlineId}`);
                        $('aside ul li:first').click();
                    }
                    $('#edit').parent().css('visibility','visible');
                }

                

            }else if(buttonId == `new`){
                if(lastActiveButton != "new"){

                        console.log(`new`);
                    chosenCard == `Album`? newAlbum() : newBlogPost(); 
                    removeActiveText($('aside ul').children());
                    removeActiveText($(buttonsList));
                    $(button).addClass('active-text');

                    $('#edit').parent().css('visibility','hidden');
                }
                

            }else{
                if(lastActiveButton != "edit"){

                    console.log(`edit`);

                    chosenCard == `Album`? editAlbum() : editBlogPost();

                    removeActiveText($(buttonsList));
                    $(button).addClass('active-text');
                }
                
            }

            
        })
    })

};

newAlbum=()=> {


    console.log(`newAlbum function is called`);
    let pictures = $('.pictures');

    pictures.children().remove();
    $('.editAlbumButtons').remove();
    
    pictures.append(
        `<form class:"naslovAlbuma">`+
        `    <input type="text" autocomplete="off" name="nazivAlbuma" placeholder="Naziv Albuma.." required>`+
        `     <input type="submit" value="Nastavi">`+
        `</form>`
    );


    $("form").eq(0).submit((e)=> {
        console.log('pritisnut submit')

        let newAlbumHeadline = $('form input:text').val();

        pictures.children().slideUp("500", ()=> {

            let albumCreated = $(
                `<h2 class="naslov">${newAlbumHeadline}</h2>`+
                    
                    `<div class="pictures-container">`+
                     `   <div class="picture">`+
                            `<img id="1" src="http://placehold.it/144x150">`+
                        `</div>`+
                        `<div class="picture">`+
                            `<img id="2" src="http://placehold.it/144x150">`+
                        `</div>`+
                        `<div class="picture">`+
                            `<img id="3" src="http://placehold.it/144x150">`+
                        `</div>`+
                        `<div class="picture">`+
                            `<img id="4" src="http://placehold.it/144x150">`+
                        `</div>`+
                        `<div class="picture">`+
                            `<img id="5" src="http://placehold.it/144x150">`+
                       ` </div>`+
                       ` <div class="picture">`+
                        `    <img id="6" src="http://placehold.it/144x150">`+
                       ` </div>`+
                       ` <div class="picture">`+
                        `    <img id="7" src="http://placehold.it/144x150">`+
                        `</div>`+
                       ` <div class="picture">`+
                       `     <img id="8" src="http://placehold.it/144x150">`+
                        `</div>`+
                        `<div class="picture">`+
                        `    <img id="9" src="http://placehold.it/144x150">`+
                       ` </div>`+
                       ` <div class="picture">`+
                         `   <img id="10" src="http://placehold.it/144x150">`+
                       ` </div>`+
                    `</div>`
            );
            albumCreated.appendTo(pictures).slideDown("500");
        });

        pictures.animate({
            bottom: "15%"
        },500,()=>{

            $('.content').append(

                `<div class="backSave">`+
                    `<div class="backSaveButtons" ><h3 id="back">Poništi</h3></div>`+
                    `<div class="backSaveButtons" ><h3 id="save">Sačuvaj</h3></div>`+
                `</div>`
            );



            $('.backSaveButtons:last').click(()=>{

                params={
                    albumName: newAlbumHeadline 
                };
            
                $.post('Albums/Create',params, (response)=>{
            
                    if(response.status == 201){
                        console.log(`uspesno snimljen novi album`);

                        $(".backSave").remove();
                        $('.h3Wrapper h3:nth-child(2)').click();
                        $('#5').click();
                        
                        
                            setTimeout(()=>{
                                $('aside ul').children().each((index,headline)=>{
                                if($(headline).text() == response.newImageAlbum.albumName){
                                    $(headline).click();
                                    $('.button:last').click();
                                }
                            })
                            },50);
                            
                        
                        
                    }else{
                        console.log(`NEUSPESNO snimljen novi albun`)
                    }
                    
                })
                    

                
            })
    
            $('.backSaveButtons:first').click(()=>{
    
                console.log("you clicked BACK");
                $(".backSave").remove();
                $('.button:nth-child(2)').click();
            })
        });
        
        



        
        



        e.preventDefault();
    });
    

};

editAlbum= ()=> {


    //styling the Album
        let pictures = $('.pictures');
        let picturesContainer = $('.pictures-container');

        pictures.prepend('<div class="editAlbumDiv"></div>');
        $('.editAlbumDiv').prepend( $('.naslov') );
        
        pictures.append('<div class="editAlbumDiv"></div>');
        $('.editAlbumDiv:last').prepend($('.pictures-container'));

        picturesContainer.css('grid-template-columns','repeat(5, 1fr)');
        picturesContainer.css('grid-template-rows','repeat(2, 1fr)');
        picturesContainer.css('overflow-y','auto');
        picturesContainer.css('overflow-x','hidden');
        picturesContainer.css('padding','1vh 2vw');

        let choosenCardId = $('.chosenCardStyle').last().attr('id');
        let buttonName =()=>{ 
           
            if(choosenCardId == 5){
               return "Objavi album";
           }else {
               return "Premesti u pripremu";
           }
           

        }
    
        $('.buttonsContainer').append(
            `<div class="editAlbumButtons">`+
            `   <div id="Dodaj sliku" class="editButtonsContainer">`+
            `       <img class="buttonIcon" src="images/photo.png" >`+
            `       <input type="file" accept=".jpg, .jpeg, .png" />`+
            `   </div>`+
            `   <div id="${buttonName()}" class="editButtonsContainer">`+
            `       <img class="buttonIcon" src="images/publishing.png" >`+
            `   </div>`    +
            `   <div id="Obrisi Album" class="editButtonsContainer">`+
            `       <img class="buttonIcon" src="images/trash.png" >`+
            `   </div>`    +
            `</div>`
        );
        $('.editAlbumDiv:first').append(
                `<div class="buttonsDescription">`+
                `    <h3 class="buttonDescriptionH3"> TESTIRAMO  </h3>`+
                `</div>`
        );

        $('.editButtonsContainer ').hover((e)=>{
            $('.buttonDescriptionH3').text(()=>{
                
                $('.buttonsDescription').css('visibility', 'visible');
                return e.target.id;
            });
            
            
            console.log(e.target.id);
        },()=>{
            
            $('.buttonsDescription').css('visibility', 'hidden');
        });
         
       const input=$('input')    
    $('input').change(()=>{
                                                //When a picture is chosen from filesystem
        $('.pictures-container').remove();
        $('.editAlbumButtons').remove();
        $('.buttonsDescription:first').css('visibility', 'hidden');
        $('.editAlbumDiv:last').prepend('<div class="uploadFilePreview"></div>');

        let filePreview = $('.uploadFilePreview');

        const uFiles = input.prop('files')

        if(uFiles.length === 0){
            const para = document.createElement('p');
            para.textContent = 'No picture selected for upload';
            filePreview.append(para);
        }else{
            const list = document.createElement('ul');
            filePreview.append($(list));

            for(const file of uFiles){
                const listItem = document.createElement('li');
                const para = document.createElement('p');
                if(validFileType(file)){
                    //Success picture display
                    const listDetails = document.createElement('ul');
                        const fileName = document.createElement('li');
                        const fileNameH3 = document.createElement('h3');
                        fileNameH3.textContent = `file name:    ${file.name}`;
                        fileName.append(fileNameH3);
                        

                        const fileSize = document.createElement('li');
                        const fileSizeH3 = document.createElement('h3');
                        fileSizeH3.textContent = `file size:    ${returnFileSize(file.size)}`;
                        fileSize.append(fileSizeH3);
                        
                        listDetails.append(fileName);
                        listDetails.append(fileSize);

                    const image = document.createElement('img');
                    image.src = URL.createObjectURL(file);

                    $(listItem).append(image);
                    $(listItem).append(listDetails);

                    //prompting the back or save buttons
                    backSaveButtons($('.content'),()=>{
                        // SAVE button function:
                        let theAlbumId = $('li.active-text').attr('id');
                        console.log(`Uploading pictures to Album ID: ${theAlbumId} `);
                        
                        let fd = new FormData();
                        fd.append(`file`,file);
                        fd.append(`albumId`,theAlbumId);
                       
                        
                         $.ajax({
                             type: "POST",
                             url: `Images/Create`,
                             data: fd,
                             processData: false,
                             contentType: false,
                             success: function (response){
                                 console.log(response.status);
                                 if(response.status == 401){
                                     console.log(response.exception);
                                 }else if(response.status == 201){
                                    $(".backSave").remove();
                                    ('aside ul li.active-text').click();$
    
                                 }
                            }
                         })
                    },()=>{
                        //BACK button function: 

                        console.log("you clicked BACK");
                        $(".backSave").remove();
                        $('#preview').parent().click();
                        $('#edit').parent().click();

                    });
                    
                }else{
                    //Wrong type picture
                    para.textContent = `File name ${file.name}: Not a valid file type. Update your selection.`;
                    $(listItem).append(para);
                }
                $(list).append($(listItem).attr('id', 'pregledSlike'));
            }
        }

        
    })

	
	

    buttonsPublishAndDelete("Albums",choosenCardId);

    if(choosenCardId == 6){
        $(".editButtonsContainer:first").remove();
        $(".editAlbumButtons").css('justify-content','flex-end');
        $(".editButtonsContainer:last").css('margin-left','1.2em')
        $(".editButtonsContainer:last").attr('id',"Obrisi Zauvek");
    }
    
    
        

           
        fileTypes = [
            "image/jpeg",
            "image/pjpeg",
            "image/png",      
        ];
          
        validFileType=(file)=> {
                return fileTypes.includes(file.type);
        };   
        returnFileSize=(number)=> {
                if(number < 1024) {
                return number + 'bytes';
                } else if(number >= 1024 && number < 1048576) {
                return (number/1024).toFixed(1) + 'KB';
                } else if(number >= 1048576) {
                return (number/1048576).toFixed(1) + 'MB';
                }
        };    
        
};
const buttonsPublishAndDelete=(requestMapping,choosenCardId)=>{

    //this is the second button for publish / unpublish
    $(".editButtonsContainer:nth-of-type(2)").click(()=> {
        // choosenCardId is 4,5 or 6 (publish,notPublish,deleted);
        if(choosenCardId == 4){
            //UNpublishing the album
			let params ={
                id : $('li.active-text').attr('id'),
                publish : false
            }
            $.post(`${requestMapping}/Publish`, params,(response)=> {

                if(response.status == 200) {

                    $('#5').click();    
                }
            })

        }else if(choosenCardId == 5){
	
			let params ={
                id : $('li.active-text').attr('id'),
                publish : true
            }
            $.post(`${requestMapping}/Publish`, params,(response)=> {

                if(response.status == 200) {

                    $('#4').click();    
                }
            })
        }else{
			let params ={
                id : $('li.active-text').attr('id'),
                softDelete : false
            }
            $.post(`${requestMapping}/SoftDelete`, params,(response)=> {

                if(response.status == 200) {

                    $('#5').click();    
                }
            })
			
	
		}

    })

    //this is the third button for delete
    $(".editButtonsContainer:last").click(()=> {
        
        if(choosenCardId == 4){
            //from publish to deleted
            let params ={
                id : $('li.active-text').attr('id'),
                softDelete : true
            }
            $.post(`${requestMapping}/SoftDelete`, params,(response)=> {

                if(response.status == 200) {

                    $('#6').click();    
                }
            })

        }else if(choosenCardId == 5){
            //from unPublished to delted
            let params ={
                id : $('li.active-text').attr('id'),
                softDelete : true
            }
            $.post(`${requestMapping}/SoftDelete`, params,(response)=> {

                if(response.status == 200) {

                    $('#6').click();    
                }
            })

        }else{
            //from deleted to permanently deleted
            let params ={
                id : $('li.active-text').attr('id')
            }
            $.post(`${requestMapping}/Delete`, params,(response)=> {

                if(response.status == 200) {

                    $('#6').click();    
                }
            })
            
        }
        
    })
}

            
const backSaveButtons=(jObject,save,back)=>{

    /*parameters: -JQuery object to append buttons to.
              -function to perform for click on button "Potvrdi"(save)
              -function to perform for click on button  "Nazad" (back)
              -message to display above buttons
    */ 
            
    
    

        jObject.append(

            `<div class="backSave">`+
                `<div class="backSaveButtons" ><h3 id="back">Poništi</h3></div>`+
                `<div class="backSaveButtons" ><h3 id="save">Sačuvaj</h3></div>`+
            `</div>`
        );   


        $('.backSaveButtons:last').click(()=>{
              
            save();
        })

        $('.backSaveButtons:first').click(()=>{
            
            back();
        })
    

}


newBlogPost=()=> {
    console.log(`newBlogPost function is called`);
    
    if($('.content').children().length > 1) {
        while($('.content').children().length != 1){
            $('.content').children().last().remove();
        }
        
    }
    
    let article = $(document.createElement('article'));
    $('.content').append(article);

    article.children().remove();
    $('.editAlbumButtons').remove();
    
    article.append(
        `<form class:"naslovAlbuma" style="margin-top:10%;">`+
        `    <input type="text" autocomplete="off" name="nazivBloga" placeholder="Naslov blog post-a.." required>`+
        `     <input type="submit" value="Nastavi">`+
        `</form>`
    );

    var editor = null;
    $("form").eq(0).submit((e)=> {
        console.log('pritisnut submit')

        let newBlogPostHeadline = $('form input:text').val();
        
        article.children().slideUp("500", ()=> {

            let blogCreated = $(
                `<h2 class="naslov">${newBlogPostHeadline}</h2>`+
                    
                    `<div id="editorHolder" class="text"></div>`
            );
            blogCreated.appendTo(article).slideDown("500");
             editor = new EditorJS({
                holder: 'editorHolder',
            
                tools:{
                    header:Header,
                    paragraph: {
                     class: Paragraph,
                     inlineToolbar: true,
                   }
                },
                
             }
            );
        });
        

        article.animate({
            bottom: "11%",
            height: "64%"
        },500,()=>{

            
            backSaveButtons($(".content"),()=>{
                //button save was clicked
                editor.save().then((outputData) => {
                    
                    params={
                        headline: newBlogPostHeadline,
                        blogText: JSON.stringify(outputData)
                    };
                    $.post('BlogPosts/Create',params, (response)=>{
            
                        if(response.status == 201){
                            console.log(`uspesno snimljen novi blog post`);
    
                            $(".backSave").remove();
                            $('#5').click();
                            
                            
                                setTimeout(()=>{
                                    $('aside ul').children().each((index,headline)=>{
                                    if($(headline).text() == response.newBlogPost.headline){
                                        $(headline).click();
                                        $('.button:last').click();
                                    }
                                })
                                },50);
                                
                            
                            
                        }else{
                            console.log(`NEUSPESNO snimljen novi albun`)
                        }
                        
                    })

                }).catch((error) => {
                    console.log('Saving failed: ', error)
                    
                });

            },()=>{
                //button back was clicked
                console.log("you clicked BACK");
                $(".backSave").remove();
                $('.button:nth-child(2)').click();
            })


           
        });
        
        



        
        



        e.preventDefault();
    })    
};
editBlogPost=()=>{};

asideListManagement = (list, cardCaller ) =>{

    const asideList = $('.aside-ul-container ul');

    if(list.length > 0) {
        asideList.children().remove();

        list.forEach((listItem)=>{
            
            asideList.append(
                `<li id="${listItem.id}">`+
                    `<caption>${listItem.headline}</caption>`+
                `</li>`
            );
        });
    }

    

};

removeActiveText=(headlines)=>{
    headlines.each((index,headline)=>{
        $(headline).removeClass('active-text');
    })
}

removeBorder=(list)=> {

    list.children().each( (index, item)=>{
        $(item).removeClass('brand-border');
    })
}


$(document).ready( ()=> {
    console.log("DOCUMENT IS READY")

     dashboardContent();

     $('#3').click();
     $('#4').click();
     

     
    
    

    

});

