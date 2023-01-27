# cipher-challenge

The project is the solution to Java Friday Challenge

Cipher (simple puzzle task):

- Create a program which replaces every digit with a letter to create a cipher.
- Same digits should be represented with the same letter.
- Program should allow to use expressions containing +,- and =.
- Examples (input => output):
  - 12+21=33 => ts+st=jj
  - 321-111=210 => xwc-ccc=wcm
  
Decipher (complex puzzle task)

- Create a program which will show all possible variations of digits ciphered with the letters.
- One letter should be used as a replacement for only one digit.
- Also, number shouldn't be 0 or have leading zeroes (like 01, 002, etc.)
- Examples (input => output):
  - xwc-ccc=wcm => 321-111=210; 642-222=420; 963-333=630; 926-666=260
  - xwc+ccc=wcm => 395+555=950; 197+777=974
  - hff+hff=pf+pf+pf+hpf => 200+200=50+50+50+250
  
Additional task:

- Implement possibility to process several expressions, like:
  - сw+wd=dw; cw-wd=wc
- In this case the result should satisfy all the expressions.
