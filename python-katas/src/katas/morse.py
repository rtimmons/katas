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

ALL_CHARS = dict()
ALL_CHARS.update(LETTERS)
ALL_CHARS.update(NUMBERS)


def as_dit_dah(word: str):
    sound_groups = [ALL_CHARS[c] for c in list(word)]
    sounds = []
    for group in sound_groups:
        for sound in list(group):
            sounds.append("dit" if sound == "." else "dah")
            sounds.append("break")
        sounds.append("space")
    sounds.append("space")
    return sounds


def as_audio(word, to_create: str):
    audio_files = dict(
        (s, AudioSegment.from_wav(f"{s}.wav"))
        for s in ["break", "space", "dit", "dah"]
    )
    infiles = [
        audio_files[s] for s in
        as_dit_dah(word)
    ]

    outfile = audio_files["break"]
    for infile in infiles:
        outfile = outfile + infile
    outfile.export(to_create, format="wav")


AUDIO_SOURCE = os.path.abspath(os.path.dirname(__file__))


def create_audio(word):
    oldir = os.getcwd()
    try:
        os.chdir(AUDIO_SOURCE)
        to_create = f"generated/{word}.wav"
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
        out.extend([x.strip() for x in handle.readlines()
                    if len(x) == 3])
    return out


def as_table(dic: dict):
    out = ["<table align=center>"]
    for k, v in dic.items():
        src = f"file://{AUDIO_SOURCE}/generated/{k}.wav"
        player = f"""<audio loop=true controls><source src="{src}" type="audio/wav"></audio>"""
        out.append(f"""<tr><td class=player>{player}</td><td class=morse>{v}</td><td class=word>{k}</td></tr>""")
    out.append("</table>")
    return "\n".join(out)


def main():
    random.seed(30)

    all_words = get_words()
    chosen = {"ki9i"}
    for _ in range(15):
        chosen.add(random.choice(all_words).lower())

    for choice in chosen:
        create_audio(choice)

    mapping = dict(
        (choice, render(choice))
        for choice in chosen
    )

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
    print(f"""\
    <html>
      <head>
        <style>{style}</style>
        <script type="text/javascript">{script}</script>
      </head>
      <body>
      {as_table(mapping)}
      </body>
    </html>
    """)


if __name__ == "__main__":
    main()

