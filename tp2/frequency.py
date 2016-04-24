#!/usr/bin/env python2
# -*- coding: utf-8 -*-

from string import ascii_uppercase
import sys

def main(args):
    message = args[1].upper().replace(' ', '')
    for letter in ascii_uppercase:
        print letter, message.count(letter) * 100.0 / len(message)

if __name__ == '__main__':
    main(sys.argv)
