
function clickable(selector) {
  words = document.querySelectorAll(selector);
  for (const word of words) {
    word.style.color = "white";
    word.visible = false;
    word.onclick = () => {
      word.visible = ! word.visible;
      word.style.color = word.visible ? 'black' : 'white';
    };
  }
}

function makeClickables() {
  clickable(".word");
  clickable(".morse");
}


function makePicker(){
    const answer = document.querySelector('.answer');
    if (!answer) {
        return;
    }
    const sign = answer.innerHTML;
    const input = document.querySelector("input#callsign");
    if (!input) {
        return;
    }
    input.onkeyup = function() {
        const value = this.value.toUpperCase();
        let pass = true;
        for(let i=0; i < sign.length; ++i) {
            const ofSign = sign[i];
            const ofValue = i >= value.length ? undefined : value[i];
            if (ofValue === '?') {
                this.value = this.value.substr(0, i) + ofSign;
                break;
            }
            if (ofValue && ofValue != ofSign) {
                pass = false;
                console.log(`${ofSign} != ${ofValue}`)
            }
        }
        this.style.backgroundColor = pass ? 'lime' : 'red';
        console.log(value);
    };
    input.onkeyup()
    console.log(input)
};

window.onload = function() {
    console.log(`Started ${new Date()}`);
    makeClickables();
    makePicker();
    console.log(`Finished ${new Date()}`)
};