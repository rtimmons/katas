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
    "6": "-....",
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


def get_words(word_len) -> List[str]:
    out = []
    with open("/usr/share/dict/words") as handle:
        out.extend(
            [
                x.strip()
                for x in handle.readlines()
                if len(x) == word_len + 1 and all_distinct(x)
            ]
        )
    return out


def as_table(dic: dict, out_dir: str = None, style="audio"):
    out = ["<table align=center width=100%>"]
    for k, v in dic.items():
        morse = f"<td class=morse>{v}</td>"
        slash_morse = f"<td class=morse>{v.replace('-','/')}</td>"
        word = f"<td class=word>{k}</td>"
        if style == "audio":
            assert out_dir
            src = f"file://{out_dir}/generated/{k}.wav"
            player = f"""<td class=player><audio loop=true controls><source src="{src}" type="audio/wav"></audio></td>"""
            out.append(f"""<tr>{player}{morse}{word}</tr>""")
        else:
            assert style == "worksheet"
            spacer = "<td class=spacer>&nbsp;&nbsp;</td>"
            out.append(f"<tr>{word}{spacer}{slash_morse}</tr>")
    out.append("</table>")
    return "\n".join(out)


def create_worksheet(
    out_dir, out_file, words=None, n=None, seed=None, add_signs: int = 0, word_len=3,
):
    if seed:
        random.seed(seed)

    if words is None:
        chosen = set()
        all_words = words if words else get_words(word_len=word_len)
        count = n if n else 200
        for _ in range(count):
            chosen.add(random.choice(all_words).lower())
    else:
        chosen = words

    for i in range(add_signs):
        chosen.add(SIGNS[i].lower())

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
      {as_table(mapping, out_dir)}
      </body>
    </html>
    """
    with open(f"{out_dir}/{out_file}.html", "w") as handle:
        handle.write(contents)


out_dir_x = "/Users/rtimmons/Desktop/morse"


def written_worksheet(out_dir, out_file, seed=None, n=None, word_len=5, add_signs=10):
    if seed:
        random.seed(seed)

    chosen = set()
    all_words = get_words(word_len=word_len)
    count = n if n else 200
    for _ in range(count):
        chosen.add(random.choice(all_words).lower())

    for i in range(add_signs):
        chosen.add(SIGNS[i].lower())

    shuffled = list(chosen)
    random.shuffle(shuffled)

    mapping = dict((choice, render(choice)) for choice in shuffled)

    script = """"""

    style = """
    body, table { margin: 0; padding: 0; }
    
    td.morse, td.word { width: 0%; }
    td.spacer { width: 100%; }
    
    tr { height: 2.5em }
    td { font-size: 1.5em } 

    td.morse { 
        font-family: monospace;
        font-size: 1em; 
    }

    table, td { border: 1px solid black; }
    """

    contents = f"""\
        <html>
          <head>
            <style>{style}</style>
            <script type="text/javascript">{script}</script>
          </head>
          <body>
          {as_table(mapping, style="worksheet")}
          </body>
        </html>
        """

    with open(f"{out_dir}/{out_file}.html", "w") as handle:
        handle.write(contents)


def written_main():
    out_dir = out_dir_x
    written_worksheet(
        out_dir=out_dir, out_file="worksheet_1", n=100, word_len=5, add_signs=0
    )


def audio_main():
    out_dir = out_dir_x
    create_worksheet(
        out_dir=out_dir,
        out_file="letters_only",
        words=list("abcdefghijklmnopqrstuvwxyz"),
    )
    create_worksheet(
        out_dir=out_dir,
        out_file="numbers_and_letters",
        words=list("abcdefghijklmnopqrstuvwxyz0123456789"),
    )
    for i in range(20):
        create_worksheet(
            out_dir=out_dir, out_file=f"3letters_{i}", seed=500, word_len=3,
        )
    for i in range(20):
        create_worksheet(
            out_dir=out_dir, out_file=f"4letters_{i}", seed=500, word_len=4,
        )
    for i in range(20):
        create_worksheet(
            out_dir=out_dir,
            out_file=f"3letters_signs_{i}",
            seed=50,
            word_len=3,
            n=40,
            add_signs=10,
        )


if __name__ == "__main__":
    written_main()
