"""Test strmanip"""

import doctest
from katas import strmanip


def test_strmanip():
    failures, tests = doctest.testmod(strmanip, raise_on_error=True)
    assert tests > 0
    assert failures == 0
