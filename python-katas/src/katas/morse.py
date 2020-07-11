#!/usr/bin/env python3

import random
from pydub import AudioSegment
import os
from typing import List

LETTERS = {
    "a": ".-",
    "b": "-...",
    "c": "-.-.",
    "d": "-..",
    "e": ".",
    "f": "..-.",
    "g": "--.",
    "h": "....",
    "i": "..",
    "j": ".---",
    "k": "-.-",
    "l": ".-..",
    "m": "--",
    "n": "-.",
    "o": "---",
    "p": ".--.",  # :(
    "q": "--.-",
    "r": ".-.",
    "s": "...",
    "t": "-",
    "u": "..-",
    "v": "...-",
    "w": ".--",
    "x": "-..-",
    "y": "-.--",
    "z": "--..",
}

NUMBERS = {
    "1": ".----",
    "2": "..---",
    "3": "...--",
    "4": "....-",
    "5": ".....",
    "6": "-...",
    "7": "--...",
    "8": "---..",
    "9": "----.",
    "0": "-----",
}

SIGNS = [
    "KD9NYE",
    "HA7TM",
    "KC3IME",
    "HA7TM",
    "CO7HNS",
    "W0LEN",
    "CO6ABP",
    "AC0DA",
    "W0BLE",
    "MI0SAI",
    "AA4LS",
    "HA5UK",
    "DL3TW",
    "KD9JJR",
    "K9BAZ",
    "N9SW",
    "NG3Y",
    "K4PDS",
    "VE3XN",
    "N9MUF",
    "WD8DAU",
    "W8OCC",
    "W5NRF",
    "K5EW",
    "N2CEC",
    "KY0R",
    "CO8RCP",
    "KE0NNE",
    "KD9EYW",
    "AC2SB",
    "VE3UIN",
    "KP4AF",
    "W7OK",
    "KE0GSZ",
    "KP4SE",
    "K9ZW",
    "WV8CQ",
    "KE8YP",
    "K8ZT",
    "NK1I",
    "K4NYX",
    "K9STL",
    "KI4MRH",
    "KI4MRH",
    "VA3FF",
    "KC9YTT",
    "K2J",
    "KB8QYJ",
    "KB8QYJ",
    "AB8SP",
    "KB9PKI",
    "N8CQD",
    "J68HZ",
]
random.shuffle(SIGNS)


ALL_CHARS = dict()
ALL_CHARS.update(LETTERS)
ALL_CHARS.update(NUMBERS)


def all_distinct(word: str):
    seen = set("")
    for letter in list(word):
        if letter in seen:
            return False
        seen.add(letter)
    return True


def as_dit_dah(word: str):
    sound_groups = [ALL_CHARS[c] for c in list(word)]
    sounds = []
    for group in sound_groups:
        for sound in list(group):
            sounds.append("dit" if sound == "." else "dah")
            sounds.append("break")
        sounds.append("space")
        sounds.append("space")
        sounds.append("space")
    sounds.append("space")
    sounds.append("space")
    sounds.append("space")
    sounds.append("space")
    sounds.append("space")
    return sounds


def as_audio(word, to_create: str):
    audio_files = dict(
        (s, AudioSegment.from_wav(f"{s}.wav")) for s in ["break", "space", "dit", "dah"]
    )
    infiles = [audio_files[s] for s in as_dit_dah(word)]

    outfile = audio_files["break"]
    for infile in infiles:
        outfile = outfile + infile
    outfile.export(to_create, format="wav")


AUDIO_SOURCE = os.path.abspath(os.path.dirname(__file__))


def create_audio(word, out_dir):
    oldir = os.getcwd()
    try:
        os.chdir(AUDIO_SOURCE)
        to_create = f"{out_dir}/generated/{word}.wav"
        if not os.path.exists(to_create):
            as_audio(word, to_create)
    finally:
        os.chdir(oldir)


def render(word: str) -> str:
    out = [ALL_CHARS[char.lower()] for char in list(word)]
    return "&nbsp;&nbsp;".join(out)


def get_words() -> List[str]:
    out = []
    with open("/usr/share/dict/words") as handle:
        out.extend(
            [x.strip() for x in handle.readlines() if len(x) == 4 and all_distinct(x)]
        )
    return out


def as_table(dic: dict):
    out = ["<table align=center>"]
    for k, v in dic.items():
        src = f"file://./generated/{k}.wav"
        player = f"""<audio loop=true controls><source src="{src}" type="audio/wav"></audio>"""
        out.append(
            f"""<tr><td class=player>{player}</td><td class=morse>{v}</td><td class=word>{k}</td></tr>"""
        )
    out.append("</table>")
    return "\n".join(out)


def create_worksheet(
    out_dir, out_file, words=None, n=None, seed=None, add_signs: int = 0
):
    if seed:
        random.seed(seed)

    if words is None:
        chosen = set()
        all_words = words if words else get_words()
        count = n if n else 200
        for _ in range(count):
            chosen.add(random.choice(all_words).lower())
    else:
        chosen = words

    if add_signs:
        for i in range(add_signs):
            chosen.add(SIGNS[i])

    for choice in chosen:
        create_audio(choice, out_dir)

    shuffled = list(chosen)
    random.shuffle(shuffled)

    mapping = dict((choice, render(choice)) for choice in shuffled)

    script = """
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
window.onload = function() {
  clickable(".word");
  clickable(".morse");
};
    """

    style = """
    td.morse { font-family: monospace; }
    table, td { border: 1px solid black; }
    td { padding-right: 1em }
    
    """

    contents = f"""\
    <html>
      <head>
        <style>{style}</style>
        <script type="text/javascript">{script}</script>
      </head>
      <body>
      {as_table(mapping)}
      </body>
    </html>
    """
    with open(f"{out_dir}/{out_file}.html", "w") as handle:
        handle.write(contents)


def letters_only():
    create_worksheet(
        out_dir="/Users/rtimmons/Desktop/morse",
        out_file="letters_only",
        words=list("abcdefghijklmnopqrstuvwxyz"),
    )


if __name__ == "__main__":
    # main()
    letters_only()
