document.getElementById('details-button').addEventListener('click', editDetails);

async function editDetails() {
    document.getElementById('text').innerHTML=originalTextEL;
    document.getElementById('sub-title').style.display='inline';
    document.getElementById('sub-title').innerText='Edit Details';
    document.getElementById('details-form').style.display='inline';

    let response=await fetch('getDetails', {
        method:'post',
        headers:new Headers({'Content-Type':'application/json;charset=utf-8'}),
        redirect:"follow",
    });

    if(response.redirected===true){
        window.location.href=response.url;
    }
    else {
        let data = await response.json();

        let nameEL=document.querySelector("#details-form [name='name']");
        nameEL.value=data[0];
        let phoneEL=document.querySelector("#details-form [name='phone']");
        phoneEL.value=data[1];
        let emailEL=document.querySelector("#details-form [name='email']");
        emailEL.value=data[2];
        let passwordEL=document.querySelector("#details-form [name='password']");
        passwordEL.value=data[3];

        document.querySelector("#details-form [name='submit']").addEventListener('click',validateEditDetailsForm);
    }


    async function validateEditDetailsForm() {
        let hiddenFeedbackEL = document.querySelector("#details-form [name='hidden-feedback']");
        hiddenFeedbackEL.innerText = '';
        hiddenFeedbackEL.style.visibility = 'hidden';
        let flag = true;

        let nameEL = document.querySelector("#details-form [name='name']");
        let hiddenNameEL = document.querySelector("#details-form [name='hidden-name']");
        if (isWord(nameEL.value) != true) {
            hiddenNameEL.innerText = 'Only letters !';
            hiddenNameEL.style.visibility = 'visible';
            flag = false;
        } else {
            hiddenNameEL.innerText = '';
            hiddenNameEL.style.visibility = 'hidden';
        }

        let phoneEL = document.querySelector("#details-form [name='phone']");
        let hiddenPhoneEL = document.querySelector("#details-form [name='hidden-phone']");
        if (isNumber(phoneEL.value) != true) {
            hiddenPhoneEL.innerText = 'Only numbers !';
            hiddenPhoneEL.style.visibility = 'visible';
            flag = false;
        } else {
            hiddenPhoneEL.innerText = '';
            hiddenPhoneEL.style.visibility = 'hidden';
        }

        let emailEL = document.querySelector("#details-form [name='email']");
        let hiddenEmailEL = document.querySelector("#details-form [name='hidden-email']");
        if (isEmail(emailEL.value) != true) {
            hiddenEmailEL.innerText = 'must has @ !';
            hiddenEmailEL.style.visibility = 'visible';
            flag = false;
        } else {
            hiddenEmailEL.innerText = '';
            hiddenEmailEL.style.visibility = 'hidden';
        }

        let passwordEL = document.querySelector("#details-form [name='password']");
        let hiddenPasswordEL = document.querySelector("#details-form [name='hidden-password']");
        if (isPassword(passwordEL.value) != true) {
            hiddenPasswordEL.innerText = 'At least 4 characters !';
            hiddenPasswordEL.style.visibility = 'visible';
            flag = false;
        } else {
            hiddenPasswordEL.innerText = '';
            hiddenPasswordEL.style.visibility = 'hidden';
        }

        const Member = {
            name: nameEL.value,
            phone: phoneEL.value,
            email: emailEL.value,
            password: passwordEL.value,
        }

        if (flag === true) {
            let response = await fetch('editDetails', {
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
                    hiddenFeedbackEL.innerText = 'Edited !';
                    hiddenFeedbackEL.style.color='green';
                    hiddenFeedbackEL.style.visibility = 'visible';
                } else {
                    hiddenFeedbackEL.innerText = data;
                    hiddenFeedbackEL.style.color='red';
                    hiddenFeedbackEL.style.visibility = 'visible';
                }
            }
        }
    }
}
