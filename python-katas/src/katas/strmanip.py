"""String manipulation."""


def to_upper_case(given: str) -> str:
    """Returns 'given' in upper-case.

    >>> to_upper_case("foo")
    'FOO'
    >>> to_upper_case("Foo")
    'FOO'
    >>> to_upper_case("FOO")
    'FOO'
    >>> to_upper_case(" ") == " "
    True
    """
    return given.upper()


def to_lower_case(given: str) -> str:
    """Returns 'given' in lower case
    >>> to_lower_case("0D")
    '0d'
    """
    return given.lower()


if __name__ == "__main__":
    import doctest

    doctest.testmod(verbose=True)
