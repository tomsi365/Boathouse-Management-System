document.getElementById('members-button').addEventListener('click', ()=>{seeAllMembers('none')});

async function seeAllMembers(value){

    document.getElementById('text').innerHTML=originalTextEL;
    if(value!=='none'){
        let hiddenDeleteEL=document.querySelector("#members-list [name='hidden-note']");
        hiddenDeleteEL.innerText=value;
        hiddenDeleteEL.style.color='green';
        hiddenDeleteEL.style.visibility='visible';
    }

    let response=await fetch('seeAllMembers', {
        method:'post',
        headers:new Headers({'Content-Type':'application/json;charset=utf-8'}),
        redirect:"follow",
    });

    if(response.redirected===true){
        window.location.href=response.url;
    }
    else {
        let data = await response.json();

        let element;
        let subelement;
        let tableEL = document.querySelector("#members-list [name='table']");

        document.getElementById('sub-title').style.display='inline';
        document.getElementById('sub-title').innerText='Members';
        document.getElementById('members-list').style.display='inline';
        document.querySelector("#members-list [name='add']").addEventListener('click', addMember);

        let i;
        for (i = 0; i < data.length; i++) {
            element=document.createElement('tr');
            let j;
            for (j = 0; j < data[i].length; j++) {
                subelement=document.createElement('td');
                subelement.innerText=data[i][j];
                element.append(subelement);
            }
            let buttonEL;

            subelement=document.createElement('td');
            buttonEL=document.createElement('button');
            buttonEL.className='yellow-small-button';
            buttonEL.innerText='edit';
            buttonEL.value=data[i][0];
            let member=data[i];
            buttonEL.addEventListener('click', () => {selectNonOwnedBoat(member)});
            subelement.style.wordBreak= 'normal';
            subelement.append(buttonEL);
            element.append(subelement);

            subelement=document.createElement('td');
            buttonEL=document.createElement('button');
            buttonEL.className='red-small-button';
            buttonEL.innerText='delete';
            buttonEL.value=data[i][0];
            buttonEL.addEventListener('click', () => {deleteMember(buttonEL.value)});
            subelement.style.wordBreak= 'normal';
            subelement.append(buttonEL);
            element.append(subelement);

            tableEL.append(element);
        }
    }
}

function addMember(){
    document.getElementById('text').innerHTML=originalTextEL;
    document.getElementById('sub-title').style.display='inline';
    document.getElementById('sub-title').innerText='Add a Member';
    document.getElementById('member-form').style.display='inline';
    document.querySelector("#member-form [name='submit']").addEventListener('click',validateAddMemberForm);
}

async function validateAddMemberForm(){
    let hiddenFeedbackEL=document.querySelector("#member-form [name='hidden-feedback']");
    hiddenFeedbackEL.innerText='';
    hiddenFeedbackEL.style.visibility='hidden';
    let flag=true;

    let nameEL=document.querySelector("#member-form [name='name']");
    let hiddenNameEL=document.querySelector("#member-form [name='hidden-name']");
    if(isWord(nameEL.value)!=true){
        hiddenNameEL.innerText='Only letters !';
        hiddenNameEL.style.visibility='visible';
        flag=false;
    }
    else{
        hiddenNameEL.innerText='';
        hiddenNameEL.style.visibility='hidden';
    }

    let phoneEL=document.querySelector("#member-form [name='phone']");
    let hiddenPhoneEL=document.querySelector("#member-form [name='hidden-phone']");
    if(isNumber(phoneEL.value)!=true){
        hiddenPhoneEL.innerText='Only numbers !';
        hiddenPhoneEL.style.visibility='visible';
        flag=false;
    }
    else{
        hiddenPhoneEL.innerText='';
        hiddenPhoneEL.style.visibility='hidden';
    }

    let emailEL=document.querySelector("#member-form [name='email']");
    let hiddenEmailEL=document.querySelector("#member-form [name='hidden-email']");
    if(isEmail(emailEL.value)!=true){
        hiddenEmailEL.innerText='must has @ !';
        hiddenEmailEL.style.visibility='visible';
        flag=false;
    }
    else{
        hiddenEmailEL.innerText='';
        hiddenEmailEL.style.visibility='hidden';
    }

    let passwordEL=document.querySelector("#member-form [name='password']");
    let hiddenPasswordEL=document.querySelector("#member-form [name='hidden-password']");
    if(isPassword(passwordEL.value)!=true){
        hiddenPasswordEL.innerText='At least 4 characters !';
        hiddenPasswordEL.style.visibility='visible';
        flag=false;
    }
    else{
        hiddenPasswordEL.innerText='';
        hiddenPasswordEL.style.visibility='hidden';
    }

    let isManager=document.querySelector("#member-form [name='is-manager']").checked;
    let age=document.querySelector("#member-form [name='age']").value;
    let expiryDate=document.querySelector("#member-form [name='date']").value;
    let notes=document.querySelector("#member-form [name='notes']").value;

    let level;
    if(document.querySelector("#member-form [name='level'][value='NORMAL']").checked){
        level='NORMAL';
    }
    else if(document.querySelector("#member-form [name='level'][value='EXPERT']").checked){
        level='EXPERT';
    }
    else {
        level='BEGINNER';
    }

    const Member = {
        name: nameEL.value,
        age: age,
        level: level,
        expiryDate: expiryDate,
        phone: phoneEL.value,
        email: emailEL.value,
        password: passwordEL.value,
        isManager: isManager,
        notes : notes
    }

    if(flag===true){
        let response=await fetch('addMember', {
            method:'post',
            headers:new Headers({'Content-Type':'application/json;charset=utf-8'}),
            body:JSON.stringify(Member),
            redirect:"follow",
        });

        if(response.redirected===true){
            window.location.href=response.url;
        }
        else {
            let data=await response.json();
            if(data==='added'){
                await seeAllMembers('Added !');
            }
            else{
                hiddenFeedbackEL.innerText=data;
                hiddenFeedbackEL.style.visibility='visible';
            }
        }
    }
}

async function deleteMember(value) {
    let hiddenDeleteEL=document.querySelector("#members-list [name='hidden-note']");

    let response = await fetch('deleteMember', {
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
            await seeAllMembers('Deleted !');
        }
        else{
            hiddenDeleteEL.innerText=data;
            hiddenDeleteEL.style.color='red';
            hiddenDeleteEL.style.visibility='visible';
        }
    }
}

function editMember(member,boat) {

    document.getElementById('text').innerHTML=originalTextEL;
    document.getElementById('sub-title').style.display='inline';
    document.getElementById('sub-title').innerText='Edit a Member';
    document.getElementById('member-form').style.display='inline';
    let nameEL=document.querySelector("#member-form [name='name']");
    nameEL.value=member[1];
    let phoneEL=document.querySelector("#member-form [name='phone']");
    phoneEL.value=member[5];
    let emailEL=document.querySelector("#member-form [name='email']");
    emailEL.value=member[2];
    let passwordEL=document.querySelector("#member-form [name='password']");
    passwordEL.value=member[3];
    let isManagerEL=document.querySelector("#member-form [name='is-manager']");
    if(member[11]==='true'){
        isManagerEL.checked=true;
    }
    else{
        isManagerEL.checked=false;
    }
    let ageEL=document.querySelector("#member-form [name='age']");
    ageEL.value=member[4];
    let level=member[6];
    document.querySelector("#member-form [name='level'][value="+level+"]").checked=true;
    let expiryDateEL=document.querySelector("#member-form [name='date']");
    expiryDateEL.value=member[10];
    let notesEL=document.querySelector("#member-form [name='notes']");
    notesEL.value=member[12];
    document.querySelector("#member-form [name='submit']").addEventListener('click',()=>{validateEditMemberForm(member,boat)});
}

async function validateEditMemberForm(member, boat) {
    let hiddenFeedbackEL = document.querySelector("#member-form [name='hidden-feedback']");
    hiddenFeedbackEL.innerText = '';
    hiddenFeedbackEL.style.visibility = 'hidden';
    let flag = true;

    let nameEL = document.querySelector("#member-form [name='name']");
    let hiddenNameEL = document.querySelector("#member-form [name='hidden-name']");
    if (isWord(nameEL.value) != true) {
        hiddenNameEL.innerText = 'Only letters !';
        hiddenNameEL.style.visibility = 'visible';
        flag = false;
    } else {
        hiddenNameEL.innerText = '';
        hiddenNameEL.style.visibility = 'hidden';
    }

    let phoneEL = document.querySelector("#member-form [name='phone']");
    let hiddenPhoneEL = document.querySelector("#member-form [name='hidden-phone']");
    if (isNumber(phoneEL.value) != true) {
        hiddenPhoneEL.innerText = 'Only numbers !';
        hiddenPhoneEL.style.visibility = 'visible';
        flag = false;
    } else {
        hiddenPhoneEL.innerText = '';
        hiddenPhoneEL.style.visibility = 'hidden';
    }

    let emailEL = document.querySelector("#member-form [name='email']");
    let hiddenEmailEL = document.querySelector("#member-form [name='hidden-email']");
    if (isEmail(emailEL.value) != true) {
        hiddenEmailEL.innerText = 'must has @ !';
        hiddenEmailEL.style.visibility = 'visible';
        flag = false;
    } else {
        hiddenEmailEL.innerText = '';
        hiddenEmailEL.style.visibility = 'hidden';
    }

    let passwordEL = document.querySelector("#member-form [name='password']");
    let hiddenPasswordEL = document.querySelector("#member-form [name='hidden-password']");
    if (isPassword(passwordEL.value) != true) {
        hiddenPasswordEL.innerText = 'At least 4 characters !';
        hiddenPasswordEL.style.visibility = 'visible';
        flag = false;
    } else {
        hiddenPasswordEL.innerText = '';
        hiddenPasswordEL.style.visibility = 'hidden';
    }

    let isManager = document.querySelector("#member-form [name='is-manager']").checked;
    let age = document.querySelector("#member-form [name='age']").value;
    let expiryDate = document.querySelector("#member-form [name='date']").value;
    let notes = document.querySelector("#member-form [name='notes']").value;

    let level;
    if(document.querySelector("#member-form [name='level'][value='NORMAL']").checked){
        level='NORMAL';
    }
    else if(document.querySelector("#member-form [name='level'][value='EXPERT']").checked){
        level='EXPERT';
    }
    else {
        level='BEGINNER';
    }

    const Member = {
        id: member[0],
        name: nameEL.value,
        age: age,
        level: level,
        expiryDate: expiryDate,
        phone: phoneEL.value,
        email: emailEL.value,
        password: passwordEL.value,
        isManager: isManager,
        notes: notes,
        serialNumber : boat
    }

    if (flag === true) {
        let response = await fetch('editMember', {
            method: 'post',
            headers: new Headers({'Content-Type': 'application/json;charset=utf-8'}),
            body: JSON.stringify(Member),
            redirect: "follow",
        });

        if (response.redirected === true) {
            window.location.href = response.url;
        } else {
            let data = await response.json();
            if (data === 'edited') {
                await seeAllMembers('Edited !');
            } else {
                hiddenFeedbackEL.innerText = data;
                hiddenFeedbackEL.style.visibility = 'visible';
            }
        }
    }
}

async function addSelectMember(type,date,window){

    document.getElementById('text').innerHTML=originalTextEL;

    let hiddenFeedbackEL=document.querySelector("#add-members-choice-list [name='hidden-feedback']");
    hiddenFeedbackEL.innerText='';
    hiddenFeedbackEL.style.visibility='hidden';

    let response=await fetch('seeSecureAllMembers', {
        method:'post',
        headers:new Headers({'Content-Type':'application/json;charset=utf-8'}),
        redirect:"follow",
    });

    if(response.redirected===true){
        window.location.href=response.url;
    }
    else {
        let data = await response.json();

        let element;
        let subelement;
        let tableEL = document.querySelector("#add-members-choice-list [name='table']");
        tableEL.style.width="40%";

        document.getElementById('sub-title').style.display='inline';
        document.getElementById('sub-title').innerText='Select a Member';
        document.getElementById('add-members-choice-list').style.display='inline';

        let i;
        for (i = 0; i < data.length; i++) {
            element=document.createElement('tr');
            let j;
            for (j = 0; j < data[i].length; j++) {
                subelement=document.createElement('td');
                subelement.innerText=data[i][j];
                element.append(subelement);
            }
            let buttonEL;

            subelement=document.createElement('td');
            buttonEL=document.createElement('button');
            buttonEL.className='green-small-button';
            buttonEL.innerText='select';
            buttonEL.value=data[i][0];

            buttonEL.addEventListener('click', async ()=>{
                const Reg = {
                    id: buttonEL.value,
                    window: window,
                    type: type,
                    date: date,
                }

                let response=await fetch('addReg', {
                    method:'post',
                    headers:new Headers({'Content-Type':'application/json;charset=utf-8'}),
                    body:JSON.stringify(Reg),
                    redirect:"follow",
                });

                if(response.redirected===true){
                    window.location.href=response.url;
                }
                else {
                    let data=await response.json();
                    if(data==='added'){
                        await seeAllRegs('Added !');
                    }
                    else{
                        hiddenFeedbackEL.innerText=data;
                        hiddenFeedbackEL.style.visibility='visible';
                    }
                }

            });

            subelement.style.wordBreak= 'normal';
            subelement.append(buttonEL);
            element.append(subelement);

            tableEL.append(element);
        }
        document.querySelector("#add-members-choice-list [name='back']").addEventListener('click',()=>addSelectWindow(type,date));
    }
}

