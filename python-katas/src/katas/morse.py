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


def as_audio(word):
    audio_files = dict(
        (s, AudioSegment.from_wav(f"{s}.wav"))
        for s in ["break", "space", "dit", "dah"]
    )
    infiles = [
        audio_files[s] for s in
        ["dit", "break", "dit", "break", "dit", "space",
         "dah", "break", "dah", "break", "dit", "space",
         "dit", "break", "dit", "break", "dit"]
    ]
    outfile_name = "sos.wav"

    outfile = audio_files["dit"]
    for infile in infiles:
        outfile = outfile + infile
    outfile.export(outfile_name, format="wav")


def create_audio(word):
    oldir = os.getcwd()
    try:
        os.chdir("/Users/rtimmons/Documents/Audacity")
        as_audio(word)
    finally:
        os.chdir(oldir)


def render(word: str) -> str:
    out = [ALL_CHARS[char.lower()] for char in list(word)]
    return "&nbsp;&nbsp;".join(out)


def get_words() -> List[str]:
    out = []
    with open("/usr/share/dict/words") as handle:
        out.extend([x.strip() for x in handle.readlines()
                    if len(x) <= 4])
    return out


def as_table(dic: dict):
    out = ["<table>"]
    for k, v in dic.items():
        out.append(f"<tr><td>{k}</td><td class=morse>{v}</td></tr>")
    out.append("</table>")
    return "\n".join(out)


def main():
    all_words = get_words()
    chosen = set()
    for _ in range(15):
        chosen.add(random.choice(all_words))

    create_audio("")
    mapping = dict(
        (choice, render(choice))
        for choice in chosen
    )

    style = """
    td.morse { font-family: monospace; }
    table, td { border: 1px solid black; }
    td { padding-right: 1em }
    
    """
    print(f"""\
    <html>
      <head>
        <style>{style}</style>
      </head>
      <body>
      {as_table(mapping)}
      </body>
    </html>
    """)


if __name__ == "__main__":
    main()

