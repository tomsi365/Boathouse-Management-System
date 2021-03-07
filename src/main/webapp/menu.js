let originalTextEL=document.getElementById('text').innerHTML;

window.addEventListener('load', initMenu);

async function initMenu() {
    let response = await fetch('status', {
        method: 'post',
        headers: new Headers({'Content-Type': 'application/json;charset=utf-8'}),
        redirect: "follow",
    });

    if (response.redirected === true) {
        window.location.href = response.url;
    } else {
        let data = await response.json();
        if (data === 'not connected') {
            window.location.href = 'error.html';
        } else {
            let registrationsButtonEL=document.getElementById('registrations-button');
            let membersButtonEL=document.getElementById('members-button');
            let boatsButtonEL=document.getElementById('boats-button');
            let windowsButtonEL=document.getElementById('windows-button');
            let placementsButtonEL=document.getElementById('placements-button');
            let alertsButtonEL=document.getElementById('alerts-button');

            if (data === 'user') {
                registrationsButtonEL.style.display='block';
                registrationsButtonEL.style.left='265px';
                document.getElementById('user-text').style.display = 'inline';
            } else {
                membersButtonEL.style.display='block';
                membersButtonEL.style.left='265px';
                boatsButtonEL.style.display='block';
                boatsButtonEL.style.left='445px';
                windowsButtonEL.style.display='block';
                windowsButtonEL.style.left='625px';
                registrationsButtonEL.style.display='block';
                registrationsButtonEL.style.left='805px';
                placementsButtonEL.style.display='block';
                placementsButtonEL.style.left='985px';
                alertsButtonEL.style.display='block';
                alertsButtonEL.style.left='1165px';
                document.getElementById('manager-text').style.display = 'inline';
            }
        }
    }
}

function isWord(str){
    if(str.length==0)
        return false;
    for(let i=0;i<str.length;i++){
        if(!(str.charAt(i)>='a'&&str.charAt(i)<='z')&&
            !(str.charAt(i)>='A'&&str.charAt(i)<='Z')&&
            (str.charAt(i)!==' '))
            return false;
    }
    return true;
}

function isNumber(str){
    if(str.length==0)
        return false;
    for(let i=0;i<str.length;i++){
        if(!(str.charAt(i)>='0'&&str.charAt(i)<='9'))
            return false;
    }
    return true;
}

function isPassword(str){
    if(str.length<4)
        return false;
    return true;
}

function isEmail(str){
    for(let i=0;i<str.length;i++){
        if(str.charAt(i)==='@')
            return true;
    }
    return false;
}
