"""Command-line interface."""

import click

from . import __version__


@click.command()
@click.option(
    "--language", "-l", default="hello", help="IDK", metavar="LANG", show_default=True,
)
@click.version_option(version=__version__)
def main(language: str) -> None:
    click.echo(language)
