document.getElementById('placements-button').addEventListener('click', ()=>{seeAllPlacements('none')});

async function seeAllPlacements(value) {

    document.getElementById('text').innerHTML = originalTextEL;
    if (value !== 'none') {
        let hiddenDeleteEL = document.querySelector("#placements-list [name='hidden-note']");
        hiddenDeleteEL.innerText = value;
        hiddenDeleteEL.style.color = 'green';
        hiddenDeleteEL.style.visibility = 'visible';
    }

    let response = await fetch('seeAllPlacements', {
        method: 'post',
        headers: new Headers({'Content-Type': 'application/json;charset=utf-8'}),
        redirect: "follow",
    });

    if (response.redirected === true) {
        window.location.href = response.url;
    } else {
        let data = await response.json();

        let element;
        let subelement;
        let tableEL = document.querySelector("#placements-list [name='table']");

        document.getElementById('sub-title').style.display = 'inline';
        document.getElementById('sub-title').innerText = 'Placements';
        document.getElementById('placements-list').style.display = 'inline';

        let i;
        for (i = 0; i < data.length; i++) {
            element = document.createElement('tr');
            let j;
            for (j = 0; j < data[i].length; j++) {
                subelement = document.createElement('td');
                subelement.innerText = data[i][j];
                element.append(subelement);
            }
            let buttonEL;

            subelement = document.createElement('td');
            buttonEL = document.createElement('button');
            buttonEL.className = 'red-small-button';
            buttonEL.innerText = 'delete';
            buttonEL.value = data[i][0];
            buttonEL.addEventListener('click', () => {deletePlacement(buttonEL.value)});
            subelement.style.wordBreak = 'normal';
            subelement.append(buttonEL);
            element.append(subelement);

            tableEL.append(element);
        }
    }
}

async function deletePlacement(value) {
    let hiddenDeleteEL=document.querySelector("#placements-list [name='hidden-note']");

    let response = await fetch('deletePlacement', {
        method: 'post',
        headers: new Headers({'Content-Type': 'application/json;charset=utf-8'}),
        body: JSON.stringify(value),
        redirect: "follow",
    });

    if(response.redirected===true){
        window.location.href=response.url;
    }
    else {
        let data=await response.json();
        if(data==='deleted'){
            await seeAllPlacements('Deleted !');
        }
        else{
            hiddenDeleteEL.innerText=data;
            hiddenDeleteEL.style.color='red';
            hiddenDeleteEL.style.visibility='visible';
        }
    }
}